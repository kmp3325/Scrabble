package Util;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/4/13
 * Time: 5:31 PM
 * A simple object for keeping track of a coordinate (row, column).
 */
public class Coordinate {

    private int row;
    private int column;

    /**
     * Simply instantiates the coordinate with a row and column.
     *
     * @param row
     * @param column
     */
    public Coordinate(int row, int column){
        this.row = row;
        this.column = column;
    }

    public int getRow(){return row;}

    public int getColumn(){return column;}

    @Override
    public String toString(){
        return "("+row+","+column+")";
    }

    @Override
    public boolean equals(Object c){
        if (c == null || !(c instanceof Coordinate)) return false;
        return (row == ((Coordinate) c).getRow() && column == ((Coordinate) c).getColumn());
    }

    @Override
    public int hashCode(){
        return (10000*row) + (10*column);
    }
}
