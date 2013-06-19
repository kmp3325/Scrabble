package GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExchangeActionListener implements ActionListener {

    private MainFrame parent;

    protected ExchangeActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (parent.bag.isEmpty()) JOptionPane.showMessageDialog(parent, "The bag is empty, you cannot trade.",
                "Empty Bag", JOptionPane.PLAIN_MESSAGE);
        else if (parent.confirmation("Trade Tiles", "You will not be able to place a move" +
                " on the board if you trade.")){
            new ExchangeWindow(parent);
        }
    }
}
