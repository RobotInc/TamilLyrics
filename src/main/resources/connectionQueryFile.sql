CREATE TABLE hero( id INT PRIMARY KEY AUTO_INCREMENT, hero_name varchar(50) UNIQUE not null)
CREATE TABLE heroin( id INT PRIMARY KEY AUTO_INCREMENT, heroin_name varchar(50) UNIQUE not null)
CREATE TABLE artist( id INT PRIMARY KEY AUTO_INCREMENT, artist_name varchar(50) UNIQUE not null)
CREATE TABLE lyricist( id INT PRIMARY KEY AUTO_INCREMENT, lyricist_name varchar(50) UNIQUE not null)
CREATE TABLE albums(
    id int PRIMARY KEY AUTO_INCREMENT,
    album_name varchar(255) not null UNIQUE,
    artist_id int not null,
    FOREIGN KEY fk_artist(artist_id)
    REFERENCES artist(id),
    hero_id int not null,
    FOREIGN KEY fk_hero(hero_id)
    REFERENCES hero(id),
    heroin_id int not null,
    FOREIGN KEY fk_heroin(heroin_id)
    REFERENCES heroin(id),
    language int not null,
    year int not null,
    image_link varchar(255) not null)

CREATE TABLE songs(
    id int PRIMARY KEY AUTO_INCREMENT,
    album_id int not null,
    FOREIGN KEY fk_albums(album_id)
    REFERENCES albums(id),
    song_title varchar(255) not null,
    download_link text not null,
    lyrics_one text utf8mb4_unicode_ci not null,
    lyrics_two text utf8mb4_unicode_ci not null,
    lyrics_three text utf8mb4_unicode_ci not null,
    lyrics_four text utf8mb4_unicode_ci not null,
    genre_id int,
    FOREIGN KEY fk_genre(genre_id)
    REFERENCES genre(id),
    lyricist_id int,
    FOREIGN KEY fk_lyricist(lyricist_id)
    REFERENCES lyricist(id),
    track_no int)

    CREATE TABLE songs(
        id int PRIMARY KEY AUTO_INCREMENT,
        album_id int not null,
        FOREIGN KEY fk_albums(album_id)
        REFERENCES albums(id),
        song_title varchar(255) not null,
        download_link text not null,
        lyrics_one text not null,
        lyrics_two text not null,
        lyrics_three text not null,
        lyrics_four text not null,
        genre_id int,
        FOREIGN KEY fk_genre(genre_id)
        REFERENCES genre(id),
        lyricist_id int,
        FOREIGN KEY fk_lyricist(lyricist_id)
        REFERENCES lyricist(id),
        track_no int)
        CHARACTER set UTF8
        COLLATE utf8mb4_unicode_ci

