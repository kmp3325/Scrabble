package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 10:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class LetterChooser extends JDialog {

    private MainFrame parent;
    private JTextField spot;

    protected LetterChooser(MainFrame parent, JTextField spot){
        super(parent, true);
        this.parent = parent;
        this.spot = spot;

        setTitle("Select a letter for the blank tile.");
        setBounds(new Rectangle(0, 0, 530, 100));

        JPanel mainPanel = new JPanel(new GridLayout(2, 13));
        for (int i=0; i<26; i++){
            JButton button = new JButton(((char)(i+65))+"");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LetterChooser.this.spot.setText(((JButton)e.getSource()).getText());
                    dispose();
                }
            });
            mainPanel.add(button);
        }

        add(mainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
