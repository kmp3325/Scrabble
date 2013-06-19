package Game;

import Player.Player;
import Player.PlayerMove;
import Util.Coordinate;
import Util.IllegalConfigException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/4/13
 * Time: 4:09 PM
 * The board object.  This object not only keeps track of the letters on the
 * board, but it also has a way of maintaining a mapping of all of the coordinates
 * that contain words and which words they contain.  It always validates a move
 * before that move can be placed on the board so that the board should always
 * represent a valid configuration.
 */
public class Board extends Observable{

    private ArrayList<ArrayList<Character>> board;
    private HashMap<Coordinate, Bonus> bonuses;
    private Validator validator;
    private HashMap<Character, Integer> letterScores;
    private HashMap<Coordinate, String> horizontalWords;
    private HashMap<Coordinate, String> verticalWords;
    private boolean printMultipliers;
    private int fullHandBonus;
    private ArrayList<ArrayList<Boolean>> blanks;

    /**
     * All of the accepted Bonus values.
     * DL: Double Letter
     * TL: Triple Letter
     * DW: Double Word
     * TW: Triple Word
     */
    private enum Bonus{
        DL,
        TL,
        DW,
        TW;
    }

    /**
     * Initializes the board according to the configuration file and with the
     * given validator to represent the rules for placement.
     *
     * @param validator The validator to be used.
     * @throws Exception Any exceptions dealing with not finding files or
     * invalid files.
     */
    public Board(Validator validator) throws IllegalConfigException, FileNotFoundException{
        this(validator, false);
    }

    /**
     * Initializes the board according to the configuration file and with the
     * given validator to represent the rules for placement.
     *
     * @param validator The validator to be used to check if a move is ok.
     * @param printMultipliers True if the user wants the score multipliers to
     *                         be printed through the toString method when they
     *                         cover an empty space.  (Used for debugging, defaults to false)
     * @throws Exception Any exceptions dealing with not finding files or
     * invalid files.
     */
    public Board(Validator validator, boolean printMultipliers) throws IllegalConfigException, FileNotFoundException{
        board = new ArrayList<ArrayList<Character>>();
        blanks = new ArrayList<ArrayList<Boolean>>();
        bonuses = new HashMap<Coordinate, Bonus>();
        this.validator = validator;
        letterScores = new HashMap<Character, Integer>();
        horizontalWords = new HashMap<Coordinate, String>();
        verticalWords = new HashMap<Coordinate, String>();
        this.printMultipliers = printMultipliers;

        Scanner scanner = new Scanner(new File(GameMediator.configFile));

        String next = scanner.nextLine();

        for (int i=0;i<Integer.parseInt(next);i++){
            ArrayList<Character> temp = new ArrayList<Character>();
            ArrayList<Boolean> tempB = new ArrayList<Boolean>();
            for (int j=0;j<Integer.parseInt(next);j++){
                temp.add('_');
                tempB.add(false);
            }
            board.add(temp);
            blanks.add(tempB);
        }

        // Hand size...
        scanner.nextLine();

        // Full Hand Bonus!
        fullHandBonus = Integer.parseInt(scanner.nextLine());

        while (!(next = scanner.nextLine()).equals("--")){
            String[] s = next.split(" ");
            letterScores.put(s[0].charAt(0), Integer.parseInt(s[1]));
        }

        // Set bonuses.
        while (!(next = scanner.nextLine()).equals("--")){
            String[] s = next.split(" ");
            Bonus bonus;
            String b = s[2].toUpperCase();
            if (b.equals("2L") || b.equals("DL")) bonus = Bonus.DL;
            else if (b.equals("3L") || b.equals("TL")) bonus = Bonus.TL;
            else if (b.equals("2W") || b.equals("DW")) bonus = Bonus.DW;
            else if (b.equals("3W") || b.equals("TW")) bonus = Bonus.TW;
            else throw new IllegalConfigException();
            bonuses.put(new Coordinate(Integer.parseInt(s[0]),
                    Integer.parseInt(s[1])), bonus);
        }
    }

