 

import java.util.*;
import java.io.*;

public class Chess extends GameSearch {

    /**
     *  Notes:  PROGRAM false -1,  HUMAN true 1
     */

    /**
     *                PUBLIC API (mostly fromGameSearch)
     */
    
     /*
      * Decides if Position p is a stalemate.
      * If all possible moves from p are checks
      * but p is not check, then returns true.
      *
      * @param Position p Position to be searched
      */
    public boolean drawnPosition(Position p) {
        ArrayList pp;
        if(!check((ChessPosition)p, Chess.HUMAN)){
                pp = possibleMoves(p, Chess.HUMAN);
                if(pp.size() == 0)return true;
        } else if(!check((ChessPosition)p, Chess.PROGRAM)){
                pp = possibleMoves(p, Chess.PROGRAM);
                if(pp.size() == 0)return true;
        }
        return false;
    }

    /*
     * Determins if Position p is won for Player player.
     * simply a helper method from game search.
     * 
     * Warning: checkMate determines whether Player player
     * is mated, this determines if player has won.
     *
     * @param Position p Position to be considered
     * @param boolean player Winning player
     */
    public boolean wonPosition(Position p, boolean player) {
       if(checkMate((ChessPosition)p, !player)) return true;
       return false;
    }
	
    /*
     * Determines if player's king is attacked at this
     * position.
     * Used extensivly by checkMate and findPossibleMoves
     *
     * @param ChessPosition p ChessPosition to be considered
     * @param boolean player player who is checked
     */
    public boolean check(ChessPosition p, boolean player){
            int i = findKing(p, player);
            if(isReallyAttacked(p, i)){	
                    return true;
            }
            return false;
    }
	
    /*
     * Determines if player has no possible legal moves at
     * ChessPosition p.
     *
     * @param ChessPosition p ChessPosition to be considered.
     * @param boolean player Player who has no more moves
     */
    public boolean checkMate(ChessPosition p, boolean player){
        if(!check(p, player)) return false;
        ArrayList ps = possibleMoves(p, player);
        for(int i=0; i<ps.size(); i++){
                if(!check((ChessPosition)ps.get(i), player))return false;
        }
        return true;
    }
  
    public static float staticPositionEvaluation(Position p, boolean player){
        Chess c = new Chess();
        return c.positionEvaluation(p, player);
    }
    /*
     * Returns float value of Position p for player
     * Combines material data, control data, attack data,
     * and center control.
     *
     * @param Position p Position to be evaluated
     * @param boolean player to Play
     */
    public float positionEvaluation(Position p, boolean player) {
        ChessPosition pos = (ChessPosition)p;
        int [] b = pos.board;
        float ret = 0.0f;
        // adjust for material:
        for (int i=22; i<100; i++) {
            if (b[i] != 0 && b[i] != 7)  ret += value[Math.abs(b[i])] * 2;
        }
    
        // adjust for positional advantages:
        setControlData(pos);
        int control = 0;
        for (int i=22; i<100; i++) {
            control += humanControl[i];
            control -= computerControl[i];
        }
        // Count center squares extra:
        control += humanControl[55] - computerControl[55];
        control += humanControl[56] - computerControl[56];
        control += humanControl[65] - computerControl[65];
        control += humanControl[66] - computerControl[66];

        control /= 10.0f;
        ret += control;

        // credit for attacked pieces:
        for (int i=22; i<100; i++) {
            if (b[i] == 0 || b[i] == 7) continue;
            if (b[i] < 0) {
                if (humanControl[i] > computerControl[i]) {
                    ret += 0.9f * value[-b[i]];
                }
            } 
            if (b[i] > 0) {
                if (humanControl[i] < computerControl[i]) {
                    ret -= 0.9f * value[b[i]];
                }
            }
        }
        
        //Add to check
        if(check(pos, player)) ret *= .05;
                    
        // adjust if computer side to move:
        if (!player) ret = -ret;
        return ret;
    }

