package Game;

import Player.PlayerMove;
import Util.Coordinate;
import Util.Dictionary;
import Util.GADDAG;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/8/13
 * Time: 6:18 PM
 * This object's sole purpose is for determining the best move available for a given
 * board and hand.  It simply finds all of the available moves and then chooses the one
 * with the highest score associated with it.
 */
public class BestMoveMachine {
    private Dictionary dictionary;
    private Validator validator;
    private Board board;
    private ArrayList<Character> hand;
    private ArrayList<PlayerMove> plays;
    private boolean workingHorizontally;
    private Coordinate currentAnchor;

    /**
     * Constructs the machine with the given parameters.
     *
     * @param dictionary The dictionary.
     * @param validator A validator to check if each move generated is valid.
     * @param board The current board.
     * @param hand The hand to work with.
     */
    public BestMoveMachine(Dictionary dictionary, Validator validator, Board board, ArrayList<Character> hand){
        this.dictionary = dictionary;
        this.validator = validator;
        this.board = board;
        this.hand = hand;
        plays = new ArrayList<PlayerMove>();
        workingHorizontally = true;
    }

    /**
     * Returns the highest scoring move available.
     *
     * @return The highest scoring move.
     */
    public PlayerMove getHighestScoringMove(){
        generateMoves();
        PlayerMove best = null;
        if (!plays.isEmpty()){
            best = plays.get(0);
            for (PlayerMove move : plays) if (board.calcScore(move) > board.calcScore(best)) best = move;

        }
        System.out.println(best);
        return best;
    }

    /**
     * Populates the plays list with every possible valid move..
     */
    private void generateMoves(){
        String handString = "";
        for (Character s : hand) handString += s;
        getAllMovesInCurrentDirection(handString);
        workingHorizontally = false;
        getAllMovesInCurrentDirection(handString);
    }

    /**
     * Gathers all possible moves currently.
     *
     * @return A list of every possible valid move.
     */
    public ArrayList<PlayerMove> getAllMoves(){
        generateMoves();
        return plays;
    }

    /**
     * Finds all valid moves going in the direction stated in workingHorizontally.
     * It then adds these moves to the plays list, thus no return value is needed.
     *
     * @param handString The hand we are working with in string form.
     */
    private void getAllMovesInCurrentDirection(String handString){
        ArrayList<Coordinate> anchors = generateAnchorSquares();
        for (Coordinate c : anchors){
            currentAnchor = c;
            gen(0, new Word(), handString, dictionary.theTree());
        }
        ArrayList<PlayerMove> toRemove = new ArrayList<PlayerMove>();
        for (PlayerMove move : plays){
            if (!validator.validate(move, board)) toRemove.add(move);
        }
        for (PlayerMove move : toRemove) plays.remove(move);
    }

    /**
     * Generates a list of all of the anchor squares needed when working in the
     * current direction.  Anchor squares consist of squares containing the first
     * letter of all the words in the given direction as well as any squares
     * directly above or below an occupied square for horizontal and directly
     * right or left of an occupied square for vertical.  These are the squares
     * that the GADDAG algorithm can base its search out from.
     *
     * @return A list of all of the anchor squares.
     */
    private ArrayList<Coordinate> generateAnchorSquares(){
        ArrayList<Coordinate> result = new ArrayList<Coordinate>();
//        for (int i=0; i<board.getDimension(); i++){
//            for (int j=0; j<board.getDimension(); j++){
//                if (hasNeighbor(i, j)) result.add(new Coordinate(i, j));
//            }
//        }
        for (int i=0; i<board.getDimension(); i++){
            boolean foundWord = false;
            for (int j=0; j<board.getDimension(); j++){
                if (workingHorizontally && board.get(i, j) == '_'){
                    foundWord = false;
                    if ((!board.getHorizontalWordAt(i-1, j).equals("")
                            || !board.getHorizontalWordAt(i+1, j).equals("")) && board.get(i, j) == '_'){
                        result.add(new Coordinate(i, j));
                    }
                }
                else if (workingHorizontally && !foundWord){
                    foundWord = true;
                    result.add(new Coordinate(i, j));
                }
                else if (!workingHorizontally && board.get(j, i) == '_'){
                    foundWord = false;
                    if ((!board.getVerticalWordAt(j, i-1).equals("")
                            || !board.getVerticalWordAt(j, i+1).equals("")) && board.get(j, i) == '_'){
                        result.add(new Coordinate(j, i));
                    }
                }
                else if (!workingHorizontally && !foundWord){
                    foundWord = true;
                    result.add(new Coordinate(j, i));
                }
            }
        }

        if (board.isEmpty()){
            result.add(new Coordinate(board.getDimension()/2, board.getDimension()/2));
        }
        return result;
    }

    /**
     * Tells if the given spot has an adjacent tile.  Useful for generating anchors.
     *
     * @param row The row.
     * @param col The column.
     * @return True if the spot has a neighboring tile.
     */
    private boolean hasNeighbor(int row, int col){
        if (board.get(row-1, col) != '_' && board.get(row-1, col) != '#') return true;
        if (board.get(row+1, col) != '_' && board.get(row+1, col) != '#') return true;
        if (board.get(row, col-1) != '_' && board.get(row, col-1) != '#') return true;
        if (board.get(row, col+1) != '_' && board.get(row, col+1) != '#') return true;
        return false;
    }

