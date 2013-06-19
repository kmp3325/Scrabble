package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerScoresActionListener implements ActionListener {

    private MainFrame parent;

    protected PlayerScoresActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = "";
        HashMap<String, Integer> mapping = parent.mediator.getPlayerScores();
        ArrayList<String> toSort = new ArrayList<String>();
        for (String player : mapping.keySet()){
            toSort.add(player+": "+mapping.get(player));
        }
        Collections.sort(toSort);
        for (String p : toSort) s+= p+"\n";
        JOptionPane.showMessageDialog(parent, s, "Current Player scores:", JOptionPane.PLAIN_MESSAGE);
    }
}
