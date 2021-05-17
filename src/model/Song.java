
package model;

import java.io.Serializable;

public class Song implements Serializable{
    
    private static final long serialVersionUID = 1200940182021L;
    
    private String songName = null;
    
    public Song(String songName) {
        this.songName = fileNameFilter(songName);
    }
    public Song() {}

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
    private String fileNameFilter(String fileName) {
        return fileName.substring(0,fileName.length() - 4).replaceAll("\\(.*?\\)|[0-9_]", " ").trim();
       
    }
}
