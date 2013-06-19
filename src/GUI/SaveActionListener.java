package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/17/13
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaveActionListener implements ActionListener {

    private MainFrame parent;

    protected SaveActionListener(MainFrame parent){
        super();
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (parent.currentFile == null){
            new SaveAsActionListener(parent);
        }
        else{
            parent.saveFile(parent.currentFile);
        }
    }
}
