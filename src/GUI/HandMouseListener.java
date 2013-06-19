package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class HandMouseListener implements MouseListener {

    private MainFrame parent;
    protected static JTextField letterToRemoveFromHand = null;

    protected HandMouseListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (parent.mode == Mode.GAME){
            letterToRemoveFromHand = (JTextField)e.getSource();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        String text = ((JTextField)e.getSource()).getText();
        if (text.length() != 1 || text.equals("*")) text = "_";
        ((JTextField)e.getSource()).setToolTipText(parent.board.calcRawScore(text)+"");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
