package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuitActionListener implements ActionListener {

    private MainFrame parent;

    protected QuitActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (parent.confirmation("QUIT PROGRAM", "This will quit the program!")){
            System.exit(0);
        }
    }
}
