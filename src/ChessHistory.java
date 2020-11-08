/*
 * ChessHistory.java
 *
 * Created on November 13, 2005, 5:28 PM
 */

import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author David Souther
 */
public class ChessHistory extends AbstractTableModel implements java.io.Serializable {
    private ArrayList whiteMoves;
    private ArrayList blackMoves;

    private final boolean BLACK = true;
    private final boolean WHITE = false;
    private boolean side = WHITE;
    private int move = 0;
    private int whites = 0;
    private int blacks = 0;

    /** Creates a new instance of ChessHistory */
    public ChessHistory() {
        whiteMoves = new ArrayList(30);
        blackMoves = new ArrayList(30);
    }

    public void addMove(ChessMove m) {
        move++;
        boolean p;
        if (move % 2 == 1) {
            addWhiteMove(m);
            p = Chess.HUMAN;
        } else {
            p = Chess.PROGRAM;
            addBlackMove(m);
        }
        this.fireTableRowsInserted(whites, whites);
    }

    public ChessMove getWhiteMove(int n) {
        return (ChessMove) whiteMoves.get(n);
    }

    public ChessMove getBlackMove(int n) {
        return (ChessMove) blackMoves.get(n);
    }

    public int getLength() {
        return (int) (move - 1) / 2;
    }

    private void addWhiteMove(ChessMove m) {
        whiteMoves.add(whites, m);
        whites++;
    }

    private void addBlackMove(ChessMove m) {
        blackMoves.add(blacks, m);
        blacks++;
    }

    private void remove(int n) {
        if (n % 2 == 1) {
            whiteMoves.remove(n / 2);
        } else {
            blackMoves.remove((n - 1) / 2);
        }
    }

    private ChessMove getMove(int n) {
        if (n % 2 == 1) {
            return getWhiteMove(n / 2);
        } else {
            return getBlackMove((n - 1) / 2);
        }
    }

    public static ChessMove findMove(ChessPosition p1, ChessPosition p2) {
        ChessMove m = new ChessMove();
        for (int i = 22; i < 100; i++) {
            if (p1.board[i] == 7)
                continue;
            if (p1.board[i] != p2.board[i]) {
                if (p2.board[i] == ChessPosition.BLANK) {
                    m.from = i;
                } else {
                    m.to = i;
                }
            }
        }
        return m;
    }

    public static ChessMove reverseMove(ChessMove m) {
        ChessMove t = new ChessMove();
        t.from = m.to;
        t.to = m.from;
        return t;
    }

    public static int capturedPiece(ChessPosition p1, ChessPosition p2) {
        for (int i = 0; i < 120; i++) {
            if (p1.board[i] != p2.board[i]) {
                if (p2.board[i] != ChessPosition.BLANK) {
                    return p1.board[i];
                }
            }
        }
        return ChessPosition.BLANK;
    }

    public void printHistory() {
        int q = 0, w = 0;
        System.out.println("History:");
        System.out.println("White" + "\t" + "Black");
        for (int i = 0; i < move; i++) {
            System.out.println(getWhiteMove(w) + "\t" + getBlackMove(q));
        }
        System.out.println("");
    }

    // ATM
    public String getColumnName(int col) {
        if (col == 0) {
            return ChessMainFrame.getString("white");
        } else {
            return ChessMainFrame.getString("black");
        }
    }

    public int getRowCount() {
        return (int) (move - move % 2) / 2;
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return getWhiteMove(row);
        } else {
            return getBlackMove(row);
        }
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
    }
}
