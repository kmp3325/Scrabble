package Player;

import Util.Coordinate;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/4/13
 * Time: 4:09 PM
 * The PlayerMove object keeps track of all of the data necessary to make a move.
 */
public class PlayerMove {

    private boolean horizontal; // True if the move is horizontal, false if vertical.
    private String word; // The full word that will be created.
    private Coordinate start; // The start location of the first letter.
    private ArrayList<Integer> newLetters; // Which letters in word are being placed by the player.
    private ArrayList<Integer> blanks; // Which letters in word are blank tiles.

    /**
     * Simple creation of the PlayerMove Object.  If Coordinate is (-1, -1) then
     * the move is a trade and word parameter are the characters to be traded.
     *
     * @param horizontal True if the word is horizontal, false otherwise.
     * @param word The full word that will be created by the move.
     * @param start The coordinate where the word begins on the board.
     * @param newLetters A list that details which letters belong to the player
     *                   making the move.  Each integer corresponds to a position
     *                   in the word that denotes a player's new tile being placed.
     */
    public PlayerMove(boolean horizontal, String word, Coordinate start, ArrayList<Integer> newLetters, ArrayList<Integer> blanks){
        this.horizontal = horizontal;
        this.word = word;
        this.start = start;
        this.newLetters = newLetters;
        this.blanks = blanks;
    }

    public boolean isHorizontal(){return horizontal;}

    public String getWord(){return word;}

    public ArrayList<Integer> getBlanks(){return blanks;}

    public Coordinate getStartCoordinate(){return start;}

    /**
     * Gets the size of the word.
     * @return The size of the word.
     */
    public int getSize(){return word.length();}

    public Coordinate getEndCoordinate(){
        Coordinate result;
        if (horizontal){
            result = new Coordinate(start.getRow(), start.getColumn()+word.length()-1);
        }
        else{
            result = new Coordinate(start.getRow()+word.length()-1, start.getColumn());
        }
        return result;
    }

    /**
     * Returns the character at position i in the word.
     *
     * @param i The position to get the character from.
     * @return The character at this position.
     */
    public char getLetterAt(int i){return word.charAt(i);}

    /**
     * Returns the list of integers corresponding to their position in the word.
     *
     * @return The list of integers corresponding to their position in the word.
     */
    public ArrayList<Integer> getNewLettersList(){return newLetters;}

    @Override
    public String toString(){
        return horizontal+" "+word+" "+start+" "+newLetters+blanks;
    }

    public int compareTo(PlayerMove move){
        if (word.equals(move.getWord())){
            if ((horizontal == move.isHorizontal())){
                if (start.equals(move.getStartCoordinate())){
                    return 0;
                }
                else return 1;
            }
            else return horizontal?1:-1;
        }
        else return word.compareTo(move.getWord());
    }

}
