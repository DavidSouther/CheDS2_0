/*
 * ChessPromotionDialog.java
 *
 * Created on December 11, 2005, 10:59 PM
 */
package com.davidsouther.chess;

import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author David Souther
 */
public class ChessPromotionDialog extends JDialog {
    private int choice = ChessPosition.QUEEN;
    private ChessDesktop desktop;

    /** Creates a new instance of ChessPromotionDialog */
    public ChessPromotionDialog(JFrame owner, ChessDesktop d) {
        super(owner, true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setTitle(ChessMainFrame.getString("promoteTitle"));
        desktop = d;
        this.initGui();
        pack();
    }

    public int getChoice() {
        return choice;
    }

    private void initGui() {
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.add(new JLabel(ChessMainFrame.getString("promoteText")));

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
        JButton b;

        b = new JButton();
        b.setIcon(desktop.whiteRook);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice = ChessPosition.ROOK;
                setVisible(false);
                System.out.println(this.toString());
            }
        });
        p.add(b);

        b = new JButton();
        b.setIcon(desktop.whiteKnight);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice = ChessPosition.KNIGHT;
                setVisible(false);
            }
        });
        p.add(b);

        b = new JButton();
        b.setIcon(desktop.whiteBishop);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice = ChessPosition.BISHOP;
                setVisible(false);
            }
        });
        p.add(b);

        b = new JButton();
        b.setIcon(desktop.whiteQueen);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choice = ChessPosition.QUEEN;
                setVisible(false);
            }
        });
        this.getRootPane().setDefaultButton(b);
        p.add(b);

        this.getContentPane().add(p);
    }
}
