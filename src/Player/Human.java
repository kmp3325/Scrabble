package Player;

import Game.Bag;
import Game.Board;
import Util.Coordinate;
import Util.Dictionary;
import Util.Source;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/5/13
 * Time: 5:34 PM
 * The Human player, this class takes input from the user.
 */
public class Human extends Player {

    Source source;

    /**
     * Initializes the player with a score of 0 and an empty hand.
     *
     * @param source Where the user will provide input from.
     * @param id The id of the player: 1, 2, 3, etc...
     * @param board The board that the player will be referencing.
     * @param bag The current bag of tiles to be working with.
     * @param dictionary The current dictionary that is being used.
     */
    public Human(Source source, int id, Board board, Bag bag, Dictionary dictionary) {
        super(id, board, bag, dictionary);
        this.source = source;
    }

    public boolean makeMove(){
        // This allows the GUI to remove moveChoice from last player to make move.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            return false;
            // Do nothing since this can happen by selecting end game.
        }
        // Would like something with concurrency here, but this might do...
        while (source.moveChoice() == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return false;
                // Do nothing since this can happen by selecting end game.
            }
        }
        if (source.moveChoice().getStartCoordinate().equals(new Coordinate(-1, -1))){
            char[] cs = source.moveChoice().getWord().toCharArray();
            ArrayList<Character> toTrade = new ArrayList<Character>();
            for (char c : cs) toTrade.add(c);
            trade(toTrade);
        }
        else{
            score += board.calcScore(source.moveChoice());
            placeMove(source.moveChoice());
            populateHand();
        }
        return true;
    }
}
