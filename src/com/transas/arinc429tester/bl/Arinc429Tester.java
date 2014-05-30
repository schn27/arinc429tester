package com.transas.arinc429tester.bl;

import com.transas.arinc429tester.ui.MainFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Arinc429Tester {
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
