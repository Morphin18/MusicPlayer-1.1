
package model;

import javax.swing.DefaultListModel;


public class ModelPlayList {
    
    private DefaultListModel<MusicForPlay> listModel;
    
    public ModelPlayList() {
      this.listModel = new DefaultListModel<>();
    }
    public void addElement(MusicForPlay music) {
        listModel.addElement(music);
        
    }

    public DefaultListModel<MusicForPlay> getListModel() {
        return listModel;
    }
   public void setListModel(DefaultListModel<MusicForPlay> list) {
       this.listModel = list;
   }
}
