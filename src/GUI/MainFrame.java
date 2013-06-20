package GUI;

import Game.*;
import Util.*;
import Player.Player;
import Player.PlayerMove;
import Util.Dictionary;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/5/13
 * Time: 7:27 PM
 * The frame that the main GUI resides in.
 */
public class MainFrame extends JFrame implements Observer, Source {

    protected GamePlay game;
    private PlayerMove moveChoice;
    private boolean humanTurn;

    protected JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem newMenuItem;
    private JMenuItem openMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem saveAsMenuItem;
    private JMenuItem endMenuItem;
    private JMenuItem quitMenuItem;
    private JMenu editMenu;
    private JMenuItem setConfigFileItem;
    private JMenuItem setDefaultConfigItem;
    private JMenu viewMenu;
    private JMenuItem letterScoresItem;
    private JMenuItem dictionaryItem;
    private JMenuItem playerScoresItem;
    private JMenuItem helpMenuItem;

    private JPanel mainPanel;
    private JPanel centerPanel;
    protected JPanel southPanel;
    private JPanel northPanel;

    private JButton shuffleButton;
    private JButton exchangeButton;
    protected JButton playButton;
    protected JButton clearBoardButton;
    protected JButton getBestMoveButton;
    private JRadioButton horizontalButton;
    protected JRadioButton verticalButton;
    protected int handSize;
    protected ArrayList<JTextField> hand;
    protected ArrayList<JTextField> virtualBoard;

    private JLabel leaderScore;
    private JLabel currentScore;
    protected JLabel bagNumber;
    private JLabel playersTurn;

    protected Board board;
    protected Dictionary dictionary;
    protected Validator validator;
    protected Bag bag;
    protected GameMediator mediator;
    private boolean canceled;

    protected JTextField letterSelected; // The letter on the board that is currently selected.
    protected JTextField toRemoveFromHand; // The letter to be removed after a letter is placed on the board.
    protected String toAddToHand; // The letter to be added to your hand after backspacing it.
    protected boolean usedBlank;
    protected File currentFile; // Which file we loaded from if any.  Starts off as null.

    protected Mode mode;

