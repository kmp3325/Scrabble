package GUI;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/18/13
 * Time: 8:46 PM
 * Sanatizes inputs.
 */
public class BoardFilter extends DocumentFilter {

    private MainFrame parent;

    protected BoardFilter(MainFrame parent){
        this.parent = parent;
    }

    public void insertString(DocumentFilter.FilterBypass fb, int offset, String text,
                             AttributeSet attr) throws BadLocationException {}

    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text,
                        AttributeSet attrs) throws BadLocationException {

        String s = text.toUpperCase();
        if (s.equals("")){
            fb.replace(offset, length, text.toUpperCase(), attrs);
            return;
        }
        // Pad special characters with spaces for the backspace button
        if ((((s.charAt(0) > 64 && s.charAt(0) < 91) || s.charAt(0) == '*') && parent.mode == Mode.CHEAT)
                || (parent.mode == Mode.GAME && ((parent.getHandString().contains(s) && s.length() == 1) ||
                s.equals("DL ") || s.equals("TL ") || s.equals("DW ") || s.equals("TW ") || s.equals("* ") ||
                s.equals("DL") || s.equals("TL") || s.equals("DW") || s.equals("TW") || s.equals("*") ||
                (parent.getHandString().contains("_") && s.charAt(0) > 64 && s.charAt(0) < 91)))){
            fb.replace(offset, length, text.toUpperCase(), attrs);
        }

        if (parent.toRemoveFromHand != null) parent.toRemoveFromHand.setText("");
        parent.toRemoveFromHand = null;
        if (parent.toAddToHand != null) for (JTextField t : parent.hand){
            if (t.getText().equals("")){
                t.setText(parent.toAddToHand);
                break;
            }
        }
        parent.toAddToHand = null;

    }
}
