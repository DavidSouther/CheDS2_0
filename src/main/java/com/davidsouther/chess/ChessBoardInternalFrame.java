/*
 * ChessBoardInternalFrame.java
 *
 * Created on November 1, 2005, 3:43 PM
 */
package com.davidsouther.chess;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author David Souther
 */
public class ChessBoardInternalFrame extends /* JPanel */ JInternalFrame implements MouseMotionListener, MouseListener {
    ChessController controller;
    ChessDesktop parent;

    private JLayeredPane layeredPane;
    private JLabel mover;
    private JPanel boardPanel;
    private JLabel[][] board;

    private int[] location;
    private boolean dragging = false;
    private ChessMove dragMove;

    /** Creates a new instance of ChessBoardInternalFrame */
    public ChessBoardInternalFrame(ChessDesktop d, ChessController c) {
        super(ChessMainFrame.getString("boardTitle"), false, // resizable
                false, // closable
                false, // maximizable
                false); // iconifiable

        controller = c;
        parent = d;
        initGui();

        pack();
        setLocation(5, 5);
        show();
    }

    public void updateBoard(ChessPosition p) {
        ChessPosition pos = p;
        int[] b = pos.board;
        int count = -1;
        for (int col = 92; col >= 22; col -= 10) {
            count++;
            for (int ii = 0; ii < 8; ii++) {
                int i = ii + col;
                board[count][ii + 1].setText(null);
                if (b[i] != 0) {
                    board[count][ii + 1].setIcon(parent.pp(b[i]) /* + "\n" + i */);
                } else {
                    board[count][ii + 1].setIcon(null /* + i */);
                }
            }
        }
    }

    private void initGui() {
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(450, 450));
        layeredPane.addMouseListener(this);
        layeredPane.addMouseMotionListener(this);
        layeredPane.setLayout(null);

        initBoardLabels();

        getContentPane().add(layeredPane);
    }

    private void initBoardLabels() {
        int count = 0;

        board = new JLabel[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                count++;
                if (count % 2 == 0) {
                    board[i][j] = createColoredLabel(Color.WHITE, Color.BLACK);
                } else {
                    board[i][j] = createColoredLabel(Color.GRAY, Color.WHITE);
                }
                // board[i][j].setText("" + i + j);
                board[i][j].setPreferredSize(new Dimension(50, 50));
            }
        }

        board[8][0] = new JLabel(" ");
        count = 8;
        for (int i = 0; i < 8; i++) {
            String k;
            String l = String.valueOf(count--);
            switch (i + 1) {
                case 1:
                    k = "A";
                    break;
                case 2:
                    k = "B";
                    break;
                case 3:
                    k = "C";
                    break;
                case 4:
                    k = "D";
                    break;
                case 5:
                    k = "E";
                    break;
                case 6:
                    k = "F";
                    break;
                case 7:
                    k = "G";
                    break;
                case 8:
                    k = "H";
                    break;
                default:
                    k = "Err";
                    break;
            }
            board[8][i + 1] = new JLabel(k);
            board[8][i + 1].setHorizontalTextPosition(JLabel.CENTER);
            board[i][0] = new JLabel(l);
            board[8][i + 1].setHorizontalTextPosition(JLabel.CENTER);
        }

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(0, 9));
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boardPanel.add(board[i][j]);
            }
        }
        boardPanel.setBounds(0, 0, 450, 450);

        mover = new JLabel();
        mover.setBounds(0, 0, 50, 50);
        layeredPane.add(mover);
        layeredPane.add(boardPanel);
    }

    // Create and set up a colored label.
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

    private JLabel createFiller() {
        return new JLabel();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        mover.setLocation(e.getX() - mover.getWidth() / 2, e.getY() - mover.getHeight() / 2);
    }

    public void mousePressed(MouseEvent e) {
        // check bounds
        if (e.getX() >= 50 && e.getY() >= 0 && e.getX() <= 450 && e.getY() <= 400) {
            if (!controller.playing) {
                dragMove = new ChessMove();
                dragMove.from = getPositionSquare(e);
                location = getBoardSquare(e);
                mover.setIcon(board[location[0]][location[1]].getIcon());
                board[location[0]][location[1]].setIcon(null);
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        mover.setLocation(e.getX() - mover.getWidth() / 2, e.getY() - mover.getHeight() / 2);
    }

    public void mouseReleased(MouseEvent e) {
        // check bounds
        if (e.getX() >= 50 && e.getY() >= 0 && e.getX() <= 450 && e.getY() <= 400) {
            if (!controller.playing) {
                dragMove.to = getPositionSquare(e);
                if (controller.isValidMove(dragMove)) {
                    location = getBoardSquare(e);
                    board[location[0]][location[1]].setIcon(mover.getIcon());
                    mover.setIcon(null);
                    controller.makeMove(dragMove);
                } else {
                    board[location[0]][location[1]].setIcon(mover.getIcon());
                    mover.setIcon(null);
                }
            }
        }
    }

    private int[] getBoardSquare(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int x2 = x - (x % 50);
        x2 /= 50;
        int y2 = y - (y % 50);
        y2 /= 50;
        y++;
        int[] ret = { y2, x2 };
        return ret;
    }

    private int getPositionSquare(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int x1 = x - (x % 50);
        x1 /= 50;
        x1++;
        int y1 = y - (y % 50);
        y1 /= 50;
        y1++;
        y1 = 10 - y1;
        int positionVal = Integer.parseInt("" + y1 + x1);

        return positionVal;
    }
}