    /**
     * GUI Constructor, setting menu items and such.
     */
    public MainFrame(GameMediator mediator, Board board, Dictionary dictionary, Validator validator, Bag bag){
        this.mediator = mediator;
        this.board = board;
        this.dictionary = dictionary;
        this.validator = validator;
        this.bag = bag;
        this.handSize = Player.HAND_SIZE;
        currentFile = null;
        humanTurn = true;
        canceled = true;
        usedBlank = false;
        board.addObserver(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setTitle("Scrabble 2.0");
        setBounds(new Rectangle(0, 0, 560, 530));
        setLocationRelativeTo(null);
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        viewMenu = new JMenu("View");

        Image newimg = null;
        Image openimg = null;
        Image saveimg = null;
        Image saveasimg = null;
        Image endimg = null;
        Image quitimg = null;
        Image setconfigimg = null;
        Image setdefaultconfigimg = null;
        Image letterscoresimg = null;
        Image dictionaryimg = null;
        Image playerscoresimg = null;
        Image helpimg = null;
        try {
            newimg = ImageIO.read(new File(GameMediator.path+"Icons/new.png"));
            openimg = ImageIO.read(new File(GameMediator.path+"Icons/open.png"));
            saveimg = ImageIO.read(new File(GameMediator.path+"Icons/save.png"));
            saveasimg = ImageIO.read(new File(GameMediator.path+"Icons/saveas.jpeg"));
            endimg = ImageIO.read(new File(GameMediator.path+"Icons/end.png"));
            quitimg = ImageIO.read(new File(GameMediator.path+"Icons/quit.png"));
            setconfigimg = ImageIO.read(new File(GameMediator.path+"Icons/setconfig.png"));
            setdefaultconfigimg = ImageIO.read(new File(GameMediator.path+"Icons/setdefaultconfig.png"));
            letterscoresimg = ImageIO.read(new File(GameMediator.path+"Icons/letterscores.png"));
            dictionaryimg = ImageIO.read(new File(GameMediator.path+"Icons/dictionary.png"));
            playerscoresimg = ImageIO.read(new File(GameMediator.path+"Icons/playerscores.png"));
            helpimg = ImageIO.read(new File(GameMediator.path+"Icons/help.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (newimg != null) newMenuItem = new JMenuItem("New Game",  new ImageIcon(newimg));
        else newMenuItem = new JMenuItem("New Game");
        if (openimg != null) openMenuItem = new JMenuItem("Open", new ImageIcon(openimg));
        else openMenuItem = new JMenuItem("Open");
        if (saveimg != null) saveMenuItem = new JMenuItem("Save", new ImageIcon(saveimg));
        else saveMenuItem = new JMenuItem("Save");
        if (saveasimg != null) saveAsMenuItem = new JMenuItem("Save As", new ImageIcon(saveasimg));
        else saveAsMenuItem = new JMenuItem("Save As");
        if (endimg != null) endMenuItem = new JMenuItem("End Game", new ImageIcon(endimg));
        else endMenuItem = new JMenuItem("End Game");
        if (quitimg != null) quitMenuItem = new JMenuItem("Quit", new ImageIcon(quitimg));
        else quitMenuItem = new JMenuItem("Quit");

        if (setconfigimg != null) setConfigFileItem = new JMenuItem("Set Config File", new ImageIcon(setconfigimg));
        else setConfigFileItem = new JMenuItem("Set Config File");
        if (setdefaultconfigimg != null) setDefaultConfigItem = new JMenuItem("Set Default Config", new ImageIcon(setdefaultconfigimg));
        else setDefaultConfigItem = new JMenuItem("Set Default Config");

        if (letterscoresimg != null) letterScoresItem = new JMenuItem("Letter Scores", new ImageIcon(letterscoresimg));
        else letterScoresItem = new JMenuItem("Letter Scores");
        if (dictionaryimg != null) dictionaryItem = new JMenuItem("Dictionary", new ImageIcon(dictionaryimg));
        else dictionaryItem = new JMenuItem("Dictionary");
        if (playerscoresimg != null) playerScoresItem = new JMenuItem("Player Scores", new ImageIcon(playerscoresimg));
        else playerScoresItem = new JMenuItem("Player Scores");
        if (helpimg != null) helpMenuItem = new JMenuItem("Help", new ImageIcon(helpimg));
        else helpMenuItem = new JMenuItem("Help");


        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        endMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        setConfigFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        setDefaultConfigItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        letterScoresItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        dictionaryItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        playerScoresItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));


        newMenuItem.addActionListener(new NewActionListener(this));

        openMenuItem.addActionListener(new OpenActionListener(this));

        saveMenuItem.addActionListener(new SaveActionListener(this));

        saveAsMenuItem.addActionListener(new SaveAsActionListener(this));

        endMenuItem.addActionListener(new EndActionListener(this));

        quitMenuItem.addActionListener(new QuitActionListener(this));

        setConfigFileItem.addActionListener(new SetConfigActionListener(this));

        setDefaultConfigItem.addActionListener(new SetDefaultConfigActionListener(this));

        letterScoresItem.addActionListener(new LetterScoresActionListener(this));

        dictionaryItem.addActionListener(new DictionaryActionListener(this));

        playerScoresItem.addActionListener(new PlayerScoresActionListener(this));

        helpMenuItem.addActionListener(new HelpActionListener(this));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitMenuItem.doClick();
            }
        });

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(quitMenuItem);
        fileMenu.add(endMenuItem);

        editMenu.add(setConfigFileItem);
        editMenu.add(setDefaultConfigItem);

