package GUI;

import Util.Dictionary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/17/13
 * Time: 9:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class DictionaryWindow extends JDialog{

    protected DictionaryWindow(MainFrame parent){
        super(parent, true);
        setTitle("Dictionary");
        setBounds(new Rectangle(0, 0, 330, 330));

        JPanel mainPanel = new JPanel(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<String>();
        JList<String> list = new JList<String>(listModel);
        JScrollPane scroll = new JScrollPane(list);
        for (String word : parent.dictionary.getSortedDictionary()) listModel.addElement(word);

        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        mainPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(ok, BorderLayout.SOUTH);

        add(mainPanel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
