package Player;

import Game.Bag;
import Game.BestMoveMachine;
import Game.Board;
import Util.Dictionary;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/11/13
 * Time: 4:51 PM
 * Chooses the worst possible move available.
 */
public class TerribleComputer extends Player{

    private Dictionary dictionary;

    /**
     * Initializes the player with a score of 0 and an empty hand.
     *
     * @param id         The id of the player: 1, 2, 3, etc...
     * @param board      The board that the player will be referencing.
     * @param bag        The current bag of tiles to be working with.
     * @param dictionary The current dictionary that is being used.
     */
    public TerribleComputer(int id, Board board, Bag bag, Dictionary dictionary) {
        super(id, board, bag, dictionary);
        this.dictionary = dictionary;
    }

    @Override
    public boolean makeMove() {
        BestMoveMachine thinker = new BestMoveMachine(dictionary, validator, board, hand);
        ArrayList<PlayerMove> moves = thinker.getAllMoves();
        if (moves.size() == 0){ // Trade.
            trade(new ArrayList<Character>(hand.get(0)));
        }
        else{
            PlayerMove move = moves.get(0);
            for (PlayerMove m : moves){
                if (board.calcScore(m) < board.calcScore(move)) move = m;
            }
            score += board.calcScore(move);
            placeMove(move);
            populateHand();
        }
        try { // Gives the GUI a second to catch up!
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

}
