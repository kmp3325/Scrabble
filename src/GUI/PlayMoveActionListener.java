package GUI;

import Player.PlayerMove;
import Util.Coordinate;
import Util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlayMoveActionListener implements ActionListener {

    private MainFrame parent;

    protected PlayMoveActionListener(MainFrame parent){
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If middle isn't covered, we know we're on first move and it's invalid.
        if (parent.virtualBoard.get(((parent.board.getDimension()/2)*parent.board.getDimension())
                +(parent.board.getDimension()/2)).getText().equals("*")){
            JOptionPane.showMessageDialog(parent, "This Move is not valid", "Invalid Move", JOptionPane.PLAIN_MESSAGE);
            Logger.log("Middle of board was not covered by move.");
            return;
        }
        // This is what we need to find.
        boolean horizontal;
        String word = "";
        Coordinate start;
        ArrayList<Integer> newLetters = new ArrayList<Integer>();
        ArrayList<Integer> blankSpots = new ArrayList<Integer>();

        // Gather all of the new Coordinates for newLetters and as a temporary start.
        HashSet<Coordinate> coordinates = new HashSet<Coordinate>();
        for (int i=0; i<parent.board.getDimension(); i++){
            for (int j=0; j<parent.board.getDimension(); j++){
                String virtual = parent.virtualBoard.get((i*parent.board.getDimension())+j).getText();
                if (parent.board.get(i, j) == '_' && virtual.length() == 1 && !virtual.equals("*")){
                    coordinates.add(new Coordinate(i, j));
                }
            }
        }

        if (coordinates.size() > 1){
            if (((Coordinate) coordinates.toArray()[0]).getRow() ==
                    ((Coordinate) coordinates.toArray()[1]).getRow()){
                horizontal = true;
                // Check to see if there are any spots left blank in this row between newly placed pieces.
                ArrayList<Integer> seq = new ArrayList<Integer>();
                for (Coordinate c : coordinates) seq.add(c.getColumn());
                Collections.sort(seq);
                int prev = seq.get(0)-1;
                for (int i : seq) {
                    if (i != prev+1 && parent.board.get(((Coordinate) coordinates.toArray()[0]).getRow(), i-1) == '_') {
                        JOptionPane.showMessageDialog(parent, "This Move is not valid", "Invalid Move", JOptionPane.PLAIN_MESSAGE);
                        String coordinatesDebugString = "";
                        for (Coordinate c : coordinates) coordinatesDebugString += c+" ";
                        Logger.log("The coordinates had spaces: "+coordinatesDebugString+"\n"+parent.getBoardString());
                        return;
                    }
                    prev = i;
                }
            }
            else if (((Coordinate) coordinates.toArray()[0]).getColumn() ==
                    ((Coordinate) coordinates.toArray()[1]).getColumn()){
                horizontal = false;
                // Check to see if there are any spots left blank in this row between newly placed pieces.
                ArrayList<Integer> seq = new ArrayList<Integer>();
                for (Coordinate c : coordinates) seq.add(c.getRow());
                Collections.sort(seq);
                int prev = seq.get(0)-1;
                for (int i : seq) {
                    if (i != prev+1 && parent.board.get(i-1, ((Coordinate) coordinates.toArray()[0]).getColumn()) == '_') {
                        JOptionPane.showMessageDialog(parent, "This Move is not valid", "Invalid Move", JOptionPane.PLAIN_MESSAGE);
                        String coordinatesDebugString = "";
                        for (Coordinate c : coordinates) coordinatesDebugString += c+" ";
                        Logger.log("The coordinates had spaces: "+coordinatesDebugString+"\n"+parent.getBoardString());
                        return;
                    }
                    prev = i;
                }
            }
            else{
                JOptionPane.showMessageDialog(parent, "This Move is not valid", "Invalid Move", JOptionPane.PLAIN_MESSAGE);
                String coordinatesDebugString = "";
                for (Coordinate c : coordinates) coordinatesDebugString += c+" ";
                Logger.log("The coordinates were not in the same row or column: "+coordinatesDebugString+"\n"+parent.getBoardString());
                return;
            }
        }
        else{
            start = (Coordinate) coordinates.toArray()[0];
            if (parent.board.get(start.getRow()+1, start.getColumn()) != '_' &&
                    parent.board.get(start.getRow()+1, start.getColumn()) != '#' ){
                horizontal = false;
            }
            else if (parent.board.get(start.getRow()-1, start.getColumn()) != '_' &&
                    parent.board.get(start.getRow()-1, start.getColumn()) != '#' ){
                horizontal = false;
            }
            else if (parent.board.get(start.getRow(), start.getColumn()+1) != '_' &&
                    parent.board.get(start.getRow(), start.getColumn()+1) != '#' ){
                horizontal = true;
            }
            else if (parent.board.get(start.getRow(), start.getColumn()-1) != '_' &&
                    parent.board.get(start.getRow(), start.getColumn()-1) != '#' ){
                horizontal = true;
            }
            else{
                JOptionPane.showMessageDialog(parent, "This Move is not valid", "Invalid Move", JOptionPane.PLAIN_MESSAGE);
                String coordinatesDebugString = "";
                for (Coordinate c : coordinates) coordinatesDebugString += c+" ";
                Logger.log("The coordinates were not in the same row or column: "+coordinatesDebugString+"\n"+parent.getBoardString());
                return;
            }
        }

        start = null;
        Coordinate spot = (Coordinate) coordinates.toArray()[0];
        String text = parent.virtualBoard.get((spot.getRow()*parent.board.getDimension())+spot.getColumn()).getText();
        if (horizontal){
            while (text.length() == 1 && spot.getColumn() != 0){
                start = spot;
                spot = new Coordinate(spot.getRow(), spot.getColumn()-1);
                text = parent.virtualBoard.get((spot.getRow()*parent.board.getDimension())+spot.getColumn()).getText();
            }
        }
        else{
            while (text.length() == 1 && spot.getRow() != 0){
                start = spot;
                spot = new Coordinate(spot.getRow()-1, spot.getColumn());
                text = parent.virtualBoard.get((spot.getRow()*parent.board.getDimension())+spot.getColumn()).getText();
            }
        }

        for (Coordinate c : coordinates){
            if (horizontal){
                if (c.getRow() != start.getRow() || c.getColumn() < start.getColumn()){
                    JOptionPane.showMessageDialog(parent, "This Move is not valid", "Invalid Move", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
            }
            else{
                if (c.getColumn() != start.getColumn() || c.getRow() < start.getRow()){
                    JOptionPane.showMessageDialog(parent, "This Move is not valid", "Invalid Move", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
            }
        }

        // Now gather the word and the newLetters list based on passed over coordinates.
        spot = start;
        text = parent.virtualBoard.get((spot.getRow()*parent.board.getDimension())+spot.getColumn()).getText();
        int i = 0;
        if (horizontal){
            while (text.length() == 1 && spot.getColumn() != parent.board.getDimension()){
                word += text;
                if (coordinates.contains(spot)) newLetters.add(i);
                if (parent.virtualBoard.get((spot.getRow()*parent.board.getDimension())+spot.getColumn()).getForeground() == Color.RED ||
                        parent.virtualBoard.get((spot.getRow()*parent.board.getDimension())+spot.getColumn()).getDisabledTextColor() == Color.RED) blankSpots.add(i);
                spot = new Coordinate(spot.getRow(), spot.getColumn()+1);
                if (spot.getColumn() != parent.board.getDimension()) text = parent.virtualBoard.get((spot.getRow()*parent.board.getDimension())+spot.getColumn()).getText();
                i++;
            }
        }
        else{
            while (text.length() == 1 && spot.getRow() != parent.board.getDimension()){
                word += text;
                if (coordinates.contains(spot)) newLetters.add(i);
                if (parent.virtualBoard.get((spot.getRow()*parent.board.getDimension())+spot.getColumn()).getForeground() == Color.RED ||
                        parent.virtualBoard.get((spot.getRow()*parent.board.getDimension())+spot.getColumn()).getDisabledTextColor() == Color.RED) blankSpots.add(i);
                spot = new Coordinate(spot.getRow()+1, spot.getColumn());
                if (spot.getRow() != parent.board.getDimension()) text = parent.virtualBoard.get((spot.getRow()*parent.board.getDimension())+spot.getColumn()).getText();
                i++;
            }
        }

        Logger.log("Confirming move: (horizontal?) " + horizontal + " (word) " + word + " New Letter Indexes: " + newLetters + " Blank Tile Indexes: " + blankSpots + "\n" + parent.getBoardString());


        PlayerMove move = new PlayerMove(horizontal, word, start, newLetters, blankSpots);
        if (parent.validator.validate(move, parent.board)){
            if (parent.confirmation("Play Move", "This move will be played.")){
                parent.setMoveChoice(move);
                Logger.log("Move confirmed.");
            }
            else Logger.log("Move canceled.");
        }
        else{
            JOptionPane.showMessageDialog(parent, "This Move is not valid", "Invalid Move", JOptionPane.PLAIN_MESSAGE);
            Logger.log("Move was not valid.");
        }
    }
}
