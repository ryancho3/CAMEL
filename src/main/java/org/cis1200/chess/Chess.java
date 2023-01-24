package org.cis1200.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Chess {

    private Piece[][] state;
    private boolean player;

    private boolean check;

    private boolean checkmate;

    private boolean stalemate;

    /**
     * Constructor sets up game state.
     */

    public Chess() {
        reset();
    }

    public Chess(Piece[][] state, boolean player) {
        this.state = state;
        this.player = player;
    }

    public void runChecks() {
        checkCheck();
        checkCheckmate();
        resetPawns();
    }

    public Piece[][] executeMove(Move move) {
        int ri = move.getStart()[0];
        int ci = move.getStart()[1];
        int rf = move.getEnd()[0];
        int cf = move.getEnd()[1];
        Piece[][] tempState = deepCopyState(this.state);
        Piece piece = tempState[ri][ci];
        boolean player = piece.isPlayer();
        int[] target = { rf, cf };
        Collection<Move> availableMoves = piece.availableMoves(tempState);
        Move eqmove = null;
        for (Move availableMove : availableMoves) {
            if (Arrays.equals(availableMove.getEnd(), target)) {
                eqmove = availableMove;
            }
        }
        if (eqmove == null) {
            throw new IllegalArgumentException(
                    "Invalid Move: " + Arrays.toString(move.getStart()) + ", " +
                            Arrays.toString(move.getEnd())
            );
        }

        piece.setMovedTwo(eqmove.isMovedTwo());
        piece.setMoved(true);
        piece.setRow(rf);
        piece.setCol(cf);
        tempState[rf][cf] = piece;
        tempState[ri][ci] = null;

        if (eqmove.isAlt()) {
            Move altMove = eqmove.getSecondaryMove();
            int[] start = altMove.getStart();
            int[] end = altMove.getEnd();
            if (!Arrays.equals(end, new int[] { -1, -1 })) {
                Piece piece2 = tempState[start[0]][start[1]];
                piece2.setRow(end[0]);
                piece2.setCol(end[1]);
                piece2.setMoved(true);
                tempState[end[0]][end[1]] = piece2;
            }
            tempState[start[0]][start[1]] = null;
        }

        if (eqmove.isPromotion()) {
            tempState[rf][cf] = new Queen(rf, cf, player);
        }
        return tempState;
    }

    public boolean makeMove(Move move) {
        try {
            runChecks();
            int r = move.getStart()[0];
            int c = move.getStart()[1];
            Piece p = this.state[r][c];
            if (p == null) {
                throw new IllegalArgumentException(
                        "Selected piece is null: " + Arrays.toString(move.getStart())
                );
            }
            if (p.isPlayer() != this.player) {
                throw new IllegalArgumentException(
                        "Selected piece is not player's piece: " +
                                Arrays.toString(move.getStart())
                );
            }

            Piece[][] tempState = executeMove(move);
            Chess temp = new Chess(tempState, this.player);
            if (temp.checkCheck()) {
                throw new IllegalArgumentException(
                        "Can't move into checkmate: " + Arrays.toString(move.getStart()) +
                                ", " + Arrays.toString(move.getEnd())
                );
            }
            this.player = !this.player;
            this.state = tempState;
            this.check = false;
            this.checkmate = false;

            return true;

        } catch (IllegalArgumentException e) {
            // System.out.println(e);
            return false;
        }
    }

    public boolean checkCheckmate() {
        try {

            Piece king = getDefendingKing();
            ArrayList<Move> moves = king.availableMoves(this.state);
            boolean futureMove = false;
            for (Move move : moves) {
                Piece[][] tempState = executeMove(move);
                Chess temp = new Chess(tempState, this.player);
                if (!temp.checkCheck()) {
                    futureMove = true;
                    break;
                }
            }
//            if (!this.checkCheck() && !futureMove) {
//                this.stalemate = true;
//                return false;
//            }
            if (!this.checkCheck()) {
                return false;
            }
            if (futureMove) {
                return false;
            }
            for (int i = 0; i< this.state.length; i++) {
                for (int j = 0; j< this.state[i].length; j++) {
                    if (this.state[i][j] != null && this.state[i][j].isPlayer() == this.player) {
                        ArrayList<Move> pieceMoves = state[i][j].availableMoves(this.state);
                        for (Move move : pieceMoves) {
                            Piece[][] temporary = executeMove(move);
                            Chess tempChess = new Chess(temporary, this.player);
                            if (!tempChess.checkCheck()) {
                                futureMove = true;
                                break;
                            }
                        }
                    }
                }
            }
            this.checkmate = this.checkCheck() && !futureMove;
            this.stalemate = (!moves.isEmpty()) && !futureMove;
            return this.checkmate;
        } catch (RuntimeException ignored) {

        }
        return this.checkmate;
    }

    public boolean checkStalemate() {
        checkCheckmate();
        return this.stalemate;
    }

    public void resetPawns() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = this.state[i][j];
                if (p != null && p.isPlayer() == this.player && p.getType().equals("pawn")) {
                    p.setMovedTwo(false);
                }
            }
        }
    }

    public boolean checkCheck() {
        try {
            Piece king = getDefendingKing();
            int kingRow = king.getRow();
            int kingCol = king.getCol();
            for (int r = 0; r < this.state.length; r++) {
                for (int c = 0; c < this.state[r].length; c++) {
                    Piece current = this.state[r][c];
                    if (current != null && !this.check && current.isPlayer() != this.player) {
                        for (Move move : current.availableMoves(this.state)) {
                            int moveRow = move.getEnd()[0];
                            int moveCol = move.getEnd()[1];
                            this.check = (moveRow == kingRow && moveCol == kingCol);
                            if (this.check) {
                                return true;
                            }
                        }
                    }

                }
            }
            return this.check;
        } catch (RuntimeException ignored) {
        }
        return this.check;
    }

    private Piece getDefendingKing() {
        Piece ret = null;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece current = this.state[r][c];
                if (current != null && current.isPlayer() == this.player
                        && current.getType().equals("king")) {
                    ret = current;
                }
            }
        }
        if (ret == null) {
            this.checkmate = true;
        }

        return ret;
    }

    public void printGameState() {
        System.out.println("\n -----------------------------------------");
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                System.out.print(" | ");
                if (state[i][j] != null) {
                    System.out.print(state[i][j]);
                } else {
                    System.out.print("  ");
                }
            }
            System.out.print(" | ");
            System.out.println("\n -----------------------------------------");
        }
    }

    public void reset() {
        this.state = new Piece[8][8];
        this.player = true;
        this.check = false;
        this.checkmate = false;
        for (int i = 0; i < 8; i++) {
            this.state[1][i] = new Pawn(1, i, false);
        }
        for (int j = 0; j < 8; j++) {
            this.state[6][j] = new Pawn(6, j, true);
        }
        this.state[0][4] = new King(0, 4, false);
        this.state[7][4] = new King(7, 4, true);
        this.state[0][3] = new Queen(0, 3, false);
        this.state[7][3] = new Queen(7, 3, true);
        this.state[0][2] = new Bishop(0, 2, false);
        this.state[0][5] = new Bishop(0, 5, false);
        this.state[7][2] = new Bishop(7, 2, true);
        this.state[7][5] = new Bishop(7, 5, true);
        this.state[0][1] = new Knight(0, 1, false);
        this.state[0][6] = new Knight(0, 6, false);
        this.state[7][1] = new Knight(7, 1, true);
        this.state[7][6] = new Knight(7, 6, true);
        this.state[0][0] = new Rook(0, 0, false);
        this.state[0][7] = new Rook(0, 7, false);
        this.state[7][0] = new Rook(7, 0, true);
        this.state[7][7] = new Rook(7, 7, true);
    }

    public boolean getCurrentPlayer() {
        return player;
    }

    public Piece get(int r, int c) {
        return state[r][c];
    }

    public void setPlayer(boolean set) {
        this.player = set;
    }

    public Piece[][] getState() {
        return deepCopyState(this.state);
    }

    public Piece[][] deepCopyState(Piece[][] state) {
        Piece[][] ret = new Piece[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece current = this.state[r][c];
                if (current != null) {
                    ret[r][c] = current.copy();
                }
            }
        }
        return ret;
    }

    public Chess deepCopy() {
        return new Chess(deepCopyState(this.state), this.player);
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {
        Chess c = new Chess();
        c.printGameState();

        Move f3 = new Move(new int[] { 6, 5 }, new int[] { 5, 5 });
        c.makeMove(f3);
        c.printGameState();

        Move e6 = new Move(new int[] { 1, 4 }, new int[] { 2, 4 });
        c.makeMove(e6);
        c.printGameState();

        Move g4 = new Move(new int[] { 6, 6 }, new int[] { 4, 6 });
        c.makeMove(g4);
        c.printGameState();

        Move qh4 = new Move(new int[] { 0, 3 }, new int[] { 4, 7 });
        c.makeMove(qh4);
        c.printGameState();
        ArrayList<Move> queenMoves = c.state[4][7].availableMoves(c.state);
        for (Move move : queenMoves) {
            System.out.println(Arrays.toString(move.getEnd()));
        }

        if (c.checkCheckmate()) {
            System.out.println("Checkmate!");
        } else {
            System.out.println("Something's wrong...");
        }

    }
}