    /**
     * Clears the board for a new game.
     */
    public void clear(){
        for (int i=0; i<getDimension(); i++){
            for (int j=0; j<getDimension(); j++){
                board.get(i).set(j, '_');
                blanks.get(i).set(j, false);
            }
        }
        horizontalWords.clear();
        verticalWords.clear();
        setChanged();
        notifyObservers();
    }

    /**
     * Calculates the score for a given move to be made on the present board.
     * The move must be valid before calculating score or else the score may
     * produce strange results.
     *
     * @param move The move to be made.
     * @return The score that it will achieve.
     */
    public int calcScore(PlayerMove move){
        int result = 0;
        int main = 0;
        int multiplier = 1;

        for (int i=0;i<move.getSize();i++){
            int neighborScore = 0;
            Coordinate currentSpot;
            if (move.getNewLettersList().contains(i)){
                String neighbor;
                if (move.isHorizontal()){
                    int row = move.getStartCoordinate().getRow();
                    int startCol = move.getStartCoordinate().getColumn();
                    currentSpot = new Coordinate(row, i+startCol);

                    Coordinate spotAbove = new Coordinate(row-1, i+startCol);
                    Coordinate spotBelow = new Coordinate(row+1, i+startCol);
                    String aboveWord = getVerticalWordAt(spotAbove);
                    String belowWord = getVerticalWordAt(spotBelow);
                    if (move.getBlanks().contains(i)) neighbor = aboveWord+"_"+belowWord;
                    else neighbor = aboveWord+move.getLetterAt(i)+belowWord;
                    for (int b=0; b<aboveWord.length(); b++){ // Check above neighbor for blanks
                        if (blanks.get(row-aboveWord.length()+b).get(i+startCol))
                            neighbor = neighbor.replaceFirst(aboveWord.charAt(b)+"", "_");
                    }
                    for (int b=0; b<belowWord.length(); b++){ // Check below neighbor for blanks
                        if (belowWord.equals("Y")) System.out.println((row+b+1)+" "+(i+startCol));
                        if (blanks.get(row+b+1).get(i+startCol))
                            neighbor = neighbor.replaceFirst(belowWord.charAt(b)+"", "_");
                    }
                }
                else{
                    int col = move.getStartCoordinate().getColumn();
                    int startRow = move.getStartCoordinate().getRow();
                    currentSpot = new Coordinate(i+startRow, col);

                    Coordinate spotLeft = new Coordinate(i+startRow, col-1);
                    Coordinate spotRight = new Coordinate(i+startRow, col+1);
                    String leftWord = getHorizontalWordAt(spotLeft);
                    String rightWord = getHorizontalWordAt(spotRight);
                    if (move.getBlanks().contains(i)) neighbor = leftWord+"_"+rightWord;
                    else neighbor = leftWord+move.getLetterAt(i)+rightWord;
                    for (int b=0; b<leftWord.length(); b++){ // Check left neighbor for blanks
                        if (blanks.get(i+startRow).get(col-leftWord.length()+b))
                            neighbor = neighbor.replaceFirst(leftWord.charAt(b)+"", "_");
                    }
                    for (int b=0; b<rightWord.length(); b++){ // Check right neighbor for blanks
                        if (blanks.get(i+startRow).get(col+b+1))
                            neighbor = neighbor.replaceFirst(rightWord.charAt(b)+"", "_");
                    }
                }
                if (neighbor.length() > 1) neighborScore += calcRawScore(neighbor);
                if (bonuses.get(currentSpot) == Bonus.DL && !move.getBlanks().contains(i)){
                    if (neighbor.length() > 1) neighborScore += letterScores.get(move.getLetterAt(i));
                    main += letterScores.get(move.getLetterAt(i));
                }
                else if (bonuses.get(currentSpot) == Bonus.TL && !move.getBlanks().contains(i)){
                    if (neighbor.length() > 1) neighborScore += letterScores.get(move.getLetterAt(i))*2;
                    main += letterScores.get(move.getLetterAt(i))*2;
                }
                else if (bonuses.get(currentSpot) == Bonus.DW){
                    if (neighbor.length() > 1) neighborScore *= 2;
                    multiplier *= 2;
                }
                else if (bonuses.get(currentSpot) == Bonus.TW){
                    if (neighbor.length() > 1) neighborScore *= 3;
                    multiplier *= 3;
                }
            }
            result += neighborScore;
        }

        String scoredWord = "";
        for (int i=0; i<move.getSize(); i++){
            if (move.getBlanks().contains(i)) scoredWord += "_";
            else scoredWord += move.getWord().charAt(i);
        }
        result += (main+calcRawScore(scoredWord))*multiplier;
        if (move.getNewLettersList().size() == Player.HAND_SIZE) result += fullHandBonus;
        return result;
    }

