package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DictionaryActionListener implements ActionListener {

    private MainFrame parent;

    protected DictionaryActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new DictionaryWindow(parent);
    }
}
