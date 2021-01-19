/*
 * ChessMenuBar.java
 *
 * Created on November 1, 2005, 1:53 PM
 */
package com.davidsouther.chess;

import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author David Souther
 */
public class ChessMenuBar implements ActionListener {
	ChessController owner;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem item;

	/** Creates a new instance of ChessMenuBar */
	public ChessMenuBar(ChessController control) {
		owner = control;
		initMenu();
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}

	public void actionPerformed(ActionEvent ae) {
		String command = ae.getActionCommand();

		if (command.equals(ChessMainFrame.getString("menuNew"))) {
			owner.newGame();
		} else if (command.equals(ChessMainFrame.getString("menuDifficulty"))) {
			owner.changeDifficulty();
		} else if (command.equals(ChessMainFrame.getString("menuSave"))) {
			owner.save();
		} else if (command.equals(ChessMainFrame.getString("menuLoad"))) {
			owner.load();
		} else if (command.equals(ChessMainFrame.getString("menuQuit"))) {
			int n = JOptionPane.showConfirmDialog(owner.getFrame(), ChessMainFrame.getString("confirmQuit"),
					ChessMainFrame.getString("menuQuit"), JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				System.exit(0);
			}
		} else if (command.equals("Edit current board")) {
			owner.editCurrent();
		} else if (command.equals("Edit new board")) {
			owner.editNew();
		} else if (command.equals("Print Board")) {
			owner.printBoard();
		} else if (command.equals("Print Control Data")) {
			owner.printControlData();
		}
	}

	private void initMenu() {
		menuBar = new JMenuBar();

		menu = new JMenu(ChessMainFrame.getString("fileMenu"));
		menuBar.add(menu);
		item = new JMenuItem(ChessMainFrame.getString("menuNew"));
		item.addActionListener(this);
		menu.add(item);

		item = new JMenuItem(ChessMainFrame.getString("menuDifficulty"));
		item.addActionListener(this);
		menu.add(item);

		item = new JMenuItem(ChessMainFrame.getString("menuSave"));
		item.addActionListener(this);
		menu.add(item);
		// item.setEnabled(false);

		item = new JMenuItem(ChessMainFrame.getString("menuLoad"));
		item.addActionListener(this);
		menu.add(item);
		// item.setEnabled(false);

		item = new JMenuItem(ChessMainFrame.getString("menuQuit"));
		item.addActionListener(this);
		menu.add(item);

		menu = new JMenu("Edit");
		menuBar.add(menu);
		item = new JMenuItem("Edit current board");
		item.addActionListener(this);
		// menu.add(item);

		item = new JMenuItem("Edit new board");
		item.addActionListener(this);
		// menu.add(item);

		item = new JMenuItem("Print Board");
		item.addActionListener(this);
		menu.add(item);

		item = new JMenuItem("Print Control Data");
		item.addActionListener(this);
		menu.add(item);
	}

}
