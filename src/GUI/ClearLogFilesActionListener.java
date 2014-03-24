package GUI;

import Game.GameMediator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: keegan
 * Date: 3/24/14
 * Time: 1:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClearLogFilesActionListener implements ActionListener {
    private MainFrame parent;

    public ClearLogFilesActionListener(MainFrame mainFrame) {
        parent = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (parent.confirmation("CLEAR LOGS", "This will clear all log files.")) {
            for(File file: new File(GameMediator.path+"/Logs").listFiles()) if (!file.getName().contains("db")) file.delete();
        }
    }
}
