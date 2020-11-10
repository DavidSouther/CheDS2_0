/*
 * ChessDesktop.java
 *
 * Created on November 1, 2005, 3:15 PM
 */
package com.davidsouther.chess;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;

/**
 *
 * @author David Souther
 */
public class ChessDesktop {
    ChessController owner;
    JFrame window;
    JDesktopPane desktop;

    ChessBoardInternalFrame board;
    ChessMoveInternalFrame moveF;
    // ChessWaitInternalFrame waiter;
    ChessDiffInternalFrame diff;
    ChessCapturesInternalFrame captures;

    public ImageIcon whitePawn, whiteRook, whiteKnight, whiteBishop, whiteQueen, whiteKing;
    public ImageIcon blackPawn, blackRook, blackKnight, blackBishop, blackQueen, blackKing;

    /** Creates a new instance of ChessDesktop */
    public ChessDesktop(ChessController owner) {
        this.owner = owner;
        window = owner.getFrame();
        desktop = new JDesktopPane();
        desktop.setLayout(null);
        // waiter = new ChessWaitInternalFrame();
        diff = new ChessDiffInternalFrame(owner);
        // desktop.add(waiter);
        desktop.add(diff);
        buildGraphics();
    }

    public JDesktopPane getDesktopPane() {
        return desktop;
    }

    public void newGame() {
        if (board != null) {
            board.setVisible(false);
            moveF.setVisible(false);
            captures.setVisible(false);
            desktop.remove(board);
            desktop.remove(moveF);
            desktop.remove(captures);
        }
        board = new ChessBoardInternalFrame(this, owner);
        moveF = new ChessMoveInternalFrame(this, owner);
        captures = new ChessCapturesInternalFrame(this, owner);
        desktop.add(board);
        desktop.add(moveF);
        desktop.add(captures);
    }

    public void updateBoard(ChessPosition p) {
        if (board == null) {
            board = new ChessBoardInternalFrame(this, owner);
            moveF = new ChessMoveInternalFrame(this, owner);
            desktop.add(board);
            desktop.add(moveF);
            pack();
        }
        board.updateBoard(p);
    }

    public void updateHistory() {
        moveF.updateHistory();
    }

    public void setStatus(String s) {
        moveF.setStatus(s);
    }

    public void waits() {
        // waiter.setVisible(true);
    }

    public void finishWait() {
        // waiter.setVisible(false);
    }

    public void showDiffFrame(float d) {
        diff.setValue(d);
        diff.setVisible(true);
    }

    public void clearDiffFrame() {
        diff.setVisible(false);
    }

    public void addCapturedPiece(int piece) {
        captures.updateCaptures(piece);
    }

    public void printCaptures() {
        captures.printCaptures();
    }

    public void humanWon() {
        JOptionPane.showMessageDialog(window, ChessMainFrame.getString("wonA"), ChessMainFrame.getString("wonB"),
                JOptionPane.WARNING_MESSAGE);
    }

    public void programWon() {
        JOptionPane.showMessageDialog(window, ChessMainFrame.getString("lostA"), ChessMainFrame.getString("lostB"),
                JOptionPane.ERROR_MESSAGE);
    }

    public void staleMate() {
        JOptionPane.showMessageDialog(window, ChessMainFrame.getString("stalemateA"),
                ChessMainFrame.getString("stalemateB"), JOptionPane.ERROR_MESSAGE);
    }

    /* soon to go away */ public void edit(ChessPosition p) {
    }

    public void updateCapturedPieces(int i) {
        captures.updateCaptures(i);
    }

    public ImageIcon pp(int piece) {
        if (piece == 0)
            return null;
        switch (piece) {
            case -ChessPosition.KING:
                return blackKing;
            case -ChessPosition.QUEEN:
                return blackQueen;
            case -ChessPosition.BISHOP:
                return blackBishop;
            case -ChessPosition.KNIGHT:
                return blackKnight;
            case -ChessPosition.ROOK:
                return blackRook;
            case -ChessPosition.PAWN:
                return blackPawn;
            case ChessPosition.PAWN:
                return whitePawn;
            case ChessPosition.ROOK:
                return whiteRook;
            case ChessPosition.KNIGHT:
                return whiteKnight;
            case ChessPosition.BISHOP:
                return whiteBishop;
            case ChessPosition.QUEEN:
                return whiteQueen;
            case ChessPosition.KING:
                return whiteKing;
            default:
                return null;
        }
    }

