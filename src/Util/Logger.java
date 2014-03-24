package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 3/23/14
 * Time: 10:53 PM
 * Logs certain critical information for the game.  Useful for debugging.
 */
public class Logger {

    private static PrintWriter writer;

    /**
     * Sets the output file for the logger to write to.
     *
     * @param outputFile The output file for the logger.
     */
    public static void setLogger(String outputFile) {
        try {
            new File(outputFile);
            writer = new PrintWriter(outputFile, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs a message with the timestamp to the output file.
     *
     * @param message The message that was logged.
     */
    public static void log(String message) {
        Calendar cal = Calendar.getInstance();
        writer.println(cal.getTime());
        System.out.println(cal.getTime());
        writer.println("    "+message);
        System.out.println("    "+message);
    }
}
