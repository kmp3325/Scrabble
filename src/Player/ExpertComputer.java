package Player;

import Game.Bag;
import Game.BestMoveMachine;
import Game.Board;
import Util.Dictionary;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/4/13
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpertComputer extends Player {

    private Dictionary dictionary;


    /**
     * Initializes the player with a score of 0 and an empty hand.
     *
     * @param id The id of the player: 1, 2, 3, etc...
     * @param board The board that the player will be referencing.
     * @param bag The current bag of tiles to be working with.
     * @param dictionary The current dictionary that is being used.
     */
    public ExpertComputer(int id, Board board, Bag bag, Dictionary dictionary){
        super(id, board, bag, dictionary);
        this.dictionary = dictionary;
    }

    @Override
    public boolean makeMove(){
        BestMoveMachine thinker = new BestMoveMachine(dictionary, validator, board, hand);
        PlayerMove move = thinker.getHighestScoringMove();
        if (move == null){ // Trade.
            trade(new ArrayList<Character>(hand.get(0)));
        }
        else{
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
