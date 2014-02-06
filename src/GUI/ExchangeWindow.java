package GUI;

import Game.Board;
import Player.PlayerMove;
import Util.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/17/13
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExchangeWindow extends JDialog {
    private ArrayList<JCheckBox> checks;
    private MainFrame parent;

    protected ExchangeWindow(MainFrame parent){
        super(parent, true);
        this.parent = parent;
        for (int i=0; i<parent.board.getDimension(); i++){
            for (int j=0; j<parent.board.getDimension(); j++){
                JTextField virtual = parent.virtualBoard.get(i*parent.board.getDimension()+j);
                if (parent.board.get(i, j) == '_' && virtual.getText().length() == 1 && !virtual.getText().equals("*")){
                    for (JTextField tile : parent.hand){
                        if (tile.getText().equals("")){
                            if (virtual.getForeground() == Color.RED) tile.setText("_");
                            else tile.setText(virtual.getText());
                            virtual.setForeground(Color.BLACK);
                            virtual.setText("");
                            break;
                        }
                    }
                }
            }
        }
        setTitle("Exchange");
        setBounds(new Rectangle(0, 0, 160, 330));

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(new JLabel(parent.bagNumber.getText()), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(parent.handSize, 1));
        checks = new ArrayList<JCheckBox>();
        for (int i=0; i<parent.handSize; i++){
            JCheckBox check = new JCheckBox(parent.hand.get(i).getText());
            checks.add(check);
            center.add(check);
        }
        mainPanel.add(center, BorderLayout.CENTER);


        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String checked = "";
                for (JCheckBox check : checks){
                    if (check.isSelected()) checked += check.getText();
                }
                if (checked.equals("")) dispose();
                else{
                    ExchangeWindow.this.parent.setMoveChoice(new PlayerMove(false, checked, new Coordinate(-1, -1), new ArrayList<Integer>(), new ArrayList<Integer>()));
                    dispose();
                }
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel south = new JPanel(new GridLayout(1, 2));
        south.add(cancel);
        south.add(ok);
        mainPanel.add(south, BorderLayout.SOUTH);

        add(mainPanel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
