package Game;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/4/13
 * Time: 4:50 PM
 * The class for holding the bag of tiles.  Can be instantiated to multiple game
 * types and operated on accordingly (removing, adding, etc).
 */
public class Bag {

    // Guide bag never changes so that we can reset the contents for new games.
    private ArrayList<Character> guideBag;
    private ArrayList<Character> bag;
    private Random random;

    /**
     * Creates a new Bag based on the given game type.
     */
    public Bag() throws FileNotFoundException{
        random = new Random();
        bag = new ArrayList<Character>();
        guideBag = new ArrayList<Character>();

        Scanner scanner = new Scanner(new File(GameMediator.configFile));

        scanner.nextLine(); // Get rid of board size.
        scanner.nextLine(); // Get rid of hand size.
        String next = scanner.nextLine(); // Get rid of full hand bonus.
        while (!(next = scanner.nextLine()).equals("--")){
            String[] s = next.split(" ");
            for (int i=0; i<Integer.parseInt(s[2]); i++){
                bag.add(s[0].charAt(0));
                guideBag.add(s[0].charAt(0));
            }
        }
    }

    /**
     * Gets a random tile from the bag and then removes it.
     *
     * @return A randomly selected Character from the bag.
     */
    public Character getRandomTile(){
        return bag.remove(random.nextInt(bag.size()));
    }

    /**
     * Adds the given character to the bag.
     *
     * @param c The character to be added.
     */
    public void add(Character c){
        bag.add(c);
    }

    /**
     * Determines whether or not the bag is empty.
     *
     * @return True if the bag is empty.
     */
    public boolean isEmpty(){
        return bag.isEmpty();
    }

    /**
     * How many tiles are left in the bag.
     *
     * @return The number of tiles remaining.
     */
    public int getNumberRemaining(){
        return bag.size();
    }

    /**
     * Completely resets the bag's contents.
     */
    public void reset(){
        bag.clear();
        bag.addAll(guideBag);
    }
}
