package com.davidsouther.chess;

import java.util.*;

public abstract class GameSearch<P extends Position> {

    public static final boolean DEBUG = false;

    public static boolean PROGRAM = false;
    public static boolean HUMAN = true;

    public P currentPosition;
    public Move currentMove;

    private float optimalMoves = 1.00f; // Percentage of optimal moves chosen

    /** PROGRAM false -1, HUMAN true 1 */

    /** Abstract methods: */

    /*
     * Game specific functions
     */
    public abstract boolean drawnPosition(P p); /* Checks if the game has ended at this board position. */

    public abstract boolean wonPosition(P p, boolean player);/* Checks if player has won the game. */

    public abstract float positionEvaluation(P p,
            boolean player);/* Returns a numerical evaluation of a given board position */

    public abstract ArrayList<P> possibleMoves(P p, boolean player);/* All possible moves for player at p */

    public abstract boolean maxDepth(P p, int depth); /* No further moves, either game won or game lost */

    /*
     * Search utility methods:
     */
    public void setOptimalMoves(float f) {
        optimalMoves = f;
    }

    public float getOptimalMoves() {
        return optimalMoves;
    }

    protected SearchPosition<P> minMax(int depth, P p, boolean player) {
        SearchPosition<P> pos = minMaxHelp(depth, p, player, 1000000.0f, -1000000.0f);
        if (GameSearch.DEBUG)
            System.out.println(pos);
        return pos;
    }

    protected SearchPosition<P> minMaxHelp(int depth, P p, boolean player, float alpha, float beta) {
        if (GameSearch.DEBUG)
            System.out.println("alphaBetaHelper(" + depth + "," + p + "," + alpha + "," + beta + ")");

        if (maxDepth(p, depth)) {
            float value = positionEvaluation(p, player);
            SearchPosition<P> position = new SearchPosition<>(value, null);
            if (GameSearch.DEBUG)
                System.out.println(" alphaBetaHelper: max depth at " + depth + ", value=" + value);
            return position;
        }

        SearchPosition<P> best = null;
        ArrayList<P> moves = possibleMoves(p, player);
        for (P move : moves) {
            SearchPosition<P> pos = minMaxHelp(depth + 1, move, !player, -beta, -alpha);
            if (pos.value > beta) {
                if (GameSearch.DEBUG)
                    System.out.println(" ! ! ! value=" + pos.value + ", beta=" + beta);
                beta = pos.value;
                best = pos;
            }
            /**
             * Use the alpha-beta cutoff test to abort search if we found a move that proves
             * that the previous move in the move chain was dubious
             */
            if (beta >= alpha) {
                break;
            }
        }
        return best;
    }

    public void playGame() {
        SearchPosition<P> pos = minMax(0, this.currentPosition, PROGRAM);
        currentMove = ChessHistory.findMove((ChessPosition) currentPosition, (ChessPosition) pos.position);
    }

    public void setPosition(P p) {
        currentPosition = p;
    }

    public P getPosition() {
        return currentPosition;
    }

    public Move getMove() {
        return currentMove;
    }
}

class SearchPosition<P extends Position> {
    public float value;
    public P position;

    SearchPosition(float value, P position) {
        this.value = value;
        this.position = position;
    }

    @Override
    public String toString() {
        return "^^ val: " + value + ", al(1): " + position;
    }
}