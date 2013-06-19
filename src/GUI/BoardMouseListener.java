package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 9:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class BoardMouseListener implements MouseListener {

    private MainFrame parent;
    protected static JTextField spotToPlaceLetter = null;

    protected BoardMouseListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        spotToPlaceLetter = (JTextField)e.getSource();
        if (HandMouseListener.letterToRemoveFromHand != null){
            String temp = "";
            String toSet;
            if (spotToPlaceLetter.getText().length() == 0 ||
                    spotToPlaceLetter.getText().length() == 2 ||
                    spotToPlaceLetter.getText().equals("*")){
                temp = "";
                toSet = HandMouseListener.letterToRemoveFromHand.getText();
            }
            else if (spotToPlaceLetter.isEnabled() && spotToPlaceLetter.getText().length() == 1
                    && spotToPlaceLetter.getForeground() == Color.BLACK){
                temp = spotToPlaceLetter.getText();
                toSet = HandMouseListener.letterToRemoveFromHand.getText();
            }
            else if (spotToPlaceLetter.isEnabled() && spotToPlaceLetter.getForeground() == Color.RED){
                temp = "_";
                toSet = HandMouseListener.letterToRemoveFromHand.getText();
                spotToPlaceLetter.setForeground(Color.BLACK);
            }
            else{ // Essentially do nothing with temp and toSet
                temp = HandMouseListener.letterToRemoveFromHand.getText();
                toSet = spotToPlaceLetter.getText();
            }
            if (toSet.equals("_")){
                new LetterChooser(parent, spotToPlaceLetter);
                // Make sure that when we set it later it doesn't actually change.
                spotToPlaceLetter.setForeground(Color.RED);
                toSet = spotToPlaceLetter.getText();
            }
            if (spotToPlaceLetter.isEnabled() && spotToPlaceLetter.getText().length() == 1
                    && HandMouseListener.letterToRemoveFromHand.getText().equals("")){
                int row = parent.virtualBoard.indexOf(spotToPlaceLetter)/parent.board.getDimension();
                int col = parent.virtualBoard.indexOf(spotToPlaceLetter)%parent.board.getDimension();
                if (row == col && row == parent.board.getDimension()/2){
                    toSet = "*";
                    spotToPlaceLetter.setForeground(Color.BLACK);
                }
                if (!parent.board.getBonusAt(row, col).equals("")){
                    toSet = parent.board.getBonusAt(row, col);
                    spotToPlaceLetter.setForeground(Color.BLUE);
                }
            }
            spotToPlaceLetter.setText(toSet);
            HandMouseListener.letterToRemoveFromHand.setText(temp);
        }

        HandMouseListener.letterToRemoveFromHand = null;
        spotToPlaceLetter = null;
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
