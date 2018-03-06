package main.ftp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import main.untility.UploadStats;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.*;
import org.apache.commons.net.io.CopyStreamAdapter;

public class FTPUploader {

    static  long percentage = 0;
    File file;
    FTPClient ftp = null;

    public interface updateProgress{

            static long update(){
                return percentage;
            }


    }
    private final UploadStats uploadStats = new UploadStats();
    CopyStreamAdapter copyStreamAdapter = new CopyStreamAdapter() {
        @Override
        public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
           // updateUploadStats(totalBytesTransferred, bytesTransferred, streamSize);
            percentage = ((int)totalBytesTransferred*100/file.length());
        }
    };
    public FTPUploader(String host, String user, String pwd) throws Exception{


        ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftp.connect(host);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.setCopyStreamListener(copyStreamAdapter);
        ftp.login(user, pwd);
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
    }
    public void uploadFile(String localFileFullName, String fileName, String hostDir,File file)
            throws Exception {
        this.file = file;
        try(InputStream input = new FileInputStream(new File(localFileFullName))){
            this.ftp.storeFile(hostDir + fileName, input);

        }
    }

    public boolean mkdir(String album_id){
        boolean answer = false;



        try{
            String dir = "/"+album_id;
            answer = ftp.makeDirectory(dir);
            showServerReply(ftp);
            return answer;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer;

    }
    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    public boolean checkDirectoryExists(String file) throws IOException {
        FTPFile[] files = ftp.listDirectories();
        for (FTPFile ftpfile : files) {
            if( ftpfile.getName().endsWith(file)) return true;
        }
        return false;
    }


    public void disconnect(){
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
                // do nothing as file is already saved to server
            }
        }
    }
    /*public static void main(String[] args) throws Exception {
        System.out.println("Start");
        FTPUploader ftpUploader = new FTPUploader("ftp.journaldev.com", "ftpUser", "ftpPassword");
        //FTP server path is relative. So if FTP account HOME directory is "/home/pankaj/public_html/" and you need to upload
        // files to "/home/pankaj/public_html/wp-content/uploads/image2/", you should pass directory parameter as "/wp-content/uploads/image2/"
        ftpUploader.uploadFile("D:\\Pankaj\\images\\MyImage.png", "image.png", "/wp-content/uploads/image2/");
        ftpUploader.disconnect();
        System.out.println("Done");
    }*/

    private void updateUploadStats(long totalBytesTransferred, int bytesTransferred, long streamSize) {
        long current = System.currentTimeMillis();

        synchronized (this.uploadStats) {
            long timeTaken = (current - uploadStats.getStartTime());


            if (timeTaken > 1000L) {
                uploadStats.setLastUpdated(current);
                uploadStats.setEstimatedSpeed(totalBytesTransferred / (timeTaken/1000L));
            }

            uploadStats.setTotalBytesTransferred(totalBytesTransferred);
            uploadStats.setBytesTransferred(bytesTransferred);
            uploadStats.setStreamSize(streamSize);
            System.out.println("Total bytes transfered : "+uploadStats.getTotalBytesTransferred());
            System.out.println("bytes transfered : "+uploadStats.getBytesTransferred());
            System.out.println("Stream Size : "+uploadStats.getStreamSize());

        }
    }

}