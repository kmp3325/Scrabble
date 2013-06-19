package GUI;

import Game.Board;
import Player.PlayerMove;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/17/13
 * Time: 9:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class MoveChooser extends JDialog {
    private ArrayList<PlayerMove> moves;
    private JList<String> movesList;
    private JTextField display;
    private Board board;

    protected MoveChooser(MainFrame parent, ArrayList<PlayerMove> movesPassed){
        super(parent, true);
        board = parent.board;
        moves = new ArrayList<PlayerMove>();

        // Sort the moves and get rid of duplicates by using a TreeSet.
        // Duplicates will exist because of multiple instances of the same
        // letter being used to represent the same move many different times.
        TreeSet<PlayerMove> movesSet = new TreeSet<PlayerMove>(new ValueComparator());
        movesSet.addAll(movesPassed);
        moves.addAll(movesSet);

        JPanel main = new JPanel(new BorderLayout());
        display = new JTextField(displayer(moves.get(0)));
        display.setEditable(false);
        main.add(display, BorderLayout.NORTH);

        DefaultListModel<String> movesModel = new DefaultListModel<String>();
        movesList = new JList<String>(movesModel);
        JScrollPane movesScroll = new JScrollPane(movesList);
        for (PlayerMove move : moves) movesModel.addElement(displayer(move));
        main.add(movesScroll, BorderLayout.CENTER);

        JPanel south = new JPanel(new GridLayout(1, 2));
        JButton play = new JButton("Place move");
        JButton cancel = new JButton("Cancel");
        south.add(cancel);
        south.add(play);
        main.add(south, BorderLayout.SOUTH);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MoveChooser.this.board.placeMove(moves.get(movesList.getSelectedIndex()));
                dispose();
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        movesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                display.setText(movesList.getSelectedValue());
            }
        });

        add(main);
        setBounds(new Rectangle(0, 0, 430, 230));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Move List");
        setVisible(true);
    }

    private String displayer(PlayerMove move){
        String h;
        if (move.isHorizontal()) h = "horizontally";
        else h = "vertically";
        return move.getWord()+": "+h+" starting at "+move.getStartCoordinate()+" for "+board.calcScore(move)+" points";
    }

    private class ValueComparator implements Comparator<PlayerMove>{

        @Override
        public int compare(PlayerMove o1, PlayerMove o2) {
            if (board.calcScore(o1) == board.calcScore(o2)){
                return o1.compareTo(o2);
            }
            else return board.calcScore(o2) - board.calcScore(o1);
        }
    }
}
