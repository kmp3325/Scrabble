package GUI;

import Game.Board;
import Game.GameMediator;
import Util.IllegalConfigException;
import Util.SavedBoard;
import Util.SavedGame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/17/13
 * Time: 10:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class OpenActionListener implements ActionListener {
    private MainFrame parent;

    protected OpenActionListener(MainFrame parent){
        super();
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Saved Game and Board Files", "svg", "brd");
        chooser.setCurrentDirectory(new File(GameMediator.path+"Saves"));
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(parent);

        if(returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()));
                if (chooser.getSelectedFile().getName().endsWith("brd")){
                    SavedBoard saved = (SavedBoard)in.readObject();
                    in.close();
                    parent.board.clear();
                    for (int i=0; i<parent.board.getDimension(); i++){
                        for (int j=0; j<parent.board.getDimension(); j++){
                            if (saved.board.get(i).get(j) != '_'){
                                parent.board.placeLetter(saved.board.get(i).get(j), i, j);
                                parent.virtualBoard.get(i*parent.board.getDimension()+j).setText(saved.board.get(i).get(j)+"");
                                if (!saved.blanks.get(i).get(j)) parent.virtualBoard.get(i*parent.board.getDimension()+j).setForeground(Color.BLACK);
                                else parent.virtualBoard.get(i*parent.board.getDimension()+j).setForeground(Color.RED);
                            }
                        }
                    }
                    for (int i=0; i<saved.hand.size(); i++) parent.hand.get(i).setText(saved.hand.get(i)+"");
                    parent.revalidate();
                    parent.currentFile = chooser.getSelectedFile();
                }
                else if (chooser.getSelectedFile().getName().endsWith("svg")){
                    SavedGame saved = (SavedGame)in.readObject();
                    in.close();
                    parent.setOptionsForGame(true);
                    parent.mode = Mode.CHEAT;
                    parent.board.clear();
                    for (int i=0; i<parent.board.getDimension(); i++){
                        for (int j=0; j<parent.board.getDimension(); j++){
                            if (saved.board.get(i).get(j) != '_'){
                                parent.board.placeLetter(saved.board.get(i).get(j), i, j);
                                parent.virtualBoard.get(i*parent.board.getDimension()+j).setText(saved.board.get(i).get(j)+"");
                                parent.virtualBoard.get(i*parent.board.getDimension()+j).setForeground(Color.BLACK);
                                if (saved.blanks.get(i).get(j)) parent.board.setBlank(i, j);
                            }
                        }
                    }
                    parent.revalidate();
                    parent.mode = Mode.GAME;
                    parent.board.generateWordLists();
                    parent.initializeBoard();
                    while (!parent.bag.isEmpty()) parent.bag.getRandomTile();
                    for (Character c : saved.baggage) parent.bag.add(c);
                    parent.game = new GamePlay(parent, saved.players, saved.whoseTurn);
                    parent.game.execute();
                }
                else{
                    throw new IllegalConfigException();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "There was an error with loading...", "LOAD ERROR", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
