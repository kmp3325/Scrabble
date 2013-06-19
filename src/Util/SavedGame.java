package Util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/10/13
 * Time: 3:33 PM
 * Simply holds data for a saved game.
 */
public class SavedGame implements Serializable {
    private static final long serialVersionUID = 41L;
    public ArrayList<SavedPlayer> players;
    public ArrayList<ArrayList<Character>> board;
    public ArrayList<ArrayList<Boolean>> blanks;
    public int whoseTurn;
    public ArrayList<Character> baggage;}
