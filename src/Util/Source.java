package Util;

import Player.PlayerMove;
import Player.Player;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/7/13
 * Time: 10:45 PM
 * A source for the Human Player to get a move from.  Until a move is made, moveChoice stays null.
 */
public interface Source {
    /**
     * Simply gets the choice of move from this source.  Forces the source to
     * have some sort of way of determining what the user selected for a move.
     *
     * @return The move choice.
     */
    public PlayerMove moveChoice();

    /**
     * Changes the current player.
     *
     * @param current The new current player.
     */
    public void changeCurrentPlayer(Player current);

    /**
     * Lets the source know that the game is done and who the winner is.
     *
     * @param winner The winner of the game.
     */
    public void setWinner(Player winner);

    /**
     * Return true if the source has canceled the game.
     */
    public boolean canceled();

}