    /**
     * Calculates the raw score for a given word.  Raw score is simply the score
     * of each individual letter added up for a total.
     *
     * @param word The word to calculate score for.
     * @return The raw score.
     */
    public int calcRawScore(String word){
        int result = 0;
        for (Character c : word.toCharArray()){
            result += letterScores.get(c);
        }
        return result;
    }

    /**
     * Places a given PlayerMove on the board, checks first to see if move is valid.
     * This does not calculate the score nor does it give any points after placement.
     *
     * @param move The move to be placed.
     * @return True if the placement was successful, false otherwise.
     */
    public boolean placeMove(PlayerMove move){
        // If the move is valid continue, else return false...
        if (validator.validate(move, this)){
            if (move.isHorizontal()){
                for (int i=0; i<move.getSize(); i++){
                    if (move.getBlanks().contains(i)){
                        blanks.get(move.getStartCoordinate().getRow()).
                                set(i+move.getStartCoordinate().getColumn(), true);
                    }
                    board.get(move.getStartCoordinate().getRow()).
                            set(i+move.getStartCoordinate().getColumn(), move.getLetterAt(i));
                }
            }
            else{
                for (int i=0; i<move.getSize(); i++){
                    if (move.getBlanks().contains(i)){
                        blanks.get(i+move.getStartCoordinate().getRow()).
                                set(move.getStartCoordinate().getColumn(), true);
                    }
                    board.get(i+move.getStartCoordinate().getRow()).
                            set(move.getStartCoordinate().getColumn(), move.getLetterAt(i));
                }
            }
            generateWordLists();
            setChanged();
            notifyObservers();
            return true;
        }
        return false;
    }

    /**
     * Places a letter at a given spot on the board.  Useful for when not playing
     * an actual game.  DOES NOT GENERATE WORDS AFTER PLACEMENT!!
     *
     * @param letter The letter to place.
     * @param c The spot to place it in.
     */
    public void placeLetter(char letter, Coordinate c){
        board.get(c.getRow()).set(c.getColumn(), letter);
    }

    /**
     * Places a letter at a given spot on the board.  Useful for when not playing
     * an actual game.  DOES NOT GENERATE WORDS AFTER PLACEMENT!!
     *
     * @param letter The letter to place.
     * @param row The row to place it in.
     * @param col The column to place it in.
     */
    public void placeLetter(char letter, int row, int col){
        placeLetter(letter, new Coordinate(row, col));
    }

    /**
     * Removes the letter at the given spot on the board.  Useful for when not
     * playing an actual game.  DOES NOT GENERATE WORDS AFTER PLACEMENT!!
     *
     * @param c The coordinate to remove.
     */
    public void removeLetterAt(Coordinate c){
        board.get(c.getRow()).set(c.getColumn(), '_');
    }