    /*
     * Checks validity of Move m at Position p
     *
     * @param Position p Position to be considered
     * @param Move m Move to be considered
     */
    public boolean isValidMove(Position p, Move m){
        ChessPosition pos = (ChessPosition) p;
        ChessMove move = (ChessMove) m;
        boolean valid = false;
        int board_val = pos.board[move.from];

        //calc normal moves
        if ((board_val > 0) && (board_val != 7)) {
            int num = calcPieceMoves(pos, move.from);
outer:              for(int j=0; j<num; j++){
                if (piece_moves[j] == move.to) {
                    valid = true;
                    break outer;
                }				
            }
        }

        //calc checks
        if(valid){
            boolean player = HUMAN;
            Position pos2 = (Position) makeMove((Position) pos, player, move);
            if(check((ChessPosition)pos2, player)){
                    valid = false;
            }
        }

        //calc castling
        if((Math.abs(pos.board[move.from]) == ChessPosition.KING)){
            if(move.to == move.from+2){
                valid = true;
            }
            if(move.to == move.from-2){
                valid = true;
            }
        }

        return valid;
    }
	
    /*
     * Returns array of valid Positions for player from position
     *
     * @param Position p Position to be considered
     * @param boolean player player to be considered
     */
    public ArrayList possibleMoves(Position p, boolean player) {
        if (GameSearch.DEBUG) System.out.println("posibleMoves("+p+","+player+")");
        ChessPosition pos = (ChessPosition)p;
        int num = calcPossibleMoves(pos, player);

        ArrayList chessPos = new ArrayList();
        ChessPosition chessPosT;
        for (int i=0; i<num; i++) {
            chessPosT = new ChessPosition();
            for (int j=22; j<100; j++) chessPosT.board[j] = pos.board[j];
            chessPosT.board[possibleMoveList[i].to] = chessPosT.board[possibleMoveList[i].from];
            chessPosT.board[possibleMoveList[i].from] = 0;
System.out.println(chessPosT);
            if(player == this.PROGRAM){
                if(!check(chessPosT, player)){
                    chessPos.add(chessPosT);
                }
            } else {
                chessPos.add(chessPosT);
            }
        }
        return chessPos;
    }
    
    /*
     *
     */
    public boolean maxDepth(Position p, int depth) {
//        if(wonPosition(p, HUMAN) || wonPosition(p, PROGRAM)) return true;
        if (depth < 1) return false;
        return true;
    }

    public static Position makeMove(Position p, boolean player, Move move) {
        if (GameSearch.DEBUG) System.out.println("Entered Chess.makeMove");
        ChessMove m = (ChessMove)move;
        ChessPosition pos = (ChessPosition) p;
        ChessPosition pos2 = new  ChessPosition();
        for (int i=0; i<120; i++) pos2.board[i] = pos.board[i];
        int pp;
        if (player) pp =  1;
        else        pp = -1;
        if (GameSearch.DEBUG) System.out.println("makeMove: m.from = " + m.from +
                                                 ", m.to = " + m.to);
        pos2.board[m.to] = pos2.board[m.from];
        pos2.board[m.from] = ChessPosition.BLANK;
        if(castling(pos, m)){
            if(m.to == (m.from + 2)){
                pos2.board[29] = ChessPosition.BLANK;
                pos2.board[27] = ChessPosition.ROOK;
            }
            if(m.to == (m.from - 2)){
                pos2.board[22] = ChessPosition.BLANK;
                pos2.board[25] = ChessPosition.ROOK;
            }
        }
        return pos2;
    }

    public static int findKing(ChessPosition pos, boolean player){
        int i;
outer:  for(i=22; i<=99; i++){
            if(player == Chess.HUMAN){
                if(pos.board[i] == ChessPosition.KING){
                        break outer;
                }
            } else if(player = Chess.PROGRAM){
                if(pos.board[i] == -ChessPosition.KING){
                        break outer;
                }
            }
        }
        return i;
    }
    
