/*
 * ChessMoveInternalFrame.java
 *
 * Created on November 1, 2005, 8:00 PM
 */
package com.davidsouther.chess;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.table.*;

/**
 *
 * @author David Souther
 */
public class ChessMoveInternalFrame extends JInternalFrame {
	private ChessController control;
	private ChessDesktop parent;
	private JPanel panel;

	private JTable history;

	private JLabel status;

	/** Creates a new instance of ChessMoveInternalFrame */
	public ChessMoveInternalFrame(ChessDesktop d, ChessController c) {
		super(ChessMainFrame.getString("movesTitle"), true, // resizable
				false, // closable
				false, // maximizable
				true); // iconifiable
		control = c;
		parent = d;

		initGui();
		pack();
		setLocation(475, 30);
		show();
	}

	public void updateHistory() {
		history.repaint();
	}

	public void setStatus(String s) {
		status.setText(s);
	}

	private void initGui() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(tableAbstract());
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		mainPanel.add(statusBar());
		mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
		this.getContentPane().add(mainPanel);
	}

	private JPanel tableAbstract() {
		JPanel p = new JPanel();
		history = new JTable(control.getHistory());
		JScrollPane s = new JScrollPane(history);
		history.setPreferredScrollableViewportSize(new Dimension(200, 300));
		p.add(s);
		return p;
	}

	private JPanel statusBar() {
		JPanel p = new JPanel();
		status = new JLabel(ChessMainFrame.getString("intro"));
		status.setFont(new Font("Times-Roman", Font.BOLD, 17));
		p.add(status);
		return p;
	}
}
