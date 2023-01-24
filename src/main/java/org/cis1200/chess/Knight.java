package org.cis1200.chess;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(int row, int col, boolean player) {
        super(row, col, player, "knight", 3);
        if (player) {
            this.setImgPath("files/white_knight.png");
        } else {
            this.setImgPath("files/black_knight.png");
        }
    }

    @Override
    public ArrayList<Move> availableMoves(Piece[][] state) {
        ArrayList<Move> ret = new ArrayList<>();
        // Forward moves
        int forwardRow = this.getRow() + 2;
        if (checkPos(forwardRow, this.getCol() - 1, state, false)) {
            ret.add(newMove(forwardRow, this.getCol() - 1));
        }
        if (checkPos(forwardRow, this.getCol() + 1, state, false)) {
            ret.add(newMove(forwardRow, this.getCol() + 1));
        }
        // Backwards moves
        int backwardRow = this.getRow() - 2;
        if (checkPos(backwardRow, this.getCol() - 1, state, false)) {
            ret.add(newMove(backwardRow, this.getCol() - 1));
        }
        if (checkPos(backwardRow, this.getCol() + 1, state, false)) {
            ret.add(newMove(backwardRow, this.getCol() + 1));
        }
        // Left moves
        int leftCol = this.getCol() - 2;
        if (checkPos(this.getRow() + 1, leftCol, state, false)) {
            ret.add(newMove(this.getRow() + 1, leftCol));
        }
        if (checkPos(this.getRow() - 1, leftCol, state, false)) {
            ret.add(newMove(this.getRow() - 1, leftCol));
        }
        // Right moves
        int rightCol = this.getCol() + 2;
        if (checkPos(this.getRow() + 1, rightCol, state, false)) {
            ret.add(newMove(this.getRow() + 1, rightCol));
        }
        if (checkPos(this.getRow() - 1, rightCol, state, false)) {
            ret.add(newMove(this.getRow() - 1, rightCol));
        }

        return ret;
    }

    @Override
    public Piece copy() {
        return new Knight(this.getRow(), this.getCol(), this.isPlayer());
    }
}
