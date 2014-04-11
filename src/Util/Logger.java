package Util;

import java.io.*;
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
            File f = new File(outputFile);
            f.getParentFile().mkdirs();
            f.createNewFile();
            writer = new PrintWriter(outputFile, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
