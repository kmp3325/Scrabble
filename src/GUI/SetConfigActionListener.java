package GUI;

import Game.GameMediator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 7:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class SetConfigActionListener implements ActionListener {

    private MainFrame parent;

    protected SetConfigActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Config Files", "cfg");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(new File(GameMediator.path+"Configurations"));
        int returnVal = chooser.showOpenDialog(parent);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            if (parent.validateConfigFile(chooser.getSelectedFile())){
                parent.setConfig(chooser.getSelectedFile());
            }
            else{
                JOptionPane.showMessageDialog(parent, "This configuration file is invalid.",
                        "INVALID CONFIGURATION", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
