package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class HelpActionListener implements ActionListener {

    private MainFrame parent;

    protected HelpActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(parent, "In order to set a tile as blank (so that no score will be added when using it) simply press either\n" +
                "'-' or '_'.  This only applies when not playing a game.  (Note that blank tiles will show up RED)\n\n" +
                "TYPE ASSIST: Use the horizontal and vertical buttons above your hand to direct which way the cursor moves\n" +
                "while typing words.  Alternatively, hit ALT+H for horizontal and ALT+V for vertical.\n" +
                "Please note, there may be graphical issues that cause the board to suddenly appear as though \nall of the letters" +
                "and tiles have been mixed up.  This should only occur during gameplay.  \nIf so, simply save your game and reload.");
    }
}