    /**
     * Returns the letter at the given spot depending on what the current anchor
     * square is.
     *
     * @param pos The distance away from the anchor square (can be <0).
     * @return The character at the given spot according to the board.
     */
    private Character getLetterOnSpot(int pos){
        if (workingHorizontally) return board.get(currentAnchor.getRow(), currentAnchor.getColumn()+pos);
        else return board.get(currentAnchor.getRow()+pos, currentAnchor.getColumn());
    }

    /**
     * Tells if the letter at the given spot is a blank.
     *
     * @param pos The distance away from the anchor square (can be <0).
     * @return The character at the given spot according to the board.
     */
    private boolean letterOnSpotIsBlank(int pos){
        if (workingHorizontally) return board.isBlank(currentAnchor.getRow(), currentAnchor.getColumn()+pos);
        else return board.isBlank(currentAnchor.getRow()+pos, currentAnchor.getColumn());
    }

    /**
     * Generate all possible moves.  This was borrowed from the pseudo-code
     * for the GADDAG algorithm by Steven A. Gordon.  It utilizes the properties
     * of the GADDAG as well as mutual recursion in order to generate all results.
     *
     * @param pos The current offset from the anchor square.
     * @param word The current word that has been built.
     * @param rack The current hand.
     * @param arc The current arc in the GADDAG.
     */
    private void gen(int pos, Word word, String rack, GADDAG arc){
        if (getLetterOnSpot(pos) != '_' && getLetterOnSpot(pos) != '#'){
            //if (arc.containsTree(getLetterOnSpot(pos)))
            goOn(pos, getLetterOnSpot(pos), new Word(word), rack, arc.getTree(getLetterOnSpot(pos)), arc, false, letterOnSpotIsBlank(pos));
        }
        else if (!rack.equals("")){
            for (Character l : rack.toCharArray()){
                if (l != '_'){
                    if (arc.containsTree(l) && allowedOn(pos, l)) goOn(pos, l, new Word(word), rack.replaceFirst(l+"", ""), arc.getTree(l), arc, true, false);
                }
                else{
                    for (int i=65; i<91; i++){
                        if (arc.containsTree((char)i) && allowedOn(pos, (char)i)) goOn(pos, (char)i, new Word(word), rack.replaceFirst("_", ""), arc.getTree((char)i), arc, true, true);
                    }
                }
            }
//            if (rack.contains("_")){
//                for (int i=65; i<91; i++){
//                    if (arc.containsTree((char)i) && allowedOn(pos, (char)i)) goOn(pos, (char)i, word, rack.replaceFirst("_", ""), arc.getTree((char)i), arc, true);
//                }
//            }
        }
    }

    /**
     * Part of the GADDAG algorithm.  This section appends/prepends the given
     * character to the word and then records the move if the state in the
     * oldArc is empty.
     *
     * @param pos The current offset from the anchor square.
     * @param l The new letter to add.
     * @param word The current word before adding the letter.
     * @param rack The current hand.
     * @param newArc The arc we are working with now.
     * @param oldArc The parent arc.
     * @param newLetter True if the letter being added was from the hand.
     */
    private void goOn(int pos, Character l, Word word, String rack, GADDAG newArc, GADDAG oldArc, boolean newLetter, boolean blank){
        if (pos <= 0){
            word.prepend(l+"", newLetter, blank);
            if (isOn(oldArc, l) && (getLetterOnSpot(pos-1) == '_' || getLetterOnSpot(pos-1) == '#')){
                if (!word.getNewLetters().isEmpty()) recordPlay(word);
            }
            if (newArc != null){
                if (getLetterOnSpot(pos-1) == '_') gen(pos-1, new Word(word), rack, newArc);
                newArc = newArc.getTree('#');
                if (newArc != null && getLetterOnSpot(pos-1) == '_' && roomToTheRight()){
                    gen(1, new Word(word), rack, newArc);
                }
            }
        }
        else{
            word.append(l + "", newLetter, blank);
            if (isOn(oldArc, l) && (getLetterOnSpot(pos+1) == '_' || getLetterOnSpot(pos+1) == '#')){
                if (!word.getNewLetters().isEmpty()) recordPlay(word);
            }
            if (newArc != null && roomToTheRight()){
                gen(pos+1, new Word(word), rack, newArc);
            }
        }
    }

    /**
     * This is useful in pruning the invalid cross-sets of a given tile
     * placement.
     *
     * @param pos The current offset from the anchor square.
     * @param l The letter to be placed.
     * @return True if it does not create any invalid words in the cross-set.
     */
    public boolean allowedOn(int pos, Character l){
        String above;
        String below;
        if (workingHorizontally){
            above = board.getVerticalWordAt(currentAnchor.getRow()-1, currentAnchor.getColumn()+pos);
            below = board.getVerticalWordAt(currentAnchor.getRow()+1, currentAnchor.getColumn()+pos);
        }
        else{
            above = board.getHorizontalWordAt(currentAnchor.getRow()+pos, currentAnchor.getColumn()-1);
            below = board.getHorizontalWordAt(currentAnchor.getRow()+pos, currentAnchor.getColumn()+1);
        }
        return dictionary.contains(above+l+below);
    }