        viewMenu.add(dictionaryItem);
        viewMenu.add(letterScoresItem);
        viewMenu.add(playerScoresItem);
        viewMenu.add(helpMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);

        setJMenuBar(menuBar);

        mainPanel = new JPanel(new BorderLayout());

        setOptionsForGame(false);

        add(mainPanel);
        setVisible(true);
        hand.get(0).requestFocusInWindow();
    }

    public PlayerMove moveChoice(){
        return moveChoice;
    }

    @Override
    public void update(Observable o, Object arg) {
        initializeBoard();
    }

    /**
     * Sets the winner.
     *
     * @param winner The winner of the game.
     */
    public void setWinner(Player winner){
        JOptionPane.showMessageDialog(this, "The Winner is Player "+winner.getId()+"!!!");
        setOptionsForGame(false);
    }

    /**
     * Changes the current player.
     *
     * @param current The new current player.
     */
    public void changeCurrentPlayer(Player current){
        moveChoice = null;
        // Set scores...
        currentScore.setText("Your score: "+current.getScore());
        int highest = 0;
        for (Player p : mediator.getPlayers()){
            if (p.getScore() > highest) highest = p.getScore();
        }
        leaderScore.setText("Current Leader: "+highest);

        // Set hand...
        for (int i=0; i<handSize; i++){
            if (current.getHand().length() > i) hand.get(i).setText(current.getHand().charAt(i)+"");
            else hand.get(i).setText("");
        }

        if (current.getClass().getName().equals("Player.Human")) humanTurn = true;
        else humanTurn = false;

        // Finally set bag number
        bagNumber.setText("Bag: "+bag.getNumberRemaining());

        playersTurn.setText("Player "+current.getId()+"'s turn");
        if (humanTurn) initializeBoard();
    }

    /**
     * Creates the board in the center of the GUI as a grid...
     */
    protected void initializeBoard(){
        virtualBoard = new ArrayList<JTextField>();
        BorderLayout layout = (BorderLayout) mainPanel.getLayout();
        if (layout.getLayoutComponent(BorderLayout.CENTER) != null){
            mainPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
        }
        centerPanel = new JPanel(new GridLayout(board.getDimension(), board.getDimension()));

        for (int i=0; i<board.getDimension(); i++){
            for (int j=0; j<board.getDimension(); j++){
                JTextField textField = new JTextField();
                textField.setHorizontalAlignment(JTextField.CENTER);
                textField.setFont(new Font(textField.getFont().getName(), Font.BOLD, textField.getFont().getSize()));
                if(!humanTurn && mode == Mode.GAME){
                    textField.setEnabled(false);
                }
                String bonus = board.getBonusAt(i, j);
                char letter = board.get(i, j);
                if (i == j && 1+(i*2) == board.getDimension() && letter == '_') textField.setText("*");
                else if (letter == '_'){
                    textField.setText(bonus);
                    if (board.getBonusAt(i, j) != "") textField.setForeground(Color.BLUE);
                }
                else{
                    if (mode == Mode.GAME){
                        textField.setEnabled(false);
                        Font defaultFont = UIManager.getDefaults().getFont("TextField.font");
                        textField.setFont(new Font(defaultFont.getFontName(), Font.BOLD, defaultFont.getSize()));
                    }
                    textField.setText("" + letter);
                    if (board.isBlank(i, j)){
                        textField.setDisabledTextColor(Color.RED);
                        textField.setForeground(Color.RED);
                    }
                }

                centerPanel.add(textField);

                textField.addFocusListener(new TileFocusListener(this));
                textField.addKeyListener(new BoardLetterTypedListener(this));
                textField.addMouseListener(new BoardMouseListener(this));
                ((AbstractDocument) textField.getDocument()).setDocumentFilter(new BoardFilter(this));
                textField.setTransferHandler(null);

                virtualBoard.add(textField);
            }
        }

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        revalidate();

    }

    /**
     * Initializes the southern panel for the hand and option buttons.
     *
     * @param handSize THe current hand size.
     */
    private void initializeHandPanel(int handSize){
        BorderLayout layout = (BorderLayout) mainPanel.getLayout();
        if (layout.getLayoutComponent(BorderLayout.SOUTH) != null){
            mainPanel.remove(layout.getLayoutComponent(BorderLayout.SOUTH));
        }
        southPanel = new JPanel(new BorderLayout());

        hand = new ArrayList<JTextField>();

        if (mode == Mode.CHEAT) initializeCheatHandPanel();
        else if (mode == Mode.GAME) initializeGameHandPanel();

        JPanel southNorth = new JPanel(new GridLayout(1, 2));
        ButtonGroup g = new ButtonGroup();
        horizontalButton = new JRadioButton("Horizontal");
        verticalButton = new JRadioButton("Vertical");
        horizontalButton.setMnemonic(KeyEvent.VK_H);
        verticalButton.setMnemonic(KeyEvent.VK_V);
        g.add(horizontalButton);
        g.add(verticalButton);
        southNorth.add(horizontalButton);
        southNorth.add(verticalButton);

        JPanel southCenter = new JPanel(new GridLayout(1, handSize));
        for (int i=0; i<handSize; i++){
            JTextField tile = new JTextField("");
            tile.setHorizontalAlignment(JTextField.CENTER);
            tile.setFont(new Font(tile.getFont().getName(), Font.BOLD,tile.getFont().getSize()));
            if (mode == Mode.CHEAT) tile.setEditable(true);
            else if (mode == Mode.GAME) tile.setEditable(false);

            tile.addFocusListener(new TileFocusListener(this));
            tile.addMouseListener(new HandMouseListener(this));
            tile.addKeyListener(new HandLetterTypedListener(this));
            ((AbstractDocument) tile.getDocument()).setDocumentFilter(new HandFilter());
            tile.setTransferHandler(null);

            hand.add(tile);
            southCenter.add(tile);
        }

        southPanel.add(southNorth, BorderLayout.NORTH);

        southPanel.add(southCenter, BorderLayout.CENTER);

        mainPanel.add(southPanel, BorderLayout.SOUTH);
        horizontalButton.setSelected(true);
        revalidate();
    }

    /**
     * Creates the buttons needed for when in Cheat Mode.
     */
    private void initializeCheatHandPanel(){
        clearBoardButton = new JButton("Clear Board");
        clearBoardButton.addActionListener(new ClearBoardActionListener(this));
        getBestMoveButton = new JButton("Best Move");
        getBestMoveButton.addActionListener(new BestMoveActionListener(this));

        southPanel.add(clearBoardButton, BorderLayout.WEST);
        southPanel.add(getBestMoveButton, BorderLayout.EAST);
    }

    /**
     * Creates the buttons needed for when a game is being played.
     */
    private void initializeGameHandPanel(){
        JPanel southLeft = new JPanel(new GridLayout(2, 1));
        shuffleButton = new JButton("Shuffle");
        shuffleButton.addActionListener(new ShuffleActionListener(this));
        exchangeButton = new JButton("Exchange");
        exchangeButton.addActionListener(new ExchangeActionListener(this));
        southLeft.add(shuffleButton);
        southLeft.add(exchangeButton);

        playButton = new JButton("Play");
        playButton.addActionListener(new PlayMoveActionListener(this));

        southPanel.add(southLeft, BorderLayout.WEST);
        southPanel.add(playButton, BorderLayout.EAST);

    }

    /**
     * Creates the panel to the north with score and bag information.
     */
    private void initializeNorthPanel(){
        removeNorthPanel();
        northPanel = new JPanel(new GridLayout(2, 2));
        leaderScore = new JLabel("There is no current leader.");
        currentScore = new JLabel("Your score: 0");
        bagNumber = new JLabel("Bag: "+bag.getNumberRemaining());
        playersTurn = new JLabel("No current player");
        northPanel.add(leaderScore);
        northPanel.add(bagNumber);
        northPanel.add(currentScore);
        northPanel.add(playersTurn);
        mainPanel.add(northPanel, BorderLayout.NORTH);
    }

    /**
     * Gets rid of the scores and bag info at the top of the screen when not playing.
     */
    private void removeNorthPanel(){
        BorderLayout layout = (BorderLayout) mainPanel.getLayout();
        if (layout.getLayoutComponent(BorderLayout.NORTH) != null){
            mainPanel.remove(layout.getLayoutComponent(BorderLayout.NORTH));
        }
    }

    /**
     * Allows toggling between the options that are available while a game is
     * being played and while the cheater is being used.  True for game, false
     * for cheating.
     *
     * @param bool True if the menus are to be set for playing, false if for cheating.
     */
    protected void setOptionsForGame(boolean bool){
        if (bool){
            usedBlank = false;
            canceled = false;
            humanTurn = true;
            currentFile = null;
            mode = Mode.GAME;
            openMenuItem.setEnabled(false);
            endMenuItem.setEnabled(true);
            setConfigFileItem.setEnabled(false);
            playerScoresItem.setEnabled(true);
            editMenu.setEnabled(false);
            bag.reset();
            initializeNorthPanel();
        }
        else{
            usedBlank = false;
            canceled = true;
            humanTurn = false;
            currentFile = null;
            mode = Mode.CHEAT;
            openMenuItem.setEnabled(true);
            endMenuItem.setEnabled(false);
            setConfigFileItem.setEnabled(true);
            playerScoresItem.setEnabled(false);
            editMenu.setEnabled(true);
            removeNorthPanel();
        }
        initializeBoard();
        initializeHandPanel(handSize);
        hand.get(0).requestFocusInWindow();
    }

    /**
     * Simply displays an option menu for confirming the selection.
     *
     * @param title The title of the menu.
     * @param message The message that will be confirmed.
     * @return True if "OK" was selected, false otherwise.
     */
    protected boolean confirmation(String title, String message){
        Object[] options = {"OK", "Cancel"};
        int result = JOptionPane.showOptionDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (result == 0) return true;
        return false;
    }

    /**
     * Saves the given file as a .brd or .svg depending on which mode we are in.
     *
     * @param saveFile The file to be saved.
     */
    protected void saveFile(File saveFile){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(saveFile));
            if (mode == Mode.CHEAT){
                SavedBoard toSave = new SavedBoard();
                toSave.hand = new ArrayList<Character>();
                toSave.board = new ArrayList<ArrayList<Character>>();
                toSave.blanks = new ArrayList<ArrayList<Boolean>>();
                for (int i=0; i<board.getDimension(); i++){
                    toSave.board.add(new ArrayList<Character>());
                    toSave.blanks.add(new ArrayList<Boolean>());
                    for (int j=0; j<board.getDimension(); j++){
                        toSave.board.get(i).add(board.get(i, j));
                        toSave.blanks.get(i).add(board.isBlank(i, j));
                        String virtual = virtualBoard.get(i*board.getDimension()+j).getText();
                        if (virtual.length() == 1 && !virtual.equals("*") && board.get(i, j) == '_'){
                            if (virtualBoard.get(i*board.getDimension()+j).getForeground() == Color.RED)
                                toSave.hand.add('_');
                            else toSave.hand.add(virtual.charAt(0));
                        }
                    }
                }
                for (int i=0; i<handSize; i++){
                    if (!hand.get(i).getText().equals("")){
                        toSave.hand.add(hand.get(i).getText().charAt(0));
                    }
                }
                out.writeObject(toSave);
                out.close();
            }
            else{
                SavedGame toSave = new SavedGame();
                toSave.board = new ArrayList<ArrayList<Character>>();
                toSave.blanks = new ArrayList<ArrayList<Boolean>>();
                for (int i=0; i<board.getDimension(); i++){
                    toSave.board.add(new ArrayList<Character>());
                    toSave.blanks.add(new ArrayList<Boolean>());
                    for (int j=0; j<board.getDimension(); j++){
                        toSave.board.get(i).add(board.get(i, j));
                        toSave.blanks.get(i).add(board.isBlank(i, j));
                    }
                }
                toSave.players = new ArrayList<SavedPlayer>();
                for (Player p : mediator.getPlayers()){
                    SavedPlayer savep = new SavedPlayer();
                    savep.hand = new ArrayList<Character>();
                    for (Character c : p.getHand().toCharArray()) savep.hand.add(c);
                    savep.id = p.getId();
                    savep.score = p.getScore();
                    savep.type = p.getClass().getName();
                    toSave.players.add(savep);
                }
                toSave.whoseTurn = mediator.getPlayers().indexOf(mediator.getCurrentPlayer());
                toSave.baggage = new ArrayList<Character>();
                while (!bag.isEmpty()) toSave.baggage.add(bag.getRandomTile());
                out.writeObject(toSave);
                out.close();
            }
            currentFile = saveFile;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "There was an error with saving...", "SAVE ERROR", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public boolean canceled(){return canceled;}

    /**
     * Validates as much of the configuration as it can to see if it is valid.
     *
     * @param config The file to be checked.
     * @return True if it is seemingly valid.
     */
    protected static boolean validateConfigFile(File config){
        try {
            Scanner scanner = new Scanner(config);
            // Check the first three lines for board size, hand size, and full hand bonus.
            Integer.parseInt(scanner.nextLine());
            Integer.parseInt(scanner.nextLine());
            Integer.parseInt(scanner.nextLine());


            // Next 27 lines should be letter scores and bag amounts.
            String next;
            for (int i=0; i<27; i++){
                if (!scanner.hasNext()) return false;
                next = scanner.nextLine();
                String[] s = next.split(" ");
                if (i == 26 && s[0].length() == 1 && s[0].toUpperCase().charAt(0) != '_'){
                    return false;
                }
                else if (s[0].length() > 1 || (s[0].length() == 1 && s[0].toUpperCase().charAt(0) != i+65 && i != 26)){
                    return false;
                }
                else if (s[0].length() == 0) return false;
                Integer.parseInt(s[1]);
                Integer.parseInt(s[2]);
            }

            // "--" should separate the next two sections.
            if (!scanner.nextLine().equals("--")) return false;
            // This section should deal with bonuses and their positions.
            while (!(next = scanner.nextLine()).equals("--")){
                if (!scanner.hasNext()) return false;
                String[] s = next.split(" ");
                Integer.parseInt(s[0]);
                Integer.parseInt(s[1]);
                String b = s[2].toUpperCase();
                if (!Board.getAllBonuses().contains(b)) return false;
            }

            // The rest is the dictionary, they can have whatever here.
            return true;

        } catch (FileNotFoundException e) {
            return false;
        }
        catch (NumberFormatException e){
            return false;
        }
        catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    /**
     * Actually changes the config file and edits the board and all other info.
     * It will catch if the default config doesn't exist and tell the user, limiting
     * capabilities until a config is set.
     *
     * @param configFile The string of the filename.
     */
    protected void setConfig(File configFile){
        GameMediator newGame = new GameMediator();
        GameMediator.configFile = configFile.getAbsolutePath();
        dispose();
        newGame.start();
    }

    /**
     * A string representation of the current hand tiles.
     *
     * @return The hand tiles in string form "ABCD" etc.
     */
    protected String getHandString(){
        String result = "";
        for (JTextField t : hand){
            result += t.getText();
        }
        return result;
    }

    protected void setMoveChoice(PlayerMove move){moveChoice = move;}
}
