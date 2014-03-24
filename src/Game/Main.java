package Game;

import Util.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/5/13
 * Time: 4:39 PM
 * Main class to run the game out of.
 */
public class Main {

    public static void main(String[] args){
        // System.out.println(Runtime.getRuntime().totalMemory());
        new GameMediator().start();

    }
}
