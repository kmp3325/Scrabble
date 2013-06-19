package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class BoardLetterTypedListener implements KeyListener {

    private MainFrame parent;

    protected BoardLetterTypedListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = Character.toUpperCase(e.getKeyChar());
        JTextField b = (JTextField) e.getSource();
        Rectangle r = b.getBounds();
        Point p = b.getLocation();
        int row = p.y / r.height;
        int col = p.x / r.width;
        if ((c == '_' || c == '-') && parent.mode == Mode.CHEAT){
            if (!parent.board.isBlank(row, col)){
                parent.board.setBlank(row, col);
                b.setForeground(Color.RED);
            }
            else{
                parent.board.removeBlank(row, col);
                b.setForeground(Color.BLACK);
            }
        }
        if ((c > 64 && c < 91 && parent.mode == Mode.CHEAT) || (parent.mode == Mode.GAME &&
                (parent.getHandString().contains(c+"") || (parent.getHandString().contains("_")
                        && c > 64 && c < 91)))){
            if (parent.mode == Mode.CHEAT) parent.board.placeLetter(c, row, col);
            else if (parent.mode == Mode.GAME){
                for (JTextField t : parent.hand){
                    if (t.getText().equals(c+"")){
                        String on = b.getText();
                        if (b.getForeground().equals(Color.RED)){
                            parent.toAddToHand = "_";
                            b.setForeground(Color.BLACK);
                        }
                        else if (!on.equals("") && on.length() == 1 && !on.equals("*")){
                            parent.toAddToHand = on;
                        }
                        parent.toRemoveFromHand = t;
                        break;
                    }
                }
                if (!parent.getHandString().contains(c+"")){
                    parent.usedBlank = true;
                    String on = b.getText();
                    if (b.getForeground().equals(Color.RED)){
                        parent.toAddToHand = "_";
                    }
                    else if (!on.equals("") && on.length() == 1 && !on.equals("*")){
                        parent.toAddToHand = on;
                    }
                    for (JTextField t : parent.hand){
                        if (t.getText().equals("_")){
                            parent.toRemoveFromHand = t;
                            b.setForeground(Color.RED);
                            break;
                        }
                    }
                }
            }
            parent.letterSelected.setText("");
            if (parent.verticalButton.isSelected()) moveCursor(KeyEvent.VK_DOWN, row, col);
            else moveCursor(KeyEvent.VK_RIGHT, row, col);
        }
        else if (c == KeyEvent.VK_ENTER){
            if (parent.mode == Mode.CHEAT) parent.getBestMoveButton.doClick();
            else if (parent.mode == Mode.GAME) parent.playButton.doClick();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        JTextField b = (JTextField) e.getSource();
        Rectangle r = b.getBounds();
        Point p = b.getLocation();
        int row = p.y / r.height;
        int col = p.x / r.width;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_HOME
                || e.getKeyCode() == 227){
            moveCursor(KeyEvent.VK_RIGHT, row, col);
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_END
                || e.getKeyCode() == 226){
            moveCursor(KeyEvent.VK_LEFT, row, col);
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == 224){
            moveCursor(KeyEvent.VK_UP, row, col);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == 225){
            moveCursor(KeyEvent.VK_DOWN, row, col);
        }

        char c = Character.toUpperCase(e.getKeyChar());
        if (c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE){
            parent.board.removeBlank(row, col);
            if (parent.verticalButton.isSelected()) moveCursor(KeyEvent.VK_UP, row, col);
            else moveCursor(KeyEvent.VK_LEFT, row, col);
            if (parent.mode == Mode.CHEAT) parent.board.removeLetterAt(row, col);
            else if (parent.mode == Mode.GAME){
                if (b.getText().length() == 1 && !b.getText().equals("*")){
                    for (JTextField t : parent.hand){
                        if (t.getText().equals("")){
                            if (b.getForeground() == Color.RED) t.setText("_");
                            else t.setText(b.getText());
                            break;
                        }
                    }
                }
            }
            if (parent.board.getBonusAt(row, col) != "" ){
                b.setText(parent.board.getBonusAt(row, col)+" ");
            }
            else if (row == col && (2*row)+1 == parent.board.getDimension()){
                b.setText("* "); // Pad space to handle when backspace is released.
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Handled best in keyTyped.
    }

    /**
     * Called by the KeyListeners to move the cursor when letters or keys are
     * pressed or typed into the board.
     *
     * @param direction The VK corresponding to the direction (KeyEvent.VK_LEFT, etc).
     * @param row The current row we are at.
     * @param col The current column we are at.
     */
    private void moveCursor( int direction, int row, int col){
        if (parent.mode == Mode.GAME){ // Handle passing over tiles that aren't enabled.
            if (direction == KeyEvent.VK_RIGHT && parent.board.get(row, col+1) != '_' && parent.board.get(row, col+1) != '#'){
                moveCursor(KeyEvent.VK_RIGHT, row, col+1);
                return;
            }
            else if (direction == KeyEvent.VK_LEFT && parent.board.get(row, col-1) != '_' && parent.board.get(row, col-1) != '#'){
                moveCursor(KeyEvent.VK_LEFT, row, col-1);
                return;
            }
            else if (direction == KeyEvent.VK_UP && parent.board.get(row-1, col) != '_' && parent.board.get(row-1, col) != '#'){
                moveCursor(KeyEvent.VK_UP, row-1, col);
                return;
            }
            else if (direction == KeyEvent.VK_DOWN && parent.board.get(row+1, col) != '_' && parent.board.get(row+1, col) != '#'){
                moveCursor(KeyEvent.VK_DOWN, row+1, col);
                return;
            }
        }
        if (direction == KeyEvent.VK_RIGHT){
            if (col == parent.board.getDimension()-1) parent.menuBar.requestFocusInWindow();
            else parent.virtualBoard.get((row*parent.board.getDimension())+col+1).requestFocusInWindow();
        }
        else if (direction == KeyEvent.VK_LEFT){
            if (col == 0) parent.menuBar.requestFocusInWindow();
            else parent.virtualBoard.get((row*parent.board.getDimension())+col-1).requestFocusInWindow();
        }
        else if (direction == KeyEvent.VK_UP){
            if (row == 0) parent.menuBar.requestFocusInWindow();
            else parent.virtualBoard.get(((row-1)*parent.board.getDimension())+col).requestFocusInWindow();
        }
        else if (direction == KeyEvent.VK_DOWN){
            if (row == parent.board.getDimension()-1) parent.menuBar.requestFocusInWindow();
            else parent.virtualBoard.get(((row+1)*parent.board.getDimension())+col).requestFocusInWindow();
        }
    }
}