    public void printControlData(ChessPosition p){
        setControlData(p);
        int[] b = p.board;
        System.out.println("Total Control:(a)");
        for(int i=99; i>=22; i--){
        if (b[i] == 7 && b[i+1]==7) System.out.println();
                if (b[i] != 7) System.out.print(i + ": (" + b[i] + ") " + computerControl[i] + " " + humanControl[i] + "\t");
        } System.out.println("");

        setControlDataB(p);
        System.out.println("Total Control:(b)");
        for(int i=99; i>=22; i--){
        if (b[i] == 7 && b[i+1]==7) System.out.println();
                if (b[i] != 7) System.out.print(i + ": (" + b[i] + ") " + computerControl[i] + " " + humanControl[i] + "\t");
        }System.out.println("");
    }

    /**
     *             PRIVATE API, mostly chess move and evaluation utilities
     */

    // static data that can be re-used (assume single threading!)
    static private float [] computerControl = new float[120];
    static private float [] humanControl    = new float[120];
    
    //"Better" control data, accounts for guarding and pinning
    private void setControlData(ChessPosition pos) {
        for (int i=0; i<120; i++) {
            computerControl[i] = 0;
            humanControl[i] = 0;
        }
        int [] b = pos.board;
        float [] control; // set to computerControl or humanControl, as appropriate
        for (int square_index=22; square_index<100; square_index++) {
            int piece = b[square_index];
            if (piece == 7 || piece == 0) continue;
            int piece_type = piece;
            if (piece_type < 0) {
                piece_type = -piece_type;
                control = computerControl;
            } else {
                control = humanControl;
            }
            int count = 0, side_index, move_offset, temp, next_square;
            int piece_index = index[piece_type];
            int move_index = pieceMovementTable[piece_index];
            if (piece < 0) side_index = -1;
            else           side_index = 1;
            switch (piece_type) {
            case ChessPosition.PAWN:
                    // first check for possible pawn captures:
                    for (int delta=-1; delta<= 1; delta += 2) {
                        move_offset = square_index + side_index * 10 + delta;
                        int target = b[move_offset];
                        if ((target <= -1 && target != 7 && piece > 0) ||
                            (target >= 1 && target != 7 && piece < 0)) {
                            // kluge: count pawn control more:
                            control[square_index + side_index * delta] += 1.25f;
                        }
                    }
                // Note: no break here: we want pawns to use move table also:
            case ChessPosition.KNIGHT:
            case ChessPosition.BISHOP:
            case ChessPosition.ROOK:
            case ChessPosition.KING:
            case ChessPosition.QUEEN:
                    move_index = piece; if (move_index < 0) move_index = -move_index;
                    move_index = index[move_index];
                    //System.out.println("move_index="+move_index);
                    next_square = square_index + pieceMovementTable[move_index];
                outer:
                    while (true) {
                    inner:
                        while (true) {
                            if (next_square > 99) break inner;
                            if (next_square < 22) break inner;
                            if (b[next_square] == 7) break inner;
                            control[next_square] += 1;
                            // the next statement should be augmented for x-ray analysis:
                            if (side_index < 0 && b[next_square] < 0) break inner;
                            if (side_index > 0 && b[next_square] > 0 && b[next_square] != 7) break inner;
                            
                            // If next piece is opposite color, stops searching
                            // This allows for calculating a piece guarding through their own color.
                            // if(((piece > 0) && (b[next_square] < 0)) || ((piece < 0) && (b[next_square] > 0)))break inner;

                            if (piece_type == ChessPosition.PAWN &&
                                (square_index / 10 == 3))  break inner;
                            if (piece_type == ChessPosition.KNIGHT) break inner;
                            if (piece_type == ChessPosition.KING) break inner;
                            next_square += pieceMovementTable[move_index];
                        }
                        move_index += 1;
                        if (pieceMovementTable[move_index] == 0) break outer;
                        next_square = square_index + pieceMovementTable[move_index];
                    }
            }
        }
    }
    
