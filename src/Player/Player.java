package Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import Game.GameMediator;
import Game.Validator;
import Game.Board;
import Game.Bag;
import Util.Dictionary;
import Util.SavedPlayer;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/4/13
 * Time: 4:10 PM
 * The abstraction of a Player Object that must be followed by all players.
 */
public abstract class Player implements Serializable {

    protected Board board;
    protected ArrayList<Character> hand;
    protected int score;
    protected Validator validator;
    public static int HAND_SIZE = 7;
    protected Bag bag;
    protected int id;

    /**
     * Initializes the player with a score of 0 and an empty hand.
     *
     * @param id The id of the player: 1, 2, 3, etc...
     * @param board The board that the player will be referencing.
     * @param bag The current bag of tiles to be working with.
     * @param dictionary The current dictionary that is being used.
     */
    public Player(int id, Board board, Bag bag, Dictionary dictionary){
        this.board = board;
        this.bag = bag;
        this.id = id;

        validator = new Validator(dictionary);
        score = 0;
        hand = new ArrayList<Character>();

        try {
            Scanner scanner = new Scanner(new File(GameMediator.configFile));
            scanner.nextLine();
            int HAND_SIZE = Integer.parseInt(scanner.nextLine());

            if (HAND_SIZE > 0) this.HAND_SIZE = HAND_SIZE;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * First selects the move to be made, checks for validity, and then updates
     * the board and player score based on the chosen move.
     *
     * @return True if the move being made was valid and placed (or if a trade
     * was made successfully), false otherwise.
     */
    public abstract boolean makeMove();

    /**
     * Trades a given amount of tiles with the bag assuming the trade is valid.
     *
     * @param toTrade The list of tiles to trade.
     */
    protected void trade(ArrayList<Character> toTrade){
        for (Character c : toTrade) hand.remove(c);
        for (Character ignored : toTrade) hand.add(bag.getRandomTile());
        for (Character c : toTrade) bag.add(c);
    }

    public int getScore(){return score;}

    /**
     * Check to see if the player's hand is empty.
     *
     * @return True if the hand is empty.
     */
    public boolean emptyHand(){
        return (hand.size() == 0);
    }

    /**
     * Fills the players hand up until they reach the current HAND_SIZE or
     * until the bag is empty.
     */
    public void populateHand(){
       while (hand.size() < HAND_SIZE && !bag.isEmpty()){
           hand.add(bag.getRandomTile());
       }
    }

    /**
     * Returns a string representation of the player's hand.
     *
     * @return The hand in string form, ex: "a b c d".
     */
    public String printHand(){
        String result = "";
        for (Character c : hand) result += c+" ";
        result = result.substring(0, result.length()-1);
        return result;
    }

    public int getHandSizeConstant(){return HAND_SIZE;}

    public int getId(){return id;}

    /**
     * Get the hand as a string. "ABCD"
     *
     * @return The hand as a string.
     */
    public String getHand(){
        String result = "";
        for (Character c : hand) result += c;
        return result;
    }

    /**
     * Sets the values for id, hand, and score to a given saved player.
     *
     * @param p The player to set values to.
     */
    public void setValues(SavedPlayer p){
        id = p.id;
        score = p.score;
        hand = p.hand;
    }

    /**
     * Just places the move on the board and then removes tiles from hand.
     *
     * @param move The move to be placed.
     */
    protected void placeMove(PlayerMove move){
        board.placeMove(move);
        ArrayList<Character> toRemove = new ArrayList<Character>();
        for (int i : move.getNewLettersList()){
            if (!move.getBlanks().contains(i)) toRemove.add(move.getWord().charAt(i));
            else toRemove.add('_');
        }
        for (Character c : toRemove) hand.remove(c);
    }

}
