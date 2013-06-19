package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClearBoardActionListener implements ActionListener {

    private MainFrame parent;

    protected ClearBoardActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (parent.confirmation("Clear Board", "This will completely wipe the board")){
            parent.board.clear();
        }
    }
}
