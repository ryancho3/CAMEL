package org.cis1200.chess;

import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(int row, int col, boolean player) {
        super(row, col, player, "bishop", 3);
        if (player) {
            this.setImgPath("files/white_bishop.png");
        } else {
            this.setImgPath("files/black_bishop.png");
        }
    }

    @Override
    public ArrayList<Move> availableMoves(Piece[][] state) {
        return diagonalMoves(state);
    }

    @Override
    public Piece copy() {
        return new Bishop(this.getRow(), this.getCol(), this.isPlayer());
    }
}
