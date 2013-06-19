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
 * Date: 6/17/13
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaveAsActionListener implements ActionListener {

    private MainFrame parent;

    protected SaveAsActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter;
        if (parent.mode == Mode.GAME){
            filter = new FileNameExtensionFilter(
                    "Saved Game Files", "svg");
            chooser.setCurrentDirectory(new File(GameMediator.path+"Saves"));
            chooser.setSelectedFile(new File("untitled.svg"));
        }
        else{
            filter = new FileNameExtensionFilter(
                    "Saved Board Files", "brd");
            chooser.setCurrentDirectory(new File(GameMediator.path+"Saves"));
            chooser.setSelectedFile(new File("untitled.brd"));
        }
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(parent);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            parent.saveFile(chooser.getSelectedFile());
        }
    }
}
