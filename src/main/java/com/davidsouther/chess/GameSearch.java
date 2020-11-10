package com.davidsouther.chess;

import java.lang.reflect.Array;
import java.util.*;
import javax.swing.*;

public abstract class GameSearch {

    public static final boolean DEBUG = false;

    public static boolean PROGRAM = false;
    public static boolean HUMAN = true;

    public Position currentPosition;
    public Move currentMove;

    private float optimalMoves = 1.00f; // Percentage of optimal moves chosen

    /** PROGRAM false -1, HUMAN true 1 */

    /** Abstract methods: */

    /*
     * Game specific functions
     */
    public abstract boolean drawnPosition(Position p); /* Checks if the game has ended at this board position. */

    public abstract boolean wonPosition(Position p, boolean player);/* Checks if player has won the game. */

    public abstract float positionEvaluation(Position p,
            boolean player);/* Returns a numerical evaluation of a given board position */

    public abstract ArrayList possibleMoves(Position p, boolean player);/* All possible moves for player at p */

    public abstract boolean maxDepth(Position p, int depth); /* No further moves, either game won or game lost */

    /*
     * Search utility methods:
     */
    public void setOptimalMoves(float f) {
        optimalMoves = f;
    }

    public float getOptimalMoves() {
        return optimalMoves;
    }

    protected ArrayList minMax(int depth, Position p, boolean player) {
        ArrayList al = minMaxHelp(depth, p, player, 1000000.0f, -1000000.0f);
        if (GameSearch.DEBUG)
            System.out.println("^^ al(0): " + al.get(0) + ", al(1): " + al.get(1));
        return al;
    }

    protected ArrayList minMaxHelp(int depth, Position p, boolean player, float alpha, float beta) {
        System.out.println("");
        if (GameSearch.DEBUG)
            System.out.println("alphaBetaHelper(" + depth + "," + p + "," + alpha + "," + beta + ")");
        if (maxDepth(p, depth)) {
            ArrayList al = new ArrayList(2);
            float value = positionEvaluation(p, player);
            float cutoff = (float) Math.random();
            if (cutoff > optimalMoves)
                value = -value;
            al.add(new Float(value));
            al.add(null);
            if (GameSearch.DEBUG) {
                System.out.println(" alphaBetaHelper: mx depth at " + depth + ", value=" + value);
            }
            return al;
        }
        ArrayList best = new ArrayList();
        ArrayList moves = possibleMoves(p, player);
        for (int i = 0; i < moves.size(); i++) {
            ArrayList al2 = minMaxHelp(depth + 1, (Position) moves.get(i), !player, -beta, -alpha);
            // if (v2 == null || v2.size() < 1) continue;
            float value = -((Float) al2.get(0)).floatValue();
            if (value > beta) {
                if (GameSearch.DEBUG)
                    System.out.println(" ! ! ! value=" + value + ", beta=" + beta);
                beta = value;
                best = new ArrayList();
                best.add(moves.get(i));
                for (int x = 1; x < al2.size(); x++) {
                    Object o = al2.get(x);
                    if (o != null)
                        best.add(o);
                }
            }
            /**
             * Use the alpha-beta cutoff test to abort search if we found a move that proves
             * that the previous move in the move chain was dubious
             */
            if (beta >= alpha) {
                break;
            }
        }
        ArrayList al3 = new ArrayList();
        al3.add(new Float(beta));
        for (int i = 0; i < best.size(); i++) {
            al3.add(best.get(i));
        }
        return al3;
    }

    public void playGame() {
        ArrayList al = minMax(0, this.currentPosition, PROGRAM);

        if (DEBUG) {
            for (int i = 0; i < al.size(); i++) {
                System.out.println(" next element: " + al.get(i));
            }
        }
        System.out.println((ChessPosition) al.get(1));
        currentMove = ChessHistory.findMove((ChessPosition) currentPosition, (ChessPosition) al.get(1));
    }

    public void setPosition(Position p) {
        currentPosition = p;
    }

    public Position getPosition() {
        return currentPosition;
    }

    public Move getMove() {
        return currentMove;
    }
}