    private void pack() {
        Rectangle b = board.getBounds();
        Rectangle m = moveF.getBounds();
        moveF.setLocation((int) b.getWidth() + 10, 1);
        board.show();
        moveF.show();
    }

    private void buildGraphics() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        String f = ChessMainFrame.getString("imagesPath"), s = "/", e = ".png", c, p = "pawn", r = "rook", n = "knight",
                b = "bishop", q = "queen", k = "king";

        /*
         * c = "white"; whitePawn = new ImageIcon(ChessDesktop.class.getResource(f + s +
         * c + s + p + e)); whiteRook = new ImageIcon(ChessDesktop.class.getResource(f +
         * s + c + s + r + e)); whiteKnight = new
         * ImageIcon(ChessDesktop.class.getResource(f + s + c + s + n + e)); whiteBishop
         * = new ImageIcon(ChessDesktop.class.getResource(f + s + c + s + b + e));
         * whiteQueen = new ImageIcon(ChessDesktop.class.getResource(f + s + c + s + q +
         * e)); whiteKing = new ImageIcon(ChessDesktop.class.getResource(f + s + c + s +
         * k + e));
         * 
         * c = "black"; blackPawn = new ImageIcon(ChessDesktop.class.getResource(f + s +
         * c + s + p + e)); blackRook = new ImageIcon(ChessDesktop.class.getResource(f +
         * s + c + s + r + e)); blackKnight = new
         * ImageIcon(ChessDesktop.class.getResource(f + s + c + s + n + e)); blackBishop
         * = new ImageIcon(ChessDesktop.class.getResource(f + s + c + s + b + e));
         * blackQueen = new ImageIcon(ChessDesktop.class.getResource(f + s + c + s + q +
         * e)); blackKing = new ImageIcon(ChessDesktop.class.getResource(f + s + c + s +
         * k + e));
         */

        c = "white";
        whitePawn = createImageIcon(f + s + c + s + p + e);
        whiteRook = createImageIcon(f + s + c + s + r + e);
        whiteKnight = createImageIcon(f + s + c + s + n + e);
        whiteBishop = createImageIcon(f + s + c + s + b + e);
        whiteQueen = createImageIcon(f + s + c + s + q + e);
        whiteKing = createImageIcon(f + s + c + s + k + e);

        c = "black";
        blackPawn = createImageIcon(f + s + c + s + p + e);
        blackRook = createImageIcon(f + s + c + s + r + e);
        blackKnight = createImageIcon(f + s + c + s + n + e);
        blackBishop = createImageIcon(f + s + c + s + b + e);
        blackQueen = createImageIcon(f + s + c + s + q + e);
        blackKing = createImageIcon(f + s + c + s + k + e);
    }

    protected static ImageIcon createImageIcon(String path) {
        int MAX_IMAGE_SIZE = 75000; // Change this to the size of
                                    // your biggest image, in bytes.
        int count = 0;
        BufferedInputStream imgStream = new BufferedInputStream(ChessDesktop.class.getResourceAsStream(path));
        if (imgStream != null) {
            byte buf[] = new byte[MAX_IMAGE_SIZE];
            try {
                count = imgStream.read(buf);
            } catch (IOException ieo) {
                System.err.println("Couldn't read stream from file: " + path);
            }

            try {
                imgStream.close();
            } catch (IOException ieo) {
                System.err.println("Can't close file " + path);
            }

            if (count <= 0) {
                System.err.println("Empty file: " + path);
                return null;
            }
            return new ImageIcon(Toolkit.getDefaultToolkit().createImage(buf));
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
