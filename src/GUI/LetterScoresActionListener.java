package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class LetterScoresActionListener implements ActionListener {

    private MainFrame parent;

    protected LetterScoresActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = "";
        for (int i=65; i<92; i++){
            if (i == 91){
                s += "_: 0";
            }
            else{
                Character c = (char)i;
                int score = parent.board.calcRawScore(c+"");
                s += c+": "+score+"\n";
            }
        }
        JOptionPane.showMessageDialog(parent, s, "Values for letters:", JOptionPane.PLAIN_MESSAGE);
    }
}
