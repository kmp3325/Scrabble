package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class HandLetterTypedListener implements KeyListener {

    private MainFrame parent;

    protected HandLetterTypedListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = Character.toUpperCase(e.getKeyChar());
        if ((c > 64 && c < 91) || c == '_'){
            JTextField b = (JTextField) e.getSource();
            Rectangle r = b.getBounds();
            Point p = b.getLocation();
            int spot = p.x / r.width;
            moveCursor(KeyEvent.VK_RIGHT, spot);
        }
        else if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE){
            JTextField b = (JTextField) e.getSource();
            Rectangle r = b.getBounds();
            Point p = b.getLocation();
            int spot = p.x / r.width;
            moveCursor(KeyEvent.VK_LEFT, spot);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_HOME
                || e.getKeyCode() == 227){
            JTextField b = (JTextField) e.getSource();
            Rectangle r = b.getBounds();
            Point p = b.getLocation();
            int spot = p.x / r.width;
            moveCursor(KeyEvent.VK_RIGHT, spot);
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_END
                || e.getKeyCode() == 226){
            JTextField b = (JTextField) e.getSource();
            Rectangle r = b.getBounds();
            Point p = b.getLocation();
            int spot = p.x / r.width;
            moveCursor(KeyEvent.VK_LEFT, spot);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Handled best in keyTyped.
    }

    /**
     * Called by the KeyListeners to move the cursor when letters or keys are
     * pressed or typed into the hand.
     * @param direction The VK corresponding to the direction (KeyEvent.VK_LEFT, etc).
     * @param spot The spot that we are currently at in the hand.
     */
    private void moveCursor(int direction, int spot){
        if (direction == KeyEvent.VK_RIGHT){
            if (spot == parent.handSize-1) parent.getBestMoveButton.requestFocusInWindow();
            else parent.hand.get(spot+1).requestFocusInWindow();
        }
        else if (direction == KeyEvent.VK_LEFT){
            if (spot == 0) parent.clearBoardButton.requestFocusInWindow();
            else parent.hand.get(spot-1).requestFocusInWindow();
        }
    }
}