    //"Real" control data, disallows moving through own pieces...
    //Better (necessary?) for finding what's attacked this turn
    private void setControlDataB(ChessPosition pos) {
        for (int i=0; i<120; i++) {
            computerControl[i] = 0;
            humanControl[i] = 0;
        }
        int [] b = pos.board;
        float [] control; // set to computerControl or humanControl, as appropriate
        for (int square_index=22; square_index<100; square_index++) {
            int piece = b[square_index];
            if (piece == 7 || piece == 0) continue;
            int piece_type = piece;
            if (piece_type < 0) {
                piece_type = -piece_type;
                control = computerControl;
            } else {
                control = humanControl;
            }
            int count = 0, side_index, move_offset, temp, next_square;
            int piece_index = index[piece_type];
            int move_index = pieceMovementTable[piece_index];
            if (piece < 0) side_index = -1;
            else           side_index = 1;
            switch (piece_type) {
            case ChessPosition.PAWN:
                    // first check for possible pawn captures:
                    for (int delta=-1; delta<= 1; delta += 2) {
                        move_offset = square_index + side_index * 10 + delta;
                        int target = b[move_offset];
                        if ((target <= -1 && target != 7 && piece > 0) ||
                            (target >= 1 && target != 7 && piece < 0)) {
                            // kluge: count pawn control more:
                            control[square_index + side_index * delta] += 1.25f;
                        }
                    }
                // Note: no break here: we want pawns to use move table also:
            case ChessPosition.KNIGHT:
            case ChessPosition.BISHOP:
            case ChessPosition.ROOK:
            case ChessPosition.KING:
            case ChessPosition.QUEEN:
                    move_index = piece; if (move_index < 0) move_index = -move_index;
                    move_index = index[move_index];
                    //System.out.println("move_index="+move_index);
                    next_square = square_index + pieceMovementTable[move_index];
                outer:
                    while (true) {
                    inner:
                        while (true) {
                            if (next_square > 99) break inner;
                            if (next_square < 22) break inner;
                            if (b[next_square] == 7) break inner;
                            control[next_square] += 1;
                            // the next statement should be augmented for x-ray analysis:
                            if (side_index < 0 && b[next_square] < 0) break inner;
                            if (side_index > 0 && b[next_square] > 0 && b[next_square] != 7) break inner;
                            
                    // If next piece is opposite color, stops searching
                    // This allows for calculating a piece guarding through their own color.
                    if(b[next_square] != 0) break inner;

                            if (piece_type == ChessPosition.PAWN &&
                                (square_index / 10 == 3))  break inner;
                            if (piece_type == ChessPosition.KNIGHT) break inner;
                            if (piece_type == ChessPosition.KING) break inner;
                            next_square += pieceMovementTable[move_index];
                        }
                        move_index += 1;
                        if (pieceMovementTable[move_index] == 0) break outer;
                        next_square = square_index + pieceMovementTable[move_index];
                    }
            }
        }
    }	

    static class aMove {
        int from;
        int to;
    }
	
    //would an ArrayList be more effective?
    private static aMove[] possibleMoveList = new aMove[255];   
    static {
        for (int i=0; i<255; i++) possibleMoveList[i] = new aMove();
    }

    private int calcPossibleMoves(ChessPosition pos, boolean player) {
        //System.out.println("calcPossibleMoves()");
        int [] b = pos.board;
        int count = 0;
        for (int i=22; i<100; i++) {
            int board_val = b[i];
            if (board_val == 7) continue;
            // computer pieces will be negative:
            if ((board_val < 0 && !player) || (board_val > 0 && player)) {
                int num = calcPieceMoves(pos, i);
                for (int j=0; j<num; j++) {
                    if (b[piece_moves[j]] != 7) {
                        if(DEBUG)System.out.println("count="+count+", i="+i);
                        possibleMoveList[count].from = i;
                        possibleMoveList[count].to = piece_moves[j];
                        if(b[piece_moves[j]] == ChessPosition.PAWN && piece_moves[j] >= 22 && piece_moves[j] <= 29){
                            if(HUMAN == player){
                                b[piece_moves[j]] = ChessPosition.QUEEN;
                            } else {
                                b[piece_moves[j]] = -ChessPosition.QUEEN;
                            }
                        } 
                        if(DEBUG)System.out.println("possible move: player="+player+", from="+i+", to="+piece_moves[j]);
                        count++;
                    }
                }
                // TBD: castle logic, etc. (page 159)
            }
        }
        return count;
    }

