package GUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/17/13
 * Time: 9:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayerSelectionWindow extends JDialog {

    private DefaultListModel<String> possibleList;
    private DefaultListModel<String> chosenList;
    private JList<String> possible;
    private JList<String> chosen;
    private ArrayList<String> players;

    protected PlayerSelectionWindow(MainFrame parent, ArrayList<String> players){
        super(parent, true);
        this.players = players;

        setTitle("Select Players");
        setBounds(new Rectangle(0, 0, 330, 330));

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        JPanel southPanel = new JPanel (new GridLayout(1, 2));

        possibleList = new DefaultListModel<String>();
        possibleList.addElement("Expert Computer");
        possibleList.addElement("Decent Computer");
        possibleList.addElement("Terrible Computer");
        possibleList.addElement("Random Computer");
        possibleList.addElement("Human");
        possible = new JList<String>(possibleList);
        possible.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){
                    if (possible.getSelectedIndex() != -1){
                        chosenList.addElement(possible.getSelectedValue());
                        possible.clearSelection();
                    }
                }
            }
        });

        chosenList = new DefaultListModel<String>();
        chosen = new JList<String>(chosenList);
        chosen.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && chosen.getSelectedIndex() != -1){
                    chosenList.remove(chosen.getSelectedIndex());
                    chosen.clearSelection();
                }
            }
        });

        possible.setBorder(BorderFactory.createTitledBorder("Possible Players"));
        centerPanel.add(possible);
        chosen.setBorder(BorderFactory.createTitledBorder("Chosen Players"));
        centerPanel.add(chosen);


        JButton cancel = new JButton("Cancel");
        JButton ok = new  JButton("OK");

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i=0;i<chosenList.size();i++){
                    PlayerSelectionWindow.this.players.add(chosenList.elementAt(i));
                }
                dispose();
            }
        });

        southPanel.add(cancel);
        southPanel.add(ok);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        add(mainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