    /**
     * Tells if the old arc contains the given letter and if that branch is terminal.
     *
     * @param oldArc The arc to check.
     * @param l The letter to check for
     * @return True if the tree contains the letter and that branch is terminal.
     */
    public boolean isOn(GADDAG oldArc, Character l){
        if (!oldArc.containsTree(l)) return false;
        if (oldArc.getTree(l).isTerminal()) return true;
        else if (oldArc.getTree(l).containsTree('#')) if (oldArc.getTree(l).getTree('#').isTerminal()) return true;
        return false;
    }

    /**
     * Determines if there is room to the right anywhere in the given row.
     * If we are working vertically, it checks for room below.
     *
     * @return True if there is an empty square.
     */
    private boolean roomToTheRight(){
        if (workingHorizontally) return board.get(currentAnchor.getRow(), board.getDimension()-1) == '_';
        else return board.get(board.getDimension()-1, currentAnchor.getColumn()) == '_';
    }

    /**
     * Adds this play to the list of plays, first determining what the play
     * actually is.
     *
     * @param word The word to be played.
     */
    private void recordPlay(Word word){
        Coordinate start = currentAnchor;
        if (word.getNewLetters().contains(0)){
            if (workingHorizontally){
                start = new Coordinate(start.getRow(), start.getColumn()-word.getFirst());
            }
            else{
                start = new Coordinate(start.getRow()-word.getFirst(), start.getColumn());
            }
        }
        ArrayList<Integer> newLetters = word.getNewLetters();
        plays.add(new PlayerMove(workingHorizontally, word.getWord(), start, newLetters, word.getBlanks()));
    }

    /**
     * This class is used to assist with determining what a given move actually
     * was when the word is passed on to recordPlay().
     */
    private class Word {
        private String word;
        private HashSet<Integer> newLetters;
        private int first; // Index of first letter added.
        private ArrayList<Integer> blanks;

        /**
         * Copies the values from another Word.
         *
         * @param word The word to copy.
         */
        public Word(Word word){
            this.word = word.getWord();
            newLetters = new HashSet<Integer>();
            newLetters.addAll(word.getNewLetters());
            first = word.getFirst();
            blanks = new ArrayList<Integer>();
            blanks.addAll(word.getBlanks());
        }

        /**
         * Starts a new Word with blank values
         */
        public Word(){
            word = "";
            newLetters = new HashSet<Integer>();
            blanks = new ArrayList<Integer>();
            first = -1;
        }

        /**
         * Appends the given string onto the current word.  Adjusts values
         * accordingly.
         *
         * @param str The string to append.
         * @param newLetter If the string is new (from the hand).
         * @param blank If the string is blank.
         */
        public void append(String str, boolean newLetter, boolean blank){
            if (newLetter){
                for (int i=0; i<str.length(); i++){
                    newLetters.add(i+word.length());
                }
            }
            if (blank){
                for (int i=0; i<str.length(); i++){
                    blanks.add(i+word.length());
                }
            }
            word += str;
        }

        /**
         * Prepends the given string onto the current word.  Adjusts values
         * accordingly.
         *
         * @param str The string to prepend.
         * @param newLetter If the string is new (from the hand).
         * @param blank If the string is blank
         */
        public void prepend(String str, boolean newLetter, boolean blank){
            ArrayList<Integer> movers = new ArrayList<Integer>();
            for (Integer i : newLetters) movers.add(i);
            newLetters.clear();
            for (Integer i : movers) newLetters.add(i+str.length());
            if (newLetter){
                for (int i=0; i<str.length(); i++) newLetters.add(i);
            }
            movers.clear();
            for (Integer i : blanks) movers.add(i);
            blanks.clear();
            for (Integer i : movers) blanks.add(i+str.length());
            if (blank){
                for (int i=0; i<str.length(); i++) blanks.add(i);
            }
            first += str.length();
            word = str+word;
        }

        /**
         * A list of the indexes in the word where letters were from the hand.
         *
         * @return A list of letters from the hand.
         */
        public ArrayList<Integer> getNewLetters(){
            ArrayList<Integer> result = new ArrayList<Integer>();
            result.addAll(newLetters);
            return result;
        }

        /**
         * A list of the indexes in the word where blanks were used.
         *
         * @return A list of indexes where blanks were used in the word.
         */
        public ArrayList<Integer> getBlanks(){
            ArrayList<Integer> result = new ArrayList<Integer>();
            result.addAll(blanks);
            return result;
        }

        public String getWord(){return word;}

        /**
         * The index of the first letter added, which is always the anchor
         * square if it contains a letter.
         *
         * @return The index of the first letter added.
         */
        public int getFirst(){return first;}

    }
}
