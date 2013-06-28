package Game;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/5/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static boolean console;

    public static void main(String[] args){
        System.out.println(Runtime.getRuntime().totalMemory());
        GameMediator game = new GameMediator();
        if (args.length > 0) console = true;
        else console = false;
        game.start();
    }
}
