package main;

import controller.buildFrame;
import static javax.swing.SwingUtilities.invokeLater;
import javax.swing.UIManager;
import view.MainFrame;

public class Application {

    public static void main(String[] args) {

        try {
         
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        try {
            invokeLater(new Runnable() {
            @Override
            public void run() {
                buildFrame.loader();
            }
        });
        } catch (Exception e) {
            e.getStackTrace();
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }

    }
}
