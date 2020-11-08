/*
 * ChessCapturesInternalFrame.java
 *
 * Created on November 2, 2005, 10:19 PM
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author David Souther
 */
public class ChessCapturesInternalFrame extends JInternalFrame {
    ChessController control;
    ChessDesktop parent;

    int[] whiteCaptured;
    int[] blackCaptured;
    int whites = 0, blacks = 0;

    ImageIcon whitePawn, whiteRook, whiteKnight, whiteBishop, whiteQueen, whiteKing;
    ImageIcon blackPawn, blackRook, blackKnight, blackBishop, blackQueen, blackKing;

    JLabel[][] caps;

    /** Creates a new instance of ChessCapturesInternalFrame */
    public ChessCapturesInternalFrame(ChessDesktop d, ChessController c) {
        super(ChessMainFrame.getString("capturesTitle"), false, // resizable
                false, // closable
                false, // maximizable
                true); // iconifiable

        control = c;
        parent = d;
        setLocation(30, 500);
        whiteCaptured = new int[16];
        blackCaptured = new int[16];

        initGui();
        pack();
        show();
    }

    private void initGui() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 16));
        p.setBackground(Color.WHITE);
        caps = new JLabel[2][16];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 16; j++) {
                caps[i][j] = createColoredLabel(Color.WHITE, Color.BLACK);
                caps[i][j].setPreferredSize(new Dimension(50, 50));
                p.add(caps[i][j]);
            }
        }
        JScrollPane sp = new JScrollPane(p);
        sp.setPreferredSize(new Dimension(600, 120));
        getContentPane().add(sp);
    }

    public void updateCaptures(int piece) {
        // System.out.println("Updating with " + piece);
        if (piece < 0) {
            blackCaptured[blacks] = -piece;
            blacks++;
            updateBlackSquares();
        } else {
            whiteCaptured[whites] = piece;
            whites++;
            updateWhiteSquares();
        }
        repaint();
    }

    public void printCaptures() {
        int i;
        System.out.println("White captures:");
        for (i = 0; i < whiteCaptured.length; i++) {
            System.out.print(whiteCaptured[i] + ", ");
        }
        System.out.println("");

        System.out.println("Black captures:");
        for (i = 0; i < blackCaptured.length; i++) {
            System.out.print(blackCaptured[i] + ", ");
        }
        System.out.println("");
    }

    private void updateBlackSquares() {
        // System.out.println("Updateing black");
        Arrays.sort(blackCaptured);
        int count = -1;
        for (int i = 0; i < 16; i++) {
            if (blackCaptured[i] == ChessPosition.BLANK)
                continue;
            count++;
            caps[1][count].setIcon(parent.pp(-blackCaptured[i]));
            i++;
        }
    }

    private void updateWhiteSquares() {
        // System.out.println("Updating white...");
        Arrays.sort(whiteCaptured);
        int count = -1;
        for (int i = 0; i < 16; i++) {
            System.out.println(i);
            if (whiteCaptured[i] == ChessPosition.BLANK)
                continue;
            count++;
            caps[0][count].setIcon(parent.pp(whiteCaptured[i]));
        }
    }

    private JLabel createColoredLabel(Color bcolor, Color fcolor) {
        JLabel label = new JLabel();
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(bcolor);
        label.setForeground(fcolor);
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        return label;
    }
}
