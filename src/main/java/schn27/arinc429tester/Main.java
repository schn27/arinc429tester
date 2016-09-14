package schn27.arinc429tester;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import schn27.arinc429tester.ui.MainFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	public static void main(String args[]) {
		setupLogger();
		setLookAndFeel();
		MainFrame mainFrame = new MainFrame();
		java.awt.EventQueue.invokeLater(() -> {mainFrame.setVisible(true);});
	}

	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3].getClassName());
		} catch (UnsupportedLookAndFeelException | ArrayIndexOutOfBoundsException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
		}
	}
	
	private static void setupLogger() {
		try {
			LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));
		} catch (IOException | SecurityException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
