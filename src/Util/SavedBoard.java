package Util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/10/13
 * Time: 3:28 PM
 * Simply holds data for a saved board.
 */
public class SavedBoard implements Serializable {
    private static final long serialVersionUID = 41L;
    public ArrayList<ArrayList<Character>> board;
    public ArrayList<ArrayList<Boolean>> blanks;
    public ArrayList<Character> hand;}
