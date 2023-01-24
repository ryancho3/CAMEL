package org.cis1200.chess;

import java.util.ArrayList;

public class King extends Piece {

    public King(int row, int col, boolean player) {
        super(row, col, player, "king", 100);
        if (player) {
            this.setImgPath("files/white_king.png");
        } else {
            this.setImgPath("files/black_king.png");
        }

    }

    @Override
    public ArrayList<Move> availableMoves(Piece[][] state) {
        ArrayList<Move> moves = new ArrayList<>();

        // Move up
        if (checkPos(getRow() + 1, getCol(), state, false)) {
            moves.add(newMove(getRow() + 1, getCol()));
        }
        // Move down
        if (checkPos(getRow() - 1, getCol(), state, false)) {
            moves.add(newMove(getRow() - 1, getCol()));
        }
        // Move left
        if (checkPos(getRow(), getCol() - 1, state, false)) {
            moves.add(newMove(getRow(), getCol() - 1));
        }
        // Move right
        if (checkPos(getRow(), getCol() + 1, state, false)) {
            moves.add(newMove(getRow(), getCol() + 1));
        }
        // Move up-left
        if (checkPos(getRow() + 1, getCol() - 1, state, false)) {
            moves.add(newMove(getRow() + 1, getCol() - 1));
        }
        // Move up-right
        if (checkPos(getRow() + 1, getCol() + 1, state, false)) {
            moves.add(newMove(getRow() + 1, getCol() + 1));
        }
        // Move down-left
        if (checkPos(getRow() - 1, getCol() - 1, state, false)) {
            moves.add(newMove(getRow() - 1, getCol() - 1));
        }
        // Move down-right
        if (checkPos(getRow() - 1, getCol() + 1, state, false)) {
            moves.add(newMove(getRow() - 1, getCol() + 1));
        }
        // Castling
        if (!this.hasMoved()) {
            boolean kingSideAble = (state[this.getRow()][5] == null)
                    && (state[this.getRow()][6] == null) &&
                    (state[this.getRow()][7] != null) && (!state[this.getRow()][7].hasMoved());
            boolean queenSideAble = (state[this.getRow()][3] == null)
                    && (state[this.getRow()][2] == null) &&
                    (state[this.getRow()][1] == null) && (state[this.getRow()][0] != null) &&
                    (!state[this.getRow()][0].hasMoved());
            if (kingSideAble) {
                Move kingSideCastleMove = newMove(this.getRow(), 6);
                int[] rookStart = { this.getRow(), 7 };
                int[] rookEnd = { this.getRow(), 5 };
                Move rookMove = new Move(rookStart, rookEnd);
                kingSideCastleMove.setAlt();
                kingSideCastleMove.setSecondaryMove(rookMove);
                moves.add(kingSideCastleMove);
            }
            if (queenSideAble) {
                Move queenSideCastleMove = newMove(this.getRow(), 2);
                int[] rookStart = { this.getRow(), 0 };
                int[] rookEnd = { this.getRow(), 3 };
                Move rookMove = new Move(rookStart, rookEnd);
                queenSideCastleMove.setAlt();
                queenSideCastleMove.setSecondaryMove(rookMove);
                moves.add(queenSideCastleMove);
            }
        }

        return moves;
    }

    public King copy() {
        King copy = new King(this.getRow(), this.getCol(), this.isPlayer());
        copy.setMoved(this.hasMoved());
        return copy;
    }
}
