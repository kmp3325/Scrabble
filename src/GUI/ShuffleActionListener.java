package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShuffleActionListener implements ActionListener {

    private MainFrame parent;

    protected ShuffleActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Collections.shuffle(parent.hand);
        BorderLayout layout = (BorderLayout) parent.southPanel.getLayout();
        if (layout.getLayoutComponent(BorderLayout.CENTER) != null){
            parent.southPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
        }
        JPanel southCenter = new JPanel(new GridLayout(1, parent.handSize));
        for (int i=0; i<parent.handSize; i++) southCenter.add(parent.hand.get(i));

        parent.southPanel.add(southCenter, BorderLayout.CENTER);
        parent.revalidate();
    }
}
