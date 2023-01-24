package org.cis1200.chess;

import java.util.Arrays;

public class Move {

    private final int[] start;
    private final int[] end;
    private boolean alt;
    private boolean promotion;
    private boolean movedTwo;

    private Move secondaryMove;

    public Move(int[] start, int[] end, boolean alt, boolean promotion, Move secondaryMove) {
        this.start = start;
        this.end = end;
        this.alt = alt;
        this.promotion = promotion;
        this.secondaryMove = secondaryMove;
    }

    public Move(int[] start, int[] end, boolean promotion) {
        this.start = start;
        this.end = end;
        this.alt = false;
        this.promotion = promotion;
        this.secondaryMove = null;
    }

    public Move(int[] start, int[] end) {
        this.start = start;
        this.end = end;
        this.alt = false;
        this.promotion = false;
        this.secondaryMove = null;
    }

    public int[] getStart() {
        return this.start;
    }

    public int[] getEnd() {
        return this.end;
    }

    public boolean isMovedTwo() {
        return movedTwo;
    }

    public boolean isAlt() {
        return this.alt;
    }

    public void setAlt() {
        this.alt = true;
    }

    public boolean isPromotion() {
        return this.promotion;
    }

    public void setPromotion() {
        this.promotion = true;
    }

    public Move getSecondaryMove() {
        return this.secondaryMove;
    }

    public void setSecondaryMove(Move move) {
        this.secondaryMove = move;
    }

    public void setMovedTwo() {
        this.movedTwo = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Move move = (Move) o;
        return alt == move.alt && promotion == move.promotion &&
                Arrays.equals(start, move.start) && Arrays.equals(end, move.end);
    }
}
