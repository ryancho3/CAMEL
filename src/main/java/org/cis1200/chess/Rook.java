package org.cis1200.chess;

import java.util.ArrayList;

public class Rook extends Piece {

    public Rook(int row, int col, boolean player) {
        super(row, col, player, "rook", 5);
        if (player) {
            this.setImgPath("files/white_rook.png");
        } else {
            this.setImgPath("files/black_rook.png");
        }
    }

    @Override
    public ArrayList<Move> availableMoves(Piece[][] state) {
        ArrayList<Move> moves = horizontalMoves(state);
        // Castling
        if (!this.hasMoved()) {
            boolean kingSideAble = (state[this.getRow()][5] == null)
                    && (state[this.getRow()][6] == null) &&
                    (state[this.getRow()][4] != null) && (!state[this.getRow()][4].hasMoved());
            boolean queenSideAble = (state[this.getRow()][3] == null)
                    && (state[this.getRow()][2] == null) &&
                    (state[this.getRow()][1] == null) && (state[this.getRow()][4] != null) &&
                    (!state[this.getRow()][4].hasMoved());
            if (kingSideAble) {
                Move kingSideCastleMove = newMove(this.getRow(), 5);
                int[] kingStart = { this.getRow(), 4 };
                int[] kingEnd = { this.getRow(), 6 };
                Move kingMove = new Move(kingStart, kingEnd);
                kingSideCastleMove.setAlt();
                kingSideCastleMove.setSecondaryMove(kingMove);
                moves.add(kingSideCastleMove);
            }
            if (queenSideAble) {
                Move queenSideCastleMove = newMove(this.getRow(), 3);
                int[] kingStart = { this.getRow(), 4 };
                int[] kingEnd = { this.getRow(), 2 };
                Move kingMove = new Move(kingStart, kingEnd);
                queenSideCastleMove.setAlt();
                queenSideCastleMove.setSecondaryMove(kingMove);
                moves.add(queenSideCastleMove);
            }
        }

        return moves;
    }

    @Override
    public Piece copy() {
        Rook copy = new Rook(this.getRow(), this.getCol(), this.isPlayer());
        copy.setMoved(this.hasMoved());
        return copy;
    }

}
