
package model;


import java.io.Serializable;


public class MusicForPlay implements Serializable{
    
    private static final long serialVersionUID = 1200940182020L;
    
    private Song song = null;
    private String path = null;
 

    public MusicForPlay(Song song, String path) {
        this.song = song;
        this.path = path;
    }
    public MusicForPlay() {}

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return song.getSongName();
    }

    
    
    
    
   

   
    
    
}
