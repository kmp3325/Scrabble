package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class EndActionListener implements ActionListener {

    private MainFrame parent;

    protected EndActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (parent.confirmation("End Game", "This will end the current game.")){
            if (parent.game != null) parent.game.cancel(true);
            parent.game = null;
            parent.setOptionsForGame(false);
        }
    }
}
