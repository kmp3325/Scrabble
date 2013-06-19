package GUI;

import Game.GameMediator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 7:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class SetDefaultConfigActionListener implements ActionListener {

    private MainFrame parent;

    protected SetDefaultConfigActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Config Files", "cfg");
        chooser.setFileFilter(filter);
        File configs = new File(GameMediator.path+"Configurations");
        chooser.setCurrentDirectory(configs);
        int returnVal = chooser.showOpenDialog(parent);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            if (parent.validateConfigFile(chooser.getSelectedFile())){
                try {
                    FileWriter newDefault = new FileWriter(new File(GameMediator.path+"Configurations/Default.txt"));
                    newDefault.write(chooser.getSelectedFile().getName());
                    newDefault.close();
                    JOptionPane.showMessageDialog(parent, "Your Default Configuration File has been set, for" +
                            "future executions, this program will run with: "+chooser.getSelectedFile().getName());
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(parent, "The Configurations folder could not be " +
                            "found, please make sure that the folder labeled 'Configurations' is in" +
                            "the same directory as Scrabble.exe.", "No Configurations Folder", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
            else{
                JOptionPane.showMessageDialog(parent, "This configuration file is invalid.",
                        "INVALID CONFIGURATION", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
