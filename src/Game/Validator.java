package Game;

import Player.PlayerMove;
import Util.Coordinate;
import Util.Dictionary;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/4/13
 * Time: 4:22 PM
 * This class has the ability to validate a given move based on a given board.
 * The only thing that it keeps track of internally is it's own dictionary.
 */
public class Validator {

    private Dictionary dictionary;

    /**
     * Creates a Validator.
     *
     * @param dictionary The dictionary to be used for this game.
     */
    public Validator(Dictionary dictionary){
        this.dictionary = dictionary;
    }

    /**
     * Tells whether the given move is valid to be placed on the given board.
     *
     * @param move The move to be checked.
     * @param board The board that the move would be placed on.
     * @return True if the move is valid (within the bounds of the board, doesn't
     * overlap another letter currently on the board, and is part of the dictionary.
     */
    public boolean validate(PlayerMove move, Board board){
        if (!dictionary.contains(move.getWord())) return false;
        if (move.getWord().length() == 1) return false;
        int startRow = move.getStartCoordinate().getRow();
        int startCol = move.getStartCoordinate().getColumn();
        int endCol = move.getEndCoordinate().getColumn();
        int endRow = move.getEndCoordinate().getRow();

        boolean result;
        if (move.isHorizontal()){

            // Check to make sure word doesn't go out of bounds and that the
            // player isn't placing tiles on top of existing ones.
            for (int i=startCol; i<endCol+1; i++){
                Coordinate spot = new Coordinate(startRow, i);
                if (i >= board.getDimension() || i < 0 || (
                        board.get(spot) != '_' && move.getNewLettersList()
                                .contains(i-startCol))){
                    return false;
                }
            }

            result = checkHorizontalNeighbors(move, board);
            if (result){ // Check if user lied about where move starts and ends...Bastards!
                if (!(board.get(startRow, startCol-1) == '_' || board.get(startRow, startCol-1) == '#') ||
                        !(board.get(startRow, endCol+1) == '_' || board.get(startRow, endCol+1) == '#')){
                    result = false;
                }
            }
        }
        // Repeat the horizontal approach by simply replacing the rows and columns.
        else{
            for (int i=startRow; i<endRow+1; i++){
                Coordinate spot = new Coordinate(i, startCol);
                if (i >= board.getDimension() || i < 0 || (
                        board.get(spot) != '_' && move.getNewLettersList()
                                .contains(i - startRow))){
                    return false;
                }
            }

            result = checkVerticalNeighbors(move, board);
            if (result){ // Check if user lied about where move starts and ends...Bastards!
                if (!(board.get(startRow-1, startCol) == '_' || board.get(startRow-1, startCol) == '#') ||
                        !(board.get(endRow+1, startCol) == '_' || board.get(endRow+1, startCol) == '#')){
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * This function's preconditions are that the word being placed is in the
     * dictionary, it does not exceed the bounds of the board, and the letters
     * being placed are not on top of existing letters.  This function then
     * checks all neighboring spots of the word to make sure that any extra
     * words created are in the dictionary.  It also makes sure that the word
     * being placed is either adjacent to another letter already on the board
     * or (in the case of the first move) covers the middle space on the board.
     *
     * @param move The move to be checked.
     * @param board The board to check it against.
     * @return True if the move is valid through neighbors.
     */
    private boolean checkHorizontalNeighbors(PlayerMove move, Board board){
        boolean hasNeighbor = false;
        // If part of our horizontal word was already present on the board.
        if (move.getNewLettersList().size() != move.getWord().length()) hasNeighbor = true;
        int row = move.getStartCoordinate().getRow();
        int startCol = move.getStartCoordinate().getColumn();

        for (int i=0; i<move.getSize(); i++){
            Coordinate spotAbove = new Coordinate(row-1, i+startCol);
            Coordinate spotBelow = new Coordinate(row+1, i+startCol);

            if (move.getNewLettersList().contains(i)){
                String neighbor = board.getVerticalWordAt(spotAbove)
                        +move.getLetterAt(i)+board.getVerticalWordAt(spotBelow);
                if (!dictionary.contains(neighbor)){
                    return false;
                }
                else if (neighbor.length() > 1) hasNeighbor = true;
            }
        }

        if (hasNeighbor) return true;

        boolean coversMiddle = false;

        if (1+(row*2) == board.getDimension()){
            for (int i=0;i<move.getWord().length();i++){
                if (1+((i+startCol)*2) == board.getDimension()) coversMiddle = true;
            }
        }

        if (coversMiddle && move.getWord().length() == 1) return false; // First move can't be of length 1

        return coversMiddle;
    }

    /**
     * This function's preconditions are that the word being placed is in the
     * dictionary, it does not exceed the bounds of the board, and the letters
     * being placed are not on top of existing letters.  This function then
     * checks all neighboring spots of the word to make sure that any extra
     * words created are in the dictionary.  It also makes sure that the word
     * being placed is either adjacent to another letter already on the board
     * or (in the case of the first move) covers the middle space on the board.
     *
     * @param move The move to be checked.
     * @param board The board to check it against.
     * @return True if the move is valid through neighbors.
     */
    private boolean checkVerticalNeighbors(PlayerMove move, Board board){
        boolean hasNeighbor = false;
        // If part of our vertical word was already present on the board.
        if (move.getNewLettersList().size() != move.getWord().length()) hasNeighbor = true;
        int col = move.getStartCoordinate().getColumn();
        int startRow = move.getStartCoordinate().getRow();

        for (int i=0; i<move.getSize(); i++){
            Coordinate spotLeft = new Coordinate(i+startRow, col-1);
            Coordinate spotRight = new Coordinate(i+startRow, col+1);

            if (move.getNewLettersList().contains(i)){
                String neighbor = board.getHorizontalWordAt(spotLeft)
                        +move.getLetterAt(i)+board.getHorizontalWordAt(spotRight);
                if (!dictionary.contains(neighbor)) return false;
                else if (neighbor.length() > 1) hasNeighbor = true;
            }
        }

        if (hasNeighbor) return true;

        boolean coversMiddle = false;

        if (1+(col*2) == board.getDimension()){
            for (int i=0;i<move.getWord().length();i++){
                if (1+((i+startRow)*2) == board.getDimension()) coversMiddle = true;
            }
        }

        if (coversMiddle && move.getWord().length() == 1) return false; // First move can't be of length 1

        return coversMiddle;
    }
}
