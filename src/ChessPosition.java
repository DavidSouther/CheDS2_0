
import java.io.*;

public class ChessPosition extends Position implements Serializable /* throws Exception */ {
    final static public int BLANK = 0;
    final static public int HUMAN = 1;
    final static public int PROGRAM = -1;
    final static public int PAWN = 1;
    final static public int KNIGHT = 2;
    final static public int BISHOP = 3;
    final static public int ROOK = 4;
    final static public int QUEEN = 5;
    final static public int KING = 9;
    int[] board = new int[120];

    public String toString() {
        Chess c = new Chess();
        StringBuffer sb = new StringBuffer();
        sb.append(c.positionEvaluation(this, Chess.PROGRAM) + ":");
        sb.append("" + c.check(this, Chess.HUMAN) + c.checkMate(this, Chess.HUMAN) + "/" + c.check(this, Chess.PROGRAM)
                + c.checkMate(this, Chess.PROGRAM) + ":");
        sb.append("[");
        for (int i = 22; i < 100; i++) {
            sb.append("" + (i) + ":" + board[i] + ",");
        }
        sb.append("]");
        c = null;
        return sb.toString();
    }

    public void setValue(int piece, int index) {
        board[index] = piece;
    }

    public String printPretty() {
        int[] temp = board;
        int ii;
        StringBuffer sb = new StringBuffer("{ ");
        for (int row = 0; row <= 110; row += 10) {
            sb.append("\n");
            for (int col = 0; col < 10; col++) {
                ii = col + row;
                sb.append(pp(temp[ii]));
                if (ii != 119) {
                    sb.append(", ");
                } else {
                    sb.append("\n");
                }
            }
        }
        sb.append("};");
        return sb.toString();
    }

    /*
     * private String pp(int piece) { if (piece == 0) return "ChessPosition.BLANK";
     * String color; if (piece < 0) color = "-"; else color = ""; int p = piece; if
     * (p < 0) p = -p; switch (p) { case PAWN: return "" + color +
     * "ChessPosition.PAWN"; case KNIGHT: return "" + color +
     * "ChessPosition.KNIGHT"; case BISHOP: return "" + color +
     * "ChessPosition.BISHOP"; case ROOK: return "" + color + "ChessPosition.ROOK";
     * case QUEEN: return "" + color + "ChessPosition.QUEEN"; case KING: return "" +
     * color + "ChessPosition.KING"; } return "7"; }
     */

    public static String pp(int piece) {
        if (piece == 0)
            return "ChessPosition.BLANK";
        String color;
        if (piece < 0)
            color = "-";
        else
            color = "";
        int p = piece;
        if (p < 0)
            p = -p;
        switch (p) {
            case PAWN:
                return "" + color + "ChessPosition.PAWN";
            case KNIGHT:
                return "" + color + "ChessPosition.KNIGHT";
            case BISHOP:
                return "" + color + "ChessPosition.BISHOP";
            case ROOK:
                return "" + color + "ChessPosition.ROOK";
            case QUEEN:
                return "" + color + "ChessPosition.QUEEN";
            case KING:
                return "" + color + "ChessPosition.KING";
        }
        return "7";
    }

    /**
     * Finds a chess move FROM this position TO another position.
     *
     * Use: Player makes move, this finds computer's move FROM player's move
     * position
     *
     * @param p ChessPosition TO
     */
}
