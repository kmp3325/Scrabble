package Util;

import Game.GameMediator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/4/13
 * Time: 6:29 PM
 * The current dictionary of words.
 */
public class Dictionary {

    private GADDAG dictionary;

    /**
     * Creates the new Dictionary and GADDAG associated with it.
     */
    public Dictionary() throws FileNotFoundException{
        dictionary = new GADDAG(null);
        Scanner scanner = new Scanner(new File(GameMediator.configFile));
        // Skip to the dictionary part of the file by avoiding the first two "--".
        for (int i=0;i<2;i++) while (!scanner.nextLine().equals("--"));

        String next;
        while(scanner.hasNext()){
            next = scanner.nextLine().toUpperCase();
            add(next);
        }
    }

    /**
     * Adds a word to the GADDAG.  First finds all proper representations of the
     * word and then subsequently adds each string to the GADDAG.
     *
     * @param word The word to add to the dictionary.
     */
    private void add(String word){
        for (int i=1; i<word.length()+1; i++){
            StringBuilder toAdd = new StringBuilder(word.substring(0, i));
            toAdd.reverse();
            toAdd.append("#"+word.substring(i, word.length()));

            dictionary.add(toAdd.toString());
        }
    }


    /**
     * Deformats a string of the form Rev(x)#y such that xy is a word into the
     * word that it represents, that is the form xy.
     *
     * @param str The string to deformat.
     * @return The word that the string forms.
     */
    private String deFormat(String str){
        StringBuilder result = new StringBuilder(str.substring(0, str.indexOf('#')));
        result.reverse();
        if (str.indexOf('#') != str.length()-1) result.append(str.substring(str.indexOf('#')+1, str.length()));
        return result.toString();
    }

    /**
     * Checks to see if this word is contained in the dictionary.
     *
     * @param word The word to check for.
     * @return True if the dictionary contains this word.
     */
    public boolean contains(String word){
        if (word.equals("")) return false;
        return contains(word.toUpperCase().charAt(0)+"#"+word.toUpperCase().substring(1), dictionary);
    }

    /**
     * Helper function for contains.  Checks to see if the given GADDAG can
     * produce this string to a terminal state by traversing this GADDAG.
     *
     * @param str The string to produce.
     * @param tree The current tree we are at.
     * @return True if the string can be produced.
     */
    private boolean contains(String str, GADDAG tree){
        if (str.length() == 0 && tree.isTerminal()) return true;
        else if (str.length() > 0 && tree.containsTree(str.charAt(0))){
            return contains(str.substring(1), tree.getTree(str.charAt(0)));
        }
        else return false;
    }

    /**
     * Gets all of the words in the dictionary.
     *
     * @return a list of every word in the dictionary, sorted.
     */
    public ArrayList<String> getSortedDictionary(){
        ArrayList<String> result = new ArrayList<String>();
        for (String str : getWordsStartingWith("")) result.add(str);
        Collections.sort(result);
        return result;
    }

    /**
     * Finds all of the strings from this point on in the GADDAG.  It simply
     * traverses the tree and returns all of the strings with a terminal state
     * somewhere in the traversal.
     *
     * @param tree The current level of the GADDAG.
     * @return All of the strings in this GADDAG.
     *
     */
    private ArrayList<String> getAllStrings(GADDAG tree){
        ArrayList<String> result = new ArrayList<String>();
        if (tree.isTerminal()) result.add(tree.getData()+"");
        for (GADDAG child : tree.getChildren()){
            for (String str : getAllStrings(child)){
                if (tree.getData() != null) result.add(tree.getData()+""+str);
                else result.add(str);
            }
        }
        return result;
    }

    /**
     * Gather a list of all of the words that start with this prefix.
     *
     * @param prefix The prefix to check for.
     * @return All of the words that start with this prefix.
     */
    public ArrayList<String> getWordsStartingWith(String prefix){
        return getWordsStartingWith(prefix.toUpperCase(), dictionary);
    }

    /**
     * This function works with words instead of any string.  It will traverse
     * the tree and gather a list of all of the words that start with the given
     * prefix.
     *
     * @param prefix The current portion of prefix to check for.
     * @param tree The current tree we are traversing.
     * @return A list of all of the words that start with the given prefix.
     */
    private ArrayList<String> getWordsStartingWith(String prefix, GADDAG tree){
        ArrayList<String> result = new ArrayList<String>();
        if (tree.getData() == null && prefix.equals("")){
            for (GADDAG child : tree.getChildren()){
                for (String str : getWordsStartingWith("", child)) result.add(child.getData()+str);
            }
        }
        else if (tree.getData() == null){
            for (String suffix : getWordsStartingWith(prefix.substring(0, prefix.length()-1), tree.getTree(prefix.charAt(prefix.length()-1)))){
                result.add(prefix+suffix);
            }
        }
        else if (prefix.equals("")){
            if (tree.containsTree('#')){
                for (String str : getAllStrings(tree.getTree('#'))){
                    result.add(str.substring(1)); // Substring removes the '#'
                }
            }
        }
        else if (tree.containsTree(prefix.charAt(prefix.length()-1))){
            result = getWordsStartingWith(prefix.substring(0, prefix.length()-1), tree.getTree(prefix.charAt(prefix.length()-1)));
        }
        return result;
    }

    /**
     * Gather a list of all of the words that end with this suffix.
     *
     * @param suffix The suffix to check for.
     * @return All of the words that end with this suffix.
     */
    public ArrayList<String> getWordsEndingWith(String suffix){
        return getWordsEndingWith(suffix.toUpperCase(), dictionary);
    }

    /**
     * This function works with words instead of any string.  It will traverse
     * the tree and gather a list of all of the words that end with the given
     * suffix.
     *
     * @param suffix The current portion of suffix to check for.
     * @param tree The current tree we are traversing.
     * @return A list of all of the words that end with the given suffix.
     */
    private ArrayList<String> getWordsEndingWith(String suffix, GADDAG tree){
        ArrayList<String> result = new ArrayList<String>();
        if (tree.getData() == null && suffix.equals("")) return result;
        else if (tree.getData() == null){
            for (String prefix : getWordsEndingWith(suffix.substring(0, suffix.length()-1), tree.getTree(suffix.charAt(suffix.length()-1)))){
                result.add(prefix+suffix);
            }
            if (contains(suffix)) result.add(suffix);
        }
        else if (suffix.equals("")){
            for (GADDAG child : tree.getChildren()){
                for (String str : getAllStrings(child)){
                    if (str.endsWith("#")) result.add(deFormat(str));
                }
            }
        }
        else if (tree.containsTree(suffix.charAt(suffix.length()-1))){
            result = getWordsEndingWith(suffix.substring(0, suffix.length()-1), tree.getTree(suffix.charAt(suffix.length()-1)));
        }
        return result;
    }

    /**
     * Gives an instance of the GADDAG so that it can be traversed for move
     * generation algorithms.
     *
     * @return The GADDAG.
     */
    public GADDAG theTree(){
        return dictionary;
    }
}