    /**
     * Removes the letter at the given spot on the board.  Useful for when not
     * playing an actual game.  DOES NOT GENERATE WORDS AFTER PLACEMENT!!
     *
     * @param row The row of the spot.
     * @param col The column of the spot.
     */
    public void removeLetterAt(int row, int col){
        removeLetterAt(new Coordinate(row, col));
    }

    /**
     * Populates the horizontal and vertical word lists.
     */
    public void generateWordLists(){
        horizontalWords.clear();
        verticalWords.clear();

        // Horizontal words.
        for (int i=0;i<board.size();i++){
            int j = 0;
            while (j<board.size()){
                String word = "";
                Coordinate start = new Coordinate(i, j);
                while (get(i, j) != '_' && get(i, j) != '#'){
                    word += get(i, j);
                    j++;
                }
                if (!word.equals("")){
                    for (int k=0;k<word.length();k++){
                        horizontalWords.put(start, word);
                        start = new Coordinate(i, start.getColumn()+1);
                    }
                }
                j++;
            }
        }

        // Vertical words.
        for (int j=0;j<board.size();j++){
            int i = 0;
            while (i<board.size()){
                String word = "";
                Coordinate start = new Coordinate(i, j);
                while (get(i, j) != '_' && get(i, j) != '#'){
                    word += get(i, j);
                    i++;
                }
                if (!word.equals("")){
                    for (int k=0;k<word.length();k++){
                        verticalWords.put(start, word);
                        start = new Coordinate(start.getRow()+1, j);
                    }
                }
                i++;
            }
        }
    }

    /**
     * Returns an integer that corresponds to the dimensions of the board.
     * For instance, if the board is 15 X 15, the int returned will be 15.
     * @return The int corresponding to the dimensions of the board.
     */
    public int getDimension(){
        return board.size();
    }

    /**
     * Returns the letter at the given coordinate.  Returns '_' if it is empty.
     * Returns '#' if out of bounds.
     *
     * @param c The coordinate to check.
     * @return The letter at the coordinate ('_' if empty, '#' if out of bounds).
     */
    public char get(Coordinate c){
        return get(c.getRow(), c.getColumn());
    }

    /**
     * Returns the letter at the given row and column.  Returns '_' if it is empty.
     * Returns '#' if out of bounds.
     *
     * @param row The row to check.
     * @param col The column to check.
     * @return The letter at the given spot ('_' if empty, '#' if out of bounds).
     */
    public char get(int row, int col){
        if (row < 0 || col < 0 || row >= board.size() || col >= board.size()) return '#';
        return board.get(row).get(col);
    }

    /**
     * Returns the horizontal word that overlaps the given coordinate.  Will return
     * "" if there is no word here.
     *
     * @param c A coordinate of the word.
     * @return The word itself.
     */
    public String getHorizontalWordAt(Coordinate c){
        if (horizontalWords.containsKey(c)) return horizontalWords.get(c);
        return "";
    }

    /**
     * Returns the vertical word that overlaps the given coordinate.  Will return
     * "" if there is no word here.
     *
     * @param c A coordinate of the word.
     * @return The word itself.
     */
    public String getVerticalWordAt(Coordinate c){
        if (verticalWords.containsKey(c)) return verticalWords.get(c);
        return "";
    }

    /**
     * Returns the horizontal word that overlaps the given row and column.  Will return
     * "" if there is no word here.
     *
     * @param row A row of the word.
     * @param col A col of the word.
     * @return The word itself.
     */
    public String getHorizontalWordAt(int row, int col){
        return getHorizontalWordAt(new Coordinate(row, col));
    }

    /**
     * Returns the vertical word that overlaps the given row and column.  Will return
     * "" if there is no word here.
     *
     * @param row A row of the word.
     * @param col A col of the word.
     * @return The word itself.
     */
    public String getVerticalWordAt(int row, int col){
        return getVerticalWordAt(new Coordinate(row, col));
    }

    /**
     * All of the coordinates on the board containing words.
     *
     * @return A set of all of the filled spots on the board.
     */
    public HashSet<Coordinate> getOccupiedCoordinates(){
        HashSet<Coordinate> result = new HashSet<Coordinate>();
        for (Coordinate c : horizontalWords.keySet()) result.add(c);
        return result;
    }

    /**
     * Gets the Bonus at the given coordinate in String form.  Will return
     * "" if there is no bonus at this spot.
     *
     * @param row The row to check.
     * @param col The column to check.
     * @return The Bonus in String form (i.e. "DL").
     */
    public String getBonusAt(int row, int col){
        return getBonusAt(new Coordinate(row, col));
    }

    /**
     * Gets the Bonus at the given coordinate in String form.  Will return
     * "" if there is no bonus at this spot.
     *
     * @param c The coordinate to check.
     * @return The Bonus in String form (i.e. "DL").
     */
    public String getBonusAt(Coordinate c){
        if (bonuses.containsKey(c)) return ""+bonuses.get(c);
        return "";
    }

    /**
     * A list of all of the possible bonus values.
     *
     * @return All of the bonuses available.
     */
    public static ArrayList<String> getAllBonuses(){
        ArrayList<String> result = new ArrayList<String>();
        for (Bonus b : Bonus.values()) result.add(b+"");
        result.add("2L");result.add("3L");result.add("2W");result.add("3W");
        return result;
    }

    /**
     * Returns true if this is an empty board.
     *
     * @return True if empty board.
     */
    public boolean isEmpty(){
        for (int i=0; i<board.size(); i++){
            for (int j=0; j<board.size(); j++){
            if (get(i, j) != '_') return false;
            }
        }
        return true;
    }

    /**
     * Tells if the tile at the given spot is blank.
     *
     * @param c The coordinate to check.
     * @return True if it is blank.
     */
    public boolean isBlank(Coordinate c){
        return isBlank(c.getRow(), c.getColumn());
    }

    /**
     * Tells if the tile at the given spot is blank.
     *
     * @param row The row to check.
     * @param col The column to check.
     * @return True if it is blank.
     */
    public boolean isBlank(int row, int col){
        if (row < 0 || col < 0 || row >= board.size() || col >= board.size()) return false;
        return blanks.get(row).get(col);
    }

    /**
     * Sets the given spot to a blank tile.
     *
     * @param c The spot to set.
     */
    public void setBlank(Coordinate c){
        setBlank(c.getRow(), c.getColumn());
    }

    /**
     * Sets the given spot to a blank tile.
     *
     * @param row The row of the spot to set.
     * @param col The column of the spot to set.
     */
    public void setBlank(int row, int col){
        blanks.get(row).set(col, true);
    }

    /**
     * Removes the blank at the given spot.
     *
     * @param c The spot to set to not blank.
     */
    public void removeBlank(Coordinate c){
        removeBlank(c.getRow(), c.getColumn());
    }

    /**
     * Removes the blank at the given spot.
     *
     * @param row The row of the spot to set to not blank.
     * @param col The column of the spot to set to not blank.
     */
    public void removeBlank(int row, int col){
        blanks.get(row).set(col, false);
    }

    @Override
    public String toString(){
        String result = "  ";
        for (int i=0;i<board.size();i++){
            result += (i%10)+"  ";
        }
        result += "\n";
        for (int i=0;i<board.size();i++){
            result += (i%10)+" ";
            for (int j=0;j<board.size();j++){
                if (get(i, j) == '_' && bonuses.containsKey(new Coordinate(i, j)) && printMultipliers){
                    result += bonuses.get(new Coordinate(i, j))+" ";
                }
                else if (i == j && get(i, j) == '_' && board.size() == (i*2)+1) result += "*  ";
                else result += get(i, j)+"  ";
            }
            result += " "+(i%10)+"\n";
        }
        result += "  ";
        for (int i=0;i<board.size();i++){
            result += (i%10)+"  ";
        }
        result += "\n";
        return result;
    }
}