    private int calcPieceMoves(ChessPosition pos, int square_index) {
        int [] b = pos.board;
        int piece = b[square_index];
        int piece_type = piece; if (piece_type < 0) piece_type = -piece_type;
        int count = 0, side_index, move_offset, temp, next_square;
        int piece_index = index[piece_type];
        int move_index = pieceMovementTable[piece_index];
		
        if (piece < 0) side_index = -1;
        else           side_index = 1;
        switch (piece_type) {
            case ChessPosition.PAWN:{
                // first check for possible pawn captures:
                for (int delta=-1; delta<= 1; delta += 2) {
                        move_offset = square_index + (side_index * 10) + delta;
                        int target = b[move_offset];
                        if ((target <= -1 && target != 7 && piece > 0) ||
                                (target >= 1 && target != 7 && piece < 0)) {
                                piece_moves[count++] = square_index + (side_index * 10) + delta;
                        }
                }
                // check for initial pawn move of 2 squares forward:
                move_offset = square_index + side_index * 20;
                if (piece > 0) temp = 3; else temp = 8;
                if (b[move_offset] == 0 &&
                        (square_index / 10) == temp &&
                        ((piece < 0 && b[square_index - 10]==0) ||
                         (piece > 0 && b[square_index + 10]==0))) {
                        piece_moves[count++] = square_index + side_index * 20;
                }
                // try to move forward 1 square:
                move_offset = square_index + side_index * 10;
                if (b[move_offset] == 0) {
                        piece_moves[count++] = move_offset;
                }
            } break;
            case ChessPosition.KNIGHT:
            case ChessPosition.BISHOP:
            case ChessPosition.ROOK:
            case ChessPosition.KING:{
                
            }//no break, use regular moves also
            case ChessPosition.QUEEN:{
                move_index = piece; if (move_index < 0) move_index = -move_index;
                move_index = index[move_index];
                //System.out.println("move_index="+move_index);
                next_square = square_index + pieceMovementTable[move_index];
outer:          while (true) {
    inner:          while (true) {
                            if (next_square > 99) break inner;
                            if (next_square < 22) break inner;
                            if (b[next_square] == 7) break inner;

                            // check for piece on the same side:
                            if (side_index < 0 && b[next_square] < 0) break inner;
                            if (side_index >0 && b[next_square]  > 0) break inner;

                            piece_moves[count++] = next_square;
                            if (b[next_square] != 0) break inner;
                            if (piece_type == ChessPosition.KNIGHT) break inner;
                            if (piece_type == ChessPosition.KING) break inner;
                            next_square += pieceMovementTable[move_index];
                        }
                    move_index += 1;
                    if (pieceMovementTable[move_index] == 0) break outer;
                    next_square = square_index + pieceMovementTable[move_index];
                }
            }
        }
        return count;
    }

    //uses setControlDataB
    private boolean isReallyAttacked(ChessPosition p, int i){
            setControlDataB(p);
            if (p.board[i] < 0) {
                    if (Math.abs(humanControl[i]) > 0) {
                        //System.out.println("chech");
                        return true;
                    }
            } 
            if (p.board[i] > 0) {
                    if (Math.abs(computerControl[i]) > 0) {
                        //System.out.println("check");
                        return true;
                    }
            }
            return false;		
    }

    //uses setControlData
    private boolean isAttacked(ChessPosition p, int i){
            setControlData(p);
            if (p.board[i] < 0) {
                    if (humanControl[i] > computerControl[i]) {
                            return true;
                    }
            } 
            if (p.board[i] > 0) {
                    if (humanControl[i] < computerControl[i]) {
                            return true;
                    }
            }
            return false;
    }
    
