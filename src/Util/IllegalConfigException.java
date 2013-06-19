package Util;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/4/13
 * Time: 10:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class IllegalConfigException extends Exception{

    /**
     * Creates a new instance that will report "Illegal configuration file."
     */
    public IllegalConfigException(){
        super("Illegal Configuration File.");
    }

}
