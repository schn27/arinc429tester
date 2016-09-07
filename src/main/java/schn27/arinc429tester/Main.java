package schn27.arinc429tester;

import schn27.arinc429tester.ui.MainFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	public static void main(String args[]) {
		setLookAndFeel();
		MainFrame mainFrame = new MainFrame();
		java.awt.EventQueue.invokeLater(() -> {mainFrame.setVisible(true);});
	}

	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1].getClassName());
		} catch (UnsupportedLookAndFeelException | ArrayIndexOutOfBoundsException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
		}
	}
}
