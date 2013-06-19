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
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class RandomComputer extends Player{

    private Dictionary dictionary;
    private Random random;

    /**
     * Initializes the player with a score of 0 and an empty hand.
     *
     * @param id         The id of the player: 1, 2, 3, etc...
     * @param board      The board that the player will be referencing.
     * @param bag        The current bag of tiles to be working with.
     * @param dictionary The current dictionary that is being used.
     */
    public RandomComputer(int id, Board board, Bag bag, Dictionary dictionary) {
        super(id, board, bag, dictionary);
        this.dictionary = dictionary;
        random = new Random();
    }

    @Override
    public boolean makeMove() {
        BestMoveMachine thinker = new BestMoveMachine(dictionary, validator, board, hand);
        ArrayList<PlayerMove> moves = thinker.getAllMoves();
        if (moves.size() == 0){ // Trade.
            trade(new ArrayList<Character>(hand.get(0)));
        }
        else{
            PlayerMove move = moves.get(random.nextInt(moves.size()));
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
