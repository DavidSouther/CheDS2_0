/*
 * ChessDiffInternalFrame.java
 *
 * Created on November 2, 2005, 5:55 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author David Souther
 */
public class ChessDiffInternalFrame extends JInternalFrame implements ActionListener {
	ChessController control;

	JSlider difficulty;
	JButton set;

	/** Creates a new instance of ChessDiffInternalFrame */
	public ChessDiffInternalFrame(ChessController c) {
		control = c;
		initGui();
		pack();
	}

	public void initGui() {
		difficulty = new JSlider(25, 100, 75);
		difficulty.setMajorTickSpacing(25);
		difficulty.setMinorTickSpacing(5);
		difficulty.setPaintTicks(true);
		difficulty.setPaintLabels(true);

		set = new JButton(ChessMainFrame.getString("difficultyLabel"));
		set.addActionListener(this);

		JPanel j = new JPanel();
		j.add(difficulty);
		j.add(set);

		getContentPane().add(j);
	}

	public void setValue(float val) {
		difficulty.setValue((int) val);
	}

	public void actionPerformed(ActionEvent e) {
		control.setDifficulty((float) difficulty.getValue());
	}
}
