package Util;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/8/13
 * Time: 7:51 PM
 * This class basically represents the data structure that will hold the dictionary.
 * It is a tree that can be traversed to find different words that start or end with
 * a given string much more quickly than other data structures because of the way that
 * it handles storing words.  The GADDAG contains the language L = {Rev(x)#y | xy is a word
 * and x != lambda}.
 */
public class GADDAG {
    private Character data;
    private ArrayList<GADDAG> children;
    private boolean terminal;

    /**
     * Initializes the GADDAG to non-terminal and to having no children.
     * If this is the first initialization of a new GADDAG from outside
     * sources, then the data passed in should be null to signify the root.
     *
     * @param data The Character this node represents.
     */
    public GADDAG(Character data){
        this.data = data;
        children = new ArrayList<GADDAG>();
        terminal = false;
    }

    /**
     * Adds to this GADDAG.  This is used by outside sources in order to add
     * full strings to the GADDAG.  In order for the string to properly represent
     * a word, added strings should always be the form of Rev(x)#y where xy is
     * a word and x is not empty.  This will always make the last state in the
     * string added terminal.
     *
     * @param str The current string to add.
     */
    public void add(String str){
        if (!str.equals("")){
            if (!containsTree(str.charAt(0))){
                children.add(new GADDAG(str.charAt(0)));
            }
            getTree(str.charAt(0)).add(str.substring(1));
        }
        else{
            terminal = true;
        }
    }

    /**
     * This simply tells if this level of the GADDAG has a child corresponding
     * to the given character.
     *
     * @param c The character to check for.
     * @return True if the GADDAG has this child at this level.
     */
    public boolean containsTree(Character c){
        for (GADDAG g : children) if (g.getData() == c) return true;
        return false;
    }

    /**
     * This simply gets the tree at this level corresponding to the given character.
     * If there is no tree corresponding to this character at this level, it
     * returns null.
     *
     * @param c The character of the tree to return.
     * @return The tree corresponding to this character at this level.
     */
    public GADDAG getTree(Character c){
        for (GADDAG g : children) if (g.getData() == c) return g;
        return null;
    }

    public Character getData(){return data;}

    public boolean isTerminal(){return terminal;}

    public ArrayList<GADDAG> getChildren(){return children;}

    /**
     * Used to check if a letter can be found anywhere down the tree from
     * this point on.
     *
     * @param c The letter to check for.
     * @return True if the letter is on some point down this tree.
     */
    @Deprecated
    public boolean contains(Character c){
        if (containsTree(c)) return true;
        else{
            for (GADDAG child : children) if (child.contains(c)) return true;
            return false;
        }
    }
}