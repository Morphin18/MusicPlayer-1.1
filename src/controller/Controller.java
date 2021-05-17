package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jlgui.basicplayer.BasicPlayer;
import model.MusicForPlay;
import model.Song;
import view.MainFrame;

public class Controller implements Action {

    private JFrame view = null;
    private Sound sound = null;
    private DefaultListModel<MusicForPlay> playlist = MainFrame.getModel().getListModel();

    public Controller(JFrame view) {
        this.view = view;
    }

    public Controller() {
    }

    @Override
    public void add() {
        JFileChooser chooser = new JFileChooser("C:\\");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Mp3 Files", "mp3", "mpeg3");
        chooser.addChoosableFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(true);

        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();

            if (selectedFiles != null) {
                for (File selectedFile : selectedFiles) {
                    String path = selectedFile.getPath();
                    String songName = selectedFile.getName();
                    MainFrame.updatePlaylist(new MusicForPlay(new Song(songName), path));
                }
            }

        }
    }

    @Override
    public void del() {
        int[] indexPlayList = MainFrame.getjPlayList().getSelectedIndices();
        if (indexPlayList.length > 0) {
            ArrayList<MusicForPlay> listForRemove = new ArrayList<>();
            for (int i = 0; i < indexPlayList.length; i++) {
                MusicForPlay music = MainFrame.getModel().getListModel().getElementAt(indexPlayList[i]);
                listForRemove.add(music);
            }
            for (MusicForPlay mfp : listForRemove) {
                MainFrame.getModel().getListModel().removeElement(mfp);
            }
        }

    }

    @Override
    public void up() {
        if (!playlist.isEmpty()) {
            int indexSelected = MainFrame.getjPlayList().getSelectedIndex() - 1;
            if (indexSelected <= MainFrame.getModel().getListModel().getSize() - 1) {
                MainFrame.getjPlayList().setSelectedIndex(indexSelected);
            }
        }

    }

    @Override
    public void down() {
        if (!playlist.isEmpty()) {
            int indexSelected = MainFrame.getjPlayList().getSelectedIndex() + 1;
            if (indexSelected <= MainFrame.getModel().getListModel().getSize() - 1) {
                MainFrame.getjPlayList().setSelectedIndex(indexSelected);
            }
        }
    }

    @Override
    public void play() {
        if (sound != null) {

            if (sound.getPlayerStatus() == BasicPlayer.PLAYING) {
                sound.stop();
            }
            if (sound.getPlayerStatus() == BasicPlayer.PAUSED) {
                sound.resume();
            }
            
        }
        int indexSelected = MainFrame.getjPlayList().getSelectedIndex();
        if (!playlist.isEmpty() && indexSelected >= 0) {
            try {
                sound = new Sound(new FileInputStream(playlist.get(indexSelected).getPath()));

                MainFrame.updateDisplay(playlist.get(indexSelected).getSong().getSongName());
                MainFrame.setjSliderEnabled(true);
                sound.play();
                

            } catch (FileNotFoundException | JavaLayerException e) {
                e.getStackTrace();
            }
         
        }
     
    }

    @Override
    public void pause() {
        if (sound != null) {
            sound.pause();
        }
    }

    @Override
    public void stop() {
        if (sound != null) {
            sound.stop();
        }
    }

    @Override
    public void mute() {
        if (sound != null) {
            sound.mute();
        }
    }

    @Override
    public void volume() {

        if (sound != null) {
            sound.setVolume(MainFrame.getjSliderValue());
            MainFrame.getFieldVol().setText(String.valueOf(MainFrame.getjSliderValue()));
        }
    }

    @Override
    public void next() {
        down();
        play();

    }

    @Override
    public void back() {
        up();
        play();
    }

    @Override
    public void find() {
        String nameFind = MainFrame.getjTextFieldSearch().getText();
        if (!playlist.isEmpty() && nameFind != null) {
            for (int i = 0; i < playlist.getSize(); i++) {
                if (playlist.get(i).getSong().getSongName().contains(nameFind)) {
                    MainFrame.getjPlayList().setSelectedIndex(i);
                    MainFrame.getjTextFieldSearch().setText("");
                    break;
                }
            }
        }
    }

    public void save() {
        if (!playlist.isEmpty()) {
            JFileChooser chooser = new JFileChooser("C:\\");
            chooser.setDialogTitle("Save playlist");
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("File playlist", "pls"));
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                if (selectedFile.exists()) {
                    int result = JOptionPane.showConfirmDialog(view, "File exists", "Overwrite?", JOptionPane.YES_NO_CANCEL_OPTION);
                    switch (result) {
                        case JOptionPane.NO_OPTION:
                            save();
                            break;
                        case JOptionPane.CANCEL_OPTION:
                            chooser.cancelSelection();
                            break;
                    }
                    chooser.approveSelection();
                }
                String playlistSave = selectedFile.getAbsolutePath() + ".pls";
                try ( FileOutputStream fos = new FileOutputStream(playlistSave);  ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    if (playlist != null) {
                        oos.writeObject(playlist);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(view, "Playlist is empty");
        }
    }

    public void open() {
        JFileChooser chooser = new JFileChooser("C:\\");
        chooser.setDialogTitle("Open playlist");
        chooser.setFileFilter(new FileNameExtensionFilter("File playlist", "pls"));
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            try ( FileInputStream fis = new FileInputStream(selectedFile.getAbsolutePath());  ObjectInputStream ois = new ObjectInputStream(fis)) {
                playlist.clear();
                playlist = (DefaultListModel<MusicForPlay>) ois.readObject();
                MainFrame.clearPlaylist();
                for (int i = 0; i < playlist.getSize(); i++) {
                    MainFrame.updatePlaylist(playlist.getElementAt(i));
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void exit() {
        int result = JOptionPane.showConfirmDialog(view, "Do you want exit?", "Exit", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    public void about() {
        JOptionPane.showMessageDialog(view, "Music Player 1.1 version\n by Morphine");
    }

}