    private static boolean castling(ChessPosition pos, ChessMove move){
        boolean valid = false;
        int i = findKing(pos, Chess.HUMAN);
        if(i == 26){
            if((Math.abs(pos.board[move.from]) == ChessPosition.KING)){
                if(move.to == move.from+2){
                    if(pos.board[29] == 4)
                        valid = true;
                    for(int j=27; j<=28; j++)
                        if(pos.board[j] != ChessPosition.BLANK)
                            valid = false;
                }
                if(move.to == move.from-2){
                    if(pos.board[22] == 4)
                        valid = true;
                    for(int j=23; j<=25; j++)                    
                        if(pos.board[j] != ChessPosition.BLANK)
                            valid = false;                    
                }
            }
        }
        return valid;
    }
    	
    private static int [] piece_moves = new int[255];

    public static int [] initialBoard = {
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
         ChessPosition.ROOK,  ChessPosition.KNIGHT,  ChessPosition.BISHOP,  ChessPosition.QUEEN,  ChessPosition.KING,  ChessPosition.BISHOP,  ChessPosition.KNIGHT,  ChessPosition.ROOK,  7, 7,   // white pieces
         ChessPosition.PAWN,  ChessPosition.PAWN,    ChessPosition.PAWN,    ChessPosition.PAWN,   ChessPosition.PAWN,  ChessPosition.PAWN,    ChessPosition.PAWN,    ChessPosition.PAWN,  7, 7,   // white pawns
         ChessPosition.BLANK, ChessPosition.BLANK,   ChessPosition.BLANK,   ChessPosition.BLANK,  ChessPosition.BLANK, ChessPosition.BLANK,   ChessPosition.BLANK,   ChessPosition.BLANK, 7, 7,   // 8 blank squares, 2 off board
         ChessPosition.BLANK, ChessPosition.BLANK,   ChessPosition.BLANK,   ChessPosition.BLANK,  ChessPosition.BLANK, ChessPosition.BLANK,   ChessPosition.BLANK,   ChessPosition.BLANK, 7, 7,   // 8 blank squares, 2 off board
         ChessPosition.BLANK, ChessPosition.BLANK,   ChessPosition.BLANK,   ChessPosition.BLANK,  ChessPosition.BLANK, ChessPosition.BLANK,   ChessPosition.BLANK,   ChessPosition.BLANK, 7, 7,   // 8 blank squares, 2 off board
         ChessPosition.BLANK, ChessPosition.BLANK,   ChessPosition.BLANK,   ChessPosition.BLANK,  ChessPosition.BLANK, ChessPosition.BLANK,   ChessPosition.BLANK,   ChessPosition.BLANK, 7, 7,   // 8 blank squares, 2 off board
        -ChessPosition.PAWN, -ChessPosition.PAWN,   -ChessPosition.PAWN,   -ChessPosition.PAWN,  -ChessPosition.PAWN, -ChessPosition.PAWN,   -ChessPosition.PAWN,   -ChessPosition.PAWN, 7, 7,   // black pawns
        -ChessPosition.ROOK, -ChessPosition.KNIGHT, -ChessPosition.BISHOP, -ChessPosition.QUEEN, -ChessPosition.KING, -ChessPosition.BISHOP, -ChessPosition.KNIGHT, -ChessPosition.ROOK, 7, 7,   // black pieces
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7
    };

    private static int [] index = {
        0, 12, 15, 10, 1, 6, 0, 0, 0, 6
    };

    private static int [] pieceMovementTable = {
        0, -1, 1, 10, -10, 0, -1, 1, 10, -10, -9, -11, 9,
        11, 0, 8, -8, 12, -12, 19, -19, 21, -21, 0, 10, 20,
        0, 0, 0, 0, 0, 0, 0, 0
    };
    private static int [] value = {
        0, 1, 3, 3, 5, 9, 0, 0, 0, 200
    };
    public static int [] blackSquares = {
        22, 24, 26, 28, 33, 35, 37, 39,
        42, 44, 46, 48, 53, 55, 57, 59,
        62, 64, 66, 68, 73, 75, 77, 79,
        82, 84, 86, 88, 93, 95, 97, 99
    };
}
