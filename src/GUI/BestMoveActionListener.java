package GUI;

import Game.BestMoveMachine;
import Player.PlayerMove;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class BestMoveActionListener implements ActionListener {

    private MainFrame parent;

    protected BestMoveActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (parent.confirmation("Best Move", "This will generate the best possible move" +
                " given this hand \nand the current board and it will place it on the board.")){
            parent.board.generateWordLists();
            for (int i=0; i<parent.board.getDimension(); i++){
                for (int j=0; j<parent.board.getDimension(); j++){
                    if (parent.virtualBoard.get(i*parent.board.getDimension()+j).getForeground() == Color.RED){
                        parent.board.setBlank(i, j);
                    }
                }
            }
            ArrayList<Character> theHand = new ArrayList<Character>();
            for (int i=0;i<parent.handSize;i++){
                if (!parent.hand.get(i).getText().equals("")) theHand.add(parent.hand.get(i).getText().charAt(0));
            }
            ArrayList<PlayerMove> moves = new BestMoveMachine(parent.dictionary,
                    parent.validator, parent.board, theHand).getAllMoves();
            if (moves.isEmpty()){
                JOptionPane.showMessageDialog(parent, "There are no moves available.");
                return;
            }
            new MoveChooser(parent, moves);
        }
    }
}
