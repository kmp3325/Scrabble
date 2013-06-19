package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/17/13
 * Time: 10:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewActionListener implements ActionListener {

    private MainFrame parent;

    protected NewActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (parent.confirmation("New Game", "This will clear the board and start a new game.")){
            ArrayList<String> players = new ArrayList<String>();
            new PlayerSelectionWindow(parent, players);
            if (players.size() != 0){ // We didn't select cancel.
                if (parent.game != null) parent.game.cancel(true);
                parent.board.clear();
                parent.setOptionsForGame(true);
                parent.game = new GamePlay(parent, players);
                parent.game.execute();
            }
        }
    }
}
