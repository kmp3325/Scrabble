package GUI;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class HandFilter extends DocumentFilter {

    public void insertString(DocumentFilter.FilterBypass fb, int offset, String text,
                             AttributeSet attr) throws BadLocationException {

        String s = text.toUpperCase();
        if (s.length() == 0 || (s.charAt(0) > 64 && s.charAt(0) < 91) || s.equals("_")){
            fb.insertString(offset, s, attr);
        }
    }

    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {

        String s = text.toUpperCase();
        if (s.length() == 0 || (s.charAt(0) > 64 && s.charAt(0) < 91) || s.equals("_")){
            fb.replace(offset, length, text.toUpperCase(), attrs);
        }

    }
}
