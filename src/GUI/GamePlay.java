package GUI;

import Util.SavedPlayer;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/17/13
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class GamePlay extends SwingWorker<Object, Object> {
    private MainFrame parent;
    private ArrayList<String> players;
    private ArrayList<SavedPlayer> savedPlayers;
    int whoseTurn;

    /**
     * Sets up for starting a game.
     *
     * @param players The players to start the game.
     */
    protected GamePlay(MainFrame parent, ArrayList<String> players){
        super();
        this.players = players;
        this.parent = parent;
    }

    /**
     * Sets up for loading a game.
     *
     * @param players The players that were playing
     * @param whoseTurn Whose turn it was.
     */
    public GamePlay(ArrayList<SavedPlayer> players, int whoseTurn){
        this.savedPlayers = players;
        this.whoseTurn = whoseTurn;
    }

    @Override
    protected Object doInBackground() throws Exception {
        if (savedPlayers == null) parent.mediator.play(parent, players, parent.board, parent.bag, parent.dictionary);
        else parent.mediator.load(parent, savedPlayers, parent.board, parent.bag, parent.dictionary, whoseTurn);
        return null;
    }
}
