package GUI;

import Game.Bag;
import Game.Board;
import Game.GameMediator;
import Game.Validator;
import Util.Dictionary;
import Util.IllegalConfigException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 6/9/13
 * Time: 3:25 PM
 * This class provides a simple loading bar for the user to see that the
 * program is starting up.
 */
public class SetUpWindow extends JFrame implements PropertyChangeListener{

    GameMediator mediator;
    JLabel label;
    JProgressBar bar;
    Loader loader;
    SetUp task;

    public SetUpWindow(GameMediator mediator){
        this.mediator = mediator;

        JPanel main = new JPanel(new BorderLayout());

        bar = new JProgressBar(0, 100);
        bar.setValue(0);
        bar.setStringPainted(true);

        main.add(bar, BorderLayout.NORTH);

        label = new JLabel("Initializing...");

        main.add(label, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                loader.cancel(true);
                task.cancel(true);
            }
        });

        add(main);
        setBounds(new Rectangle(0, 0, 430, 70));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Loading...");
        setVisible(true);

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        loader = new Loader();
        loader.addPropertyChangeListener(this);
        loader.execute();

        task = new SetUp();
        task.execute();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            bar.setValue(progress);
        }
    }

    private class Loader extends SwingWorker<Object, Object> {

        private int progress;

        @Override
        protected Object doInBackground() throws Exception {
            progress = 0;
            setProgress(progress);
            while (progress < 100){
                if (progress == 4){
                    label.setText("Parsing Configuration File.");
                }
                else if (progress == 10){
                    label.setText("Parsing Configuration File: Loading into Dictionary.");
                }
                else if (progress == 18){
                    label.setText("Filling Dictionary: Traversing Reverse-Directed Acyclic Graph");
                }
                else if (progress == 45){
                    label.setText("Finalizing Dictionary: Loading Complete GADDAG.");
                }
                else if (progress == 50){
                    label.setText("Finalizing Dictionary.");
                }
                else if (progress == 60){
                    label.setText("Developing rule set.");
                }
                else if (progress == 62){
                    label.setText("Developing rule set: Creating Validator");
                }
                else if (progress == 68){
                    label.setText("Creating Board.");
                }
                else if (progress == 72){
                    label.setText("Filling Bag.");
                }
                else if (progress == 75){
                    label.setText("Readying Graphical User Interface...");
                }

                System.out.println("before try sleep");
                try{
                    Thread.sleep(40);
                } catch (InterruptedException e) {return null;}
                System.out.println("Adding 1");
                progress += 1;
                setProgress(progress);
            }
            return null;
        }

        public void cancel(){
            progress = 100;
        }


        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null); //turn off the wait cursor
            if (task.isDone()) SetUpWindow.this.dispose();
        }
    }

    private class SetUp extends SwingWorker<Object, Object> {

        @Override
        protected Object doInBackground() throws Exception {
            try{ // Try to initialize
                if (!MainFrame.validateConfigFile(new File(GameMediator.configFile))) throw new IllegalConfigException();
                Dictionary dictionary = new Dictionary();
                Validator validator = new Validator(dictionary);
                Board board = new Board(validator);
                Bag bag = new Bag();
                new MainFrame(mediator, board, dictionary, validator, bag);
                SetUpWindow.this.dispose();
                return null;
            }catch (FileNotFoundException e){
                configError();return null;}
            catch (IllegalConfigException e){
                configError();return null;}
            catch (Exception e){e.printStackTrace();return null;}
        }

        private void configError(){
            loader.cancel(true);
            JOptionPane.showMessageDialog(SetUpWindow.this, "The default configuration file" +
                    " either could not be found or was invalid, please choose a proper" +
                    " configuration.\n(Config files are stored in the Configurations folder)"
                    , "NO CONFIGURATION SET", JOptionPane.ERROR_MESSAGE);
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Config Files", "cfg");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(SetUpWindow.this);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                GameMediator.configFile = chooser.getSelectedFile().getAbsolutePath();
                new SetUpWindow(mediator);
            }
            SetUpWindow.this.dispose();
        }

        @Override
        protected void done(){
            try{
                // This will catch any exceptions from doInBackground().
                super.get();
                loader.cancel();

            } catch(Throwable t){}
        }
    }
}