package org.cis1200.chess;

import java.util.ArrayList;

public class Pawn extends Piece {

    public Pawn(int row, int col, boolean player) {
        super(row, col, player, "pawn", 1);
        if (player) {
            this.setImgPath("files/white_pawn.png");
        } else {
            this.setImgPath("files/black_pawn.png");
        }

    }

    @Override
    public ArrayList<Move> availableMoves(Piece[][] state) {
        int forward;
        if (this.isPlayer()) {
            forward = -1;
        } else {
            forward = 1;
        }
        ArrayList<Move> moves = new ArrayList<>();

        // Move forward
        if (this.checkPos(this.getRow() + forward, this.getCol(), state, false) &&
                state[this.getRow() + forward][this.getCol()] == null) {
            Move forwardMove = newMove(this.getRow() + forward, this.getCol());
            if (checkPromotion(this.getRow() + forward)) {
                forwardMove.setPromotion();
            }
            moves.add(forwardMove);

        }
        // Move forward two spaces (hasn't moved yet)
        if (this.checkPos(this.getRow() + (2 * forward), this.getCol(), state, false) &&
                !this.hasMoved() && state[this.getRow() + (2 * forward)][this.getCol()] == null &&
                state[this.getRow() + forward][this.getCol()] == null) {
            Move forwardTwoMove = newMove(this.getRow() + (2 * forward), this.getCol());
            forwardTwoMove.setMovedTwo();
            moves.add(forwardTwoMove);
        }
        // Left Take
        if (this.checkPos(this.getRow() + forward, this.getCol() - 1, state, true)) {
            Move leftTakeMove = newMove(this.getRow() + forward, this.getCol() - 1);
            if (checkPromotion(this.getRow() + 1)) {
                leftTakeMove.setPromotion();
            }
            moves.add(leftTakeMove);
        }
        // Right Take
        if (this.checkPos(this.getRow() + forward, this.getCol() + 1, state, true)) {
            Move rightTakeMove = newMove(this.getRow() + forward, this.getCol() + 1);
            if (checkPromotion(this.getRow() + 1)) {
                rightTakeMove.setPromotion();
            }
            moves.add(rightTakeMove);
        }
        // En Passant
        if ((this.getCol() - 1) >= 0) {
            Piece leftPiece = state[this.getRow()][this.getCol() - 1];
            if (leftPiece != null && leftPiece.getType().equals("pawn")
                    && (leftPiece.isPlayer() != this.isPlayer())) {
                if (leftPiece.isMovedTwo() &&
                        checkPos(this.getRow() + forward, this.getCol() - 1, state, false)) {
                    Move enPassantLeftMove = newMove(this.getRow() + forward, this.getCol() - 1);
                    enPassantLeftMove.setAlt();
                    int[] targetPos = { this.getRow(), this.getCol() - 1 };
                    int[] kill = { -1, -1 };
                    Move killMove = new Move(targetPos, kill);
                    enPassantLeftMove.setSecondaryMove(killMove);
                    moves.add(enPassantLeftMove);
                }
            }
        }
        if ((this.getCol() + 1) < 8) {
            Piece rightPiece = state[this.getRow()][this.getCol() + 1];
            if (rightPiece != null && rightPiece.getType().equals("pawn")
                    && (rightPiece.isPlayer() != this.isPlayer())) {
                if (rightPiece.isMovedTwo() &&
                        checkPos(this.getRow() + forward, this.getCol() + 1, state, false)) {
                    Move enPassantRightMove = newMove(this.getRow() + forward, this.getCol() + 1);
                    enPassantRightMove.setAlt();
                    int[] targetPos = { this.getRow(), this.getCol() + 1 };
                    int[] kill = { -1, -1 };
                    Move killMove = new Move(targetPos, kill);
                    enPassantRightMove.setSecondaryMove(killMove);
                    moves.add(enPassantRightMove);
                }
            }
        }

        return moves;
    }

    public boolean checkPromotion(int targetRow) {
        if (this.isPlayer() && targetRow == 0) {
            return true;
        }
        return !this.isPlayer() && targetRow == 7;
    }

    public Pawn copy() {
        Pawn copy = new Pawn(this.getRow(), this.getCol(), this.isPlayer());
        copy.setMovedTwo(this.isMovedTwo());
        copy.setMoved(this.hasMoved());
        return copy;
    }
}