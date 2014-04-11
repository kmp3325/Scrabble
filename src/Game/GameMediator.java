package Game;

import GUI.SetUpWindow;
import Player.Human;
import Player.Player;
import Player.ExpertComputer;
import Player.TerribleComputer;
import Player.DecentComputer;
import Player.RandomComputer;
import Util.Dictionary;
import Util.Logger;
import Util.SavedPlayer;
import Util.Source;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/4/13
 * Time: 8:05 PM
 * The GameMediator simply goes through the stages of gameplay.
 */
public class GameMediator {

    public static String path = "src/";
    public static String configFile = path+"Configurations/Scrabble.cfg";
    public static String logFileBeginning = path+"Logs/";
    private ArrayList<Player> players;
    private Player currentPlayer;

    public GameMediator(){
        players = new ArrayList<Player>();
        Calendar cal = Calendar.getInstance();
        Logger.setLogger(logFileBeginning + "log_"+cal.getTime()+".txt");
        String classpath = System.getProperty("java.class.path");
        int jarPos = classpath.indexOf("Scrabble.exe");
        if (jarPos == -1) jarPos = classpath.indexOf("Scrabble.jar");
        if (jarPos != -1){
            int jarPathPos = classpath.lastIndexOf(File.pathSeparatorChar, jarPos)+1;
            path = classpath.substring(jarPathPos, jarPos);
            Scanner scanner = null;
            try {
                scanner = new Scanner(new File(path+"Configurations/Default.txt"));
                configFile = path+"Configurations/"+scanner.nextLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Start is called and will determine if the game is console or GUI based.  (Currently no console version)...
     */
    public void start(){
        startGUIVersion();
    }

    /**
     * Begins play of the GUI version of the game.
     */
    private void startGUIVersion(){
        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("info", new Color(0, 150, 150));
            UIManager.put("control", new Color(164,167,173));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }finally{
            new SetUpWindow(this);
        }
    }

    /**
     * Plays through a game.  First initializes the player list depending on set
     * Strings to be passed in that represent different players.
     *
     * @param parent The object that called play must be made able to be updated.
     * @param players The list of players to play the game.
     * @param board The board for this game.
     * @param bag The bag for this game.
     * @param dictionary The dictionary for this game.
     * @return The winner of the game.
     */
    public void play(Source parent, ArrayList<String> players, Board board, Bag bag, Dictionary dictionary){
        ArrayList<Player> newPlayers = new ArrayList<Player>();
        int i = 0;
        for (String player : players){
            i++;
            if (player.contains("Expert")) newPlayers.add(new ExpertComputer(i, board, bag, dictionary));
            else if (player.contains("Human")) newPlayers.add(new Human(parent, i, board, bag, dictionary));
            else if (player.contains("Decent")) newPlayers.add(new DecentComputer(i, board, bag, dictionary));
            else if (player.contains("Terrible")) newPlayers.add(new TerribleComputer(i, board, bag, dictionary));
            else if (player.contains("Random")) newPlayers.add(new RandomComputer(i, board, bag, dictionary));
        }

        for (Player player : newPlayers){
            player.populateHand();
        }

        load(parent, new LinkedList<Player>(newPlayers), board, bag, dictionary, 0);

    }

    /**
     * Loads a game from a given spot.
     *
     * @param parent The object that called play must be able to be updated.
     * @param players The list of players that were playing the game.
     * @param board The board for this game.
     * @param bag The bag for this game.
     * @param dictionary The dictionary for this game.
     * @param whoseTurn an integer representing the index of the player whose turn it was.
     * @return The winner of the game.
     */
    private void load(Source parent, LinkedList<Player> players, Board board, Bag bag, Dictionary dictionary, int whoseTurn){
        try {
            Thread.sleep(1000); // Give the board a second to reinitialize...
        }catch(InterruptedException e){}
        try{
        this.players.clear();
        for (Player p : players) this.players.add(p);

        boolean complete = false;
        int turn = 0;
        while (!complete){
            for (Player player : this.players){
                currentPlayer = player;
                parent.changeCurrentPlayer(currentPlayer);
                if (turn == whoseTurn) whoseTurn = -1;
                boolean stay = true;
                if (whoseTurn == -1){
                    stay = player.makeMove();
                }
                if (!stay) return;
                if (player.emptyHand()){
                    complete = true;
                    break;
                }
                if (parent.canceled()){
                    complete = true;
                    break;
                }
                turn = (turn+1)%players.size();
            }
        }

        Player winner = this.players.get(0);
        for (Player player : this.players){
            if (player.getScore() > winner.getScore()) winner = player;
        }

        parent.setWinner(winner);
        }catch(Exception e){e.printStackTrace();}
    }

    /**
     * Loads a game from a given spot.
     *
     * @param parent The object that called play must be able to be updated.
     * @param players The list of players that were playing the game.
     * @param board The board for this game.
     * @param bag The bag for this game.
     * @param dictionary The dictionary for this game.
     * @param whoseTurn an integer representing the index of the player whose turn it was.
     * @return The winner of the game.
     */
    public void load(Source parent, ArrayList<SavedPlayer> players, Board board, Bag bag, Dictionary dictionary, int whoseTurn){
        ArrayList<Player> realPlayers = new ArrayList<Player>();
        for (SavedPlayer p : players){
            Player newPlayer;
            if (p.type.equals("Player.ExpertComputer")) newPlayer = new ExpertComputer(p.id, board, bag, dictionary);
            else if (p.type.equals("Player.Human")) newPlayer = new Human(parent, p.id, board, bag, dictionary);
            else if (p.type.equals("Player.DecentComputer")) newPlayer = new DecentComputer(p.id, board, bag, dictionary);
            else if (p.type.equals("Player.RandomComputer")) newPlayer = new RandomComputer(p.id, board, bag, dictionary);
            else newPlayer = new Human(parent, p.id, board, bag, dictionary);
            newPlayer.setValues(p);
            realPlayers.add(newPlayer);
        }
        load(parent, new LinkedList<Player>(realPlayers), board, bag, dictionary, whoseTurn);
    }


    /**
     * Gets all of the players.  Useful for saving.
     *
     * @return A list of all players.
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }

    /**
     * Gets the current player that is taking their turn.
     *
     * @return The current player.
     */
    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * A mapping of all of the current scores.
     *
     * @return A map of player string to score.
     */
    public HashMap<String, Integer> getPlayerScores(){
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        for (Player p : players){
            result.put("Player "+p.getId()+" ("+p.getClass().getName().substring(7)+")", p.getScore());
        }
        return result;
    }
}
