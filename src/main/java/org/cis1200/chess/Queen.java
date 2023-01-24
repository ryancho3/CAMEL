package org.cis1200.chess;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(int row, int col, boolean player) {
        super(row, col, player, "queen", 9);

        if (player) {
            this.setImgPath("files/white_queen.png");
        } else {
            this.setImgPath("files/black_queen.png");
        }
    }

    @Override
    public ArrayList<Move> availableMoves(Piece[][] state) {
        ArrayList<Move> ret = horizontalMoves(state);
        ret.addAll(diagonalMoves(state));
        return ret;
    }

    @Override
    public Piece copy() {
        return new Queen(this.getRow(), this.getCol(), this.isPlayer());
    }
}
