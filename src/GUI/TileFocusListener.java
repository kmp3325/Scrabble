package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TileFocusListener implements FocusListener {

    private MainFrame parent;

    protected TileFocusListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void focusGained(FocusEvent e) {
        JTextField b = (JTextField)e.getSource();
        parent.letterSelected = b;
        b.selectAll();
    }

    @Override
    public void focusLost(FocusEvent e) {
        JTextField b = (JTextField)e.getSource();
        b.select(0, 0); // Remove selection.
        boolean good = true;
        if (parent.virtualBoard.indexOf(b)%parent.board.getDimension() == -1){
            good = false; // Happens when window focus is lost.
        }
        if (b.getText().length() > 1){
            b.setForeground(Color.BLUE);
        }
        else if (parent.usedBlank ||
                (good &&
                 parent.board.isBlank(parent.virtualBoard.indexOf(b)/parent.board.getDimension()
                         , parent.virtualBoard.indexOf(b)%parent.board.getDimension()))){
            b.setForeground(Color.RED);
        }
        else if(b.getText().length() == 1 && !b.getForeground().equals(Color.RED)){
            b.setForeground(Color.BLACK);
        }
        else if (b.getText().length() == 0){
            b.setForeground(Color.BLACK);
        }
        parent.usedBlank = false;
        parent.letterSelected = null;
    }
}
