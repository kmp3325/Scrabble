package Util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/10/13
 * Time: 5:24 PM
 * A class for saving a player object, keeps track of most basic data.
 */
public class SavedPlayer implements Serializable {
    private static final long serialVersionUID = 40L;
    public String type;
    public int id;
    public ArrayList<Character> hand;
    public int score;}
