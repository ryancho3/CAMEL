package org.cis1200.chess;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Piece {
    private int value;
    private int row;
    private int col;
    private final boolean player;

    private final String type;

    private boolean hasMoved;

    private String imgPath;

    private boolean movedTwo;

    public Piece(int row, int col, boolean player, String type, int value) {
        this.row = row;
        this.col = col;
        this.player = player;
        this.hasMoved = false;
        this.type = type;
        this.value = value;

    }

    public int getValue() {
        return this.value;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public String getType() {
        return type;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setMovedTwo(boolean movedTwo) {
        this.movedTwo = movedTwo;
    }

    public boolean isMovedTwo() {
        return movedTwo;
    }

    public boolean checkPos(int targetRow, int targetCol, Piece[][] state, boolean pawnTake) {
        if (targetRow < 0 || targetRow > 7 || targetCol < 0 || targetCol > 7) {
            return false;
        }
        Piece target = state[targetRow][targetCol];
        if (target == null) {
            return !pawnTake;
        }

        return target.isPlayer() != isPlayer();
    }

    public boolean isPlayer() {
        return this.player;
    }

    public boolean hasMoved() {
        return this.hasMoved;
    }

    public void setMoved(boolean moved) {
        this.hasMoved = moved;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Move newMove(int endRow, int endCol) {
        int[] start = { getRow(), getCol() };
        int[] end = { endRow, endCol };
        return new Move(start, end);
    }

    public ArrayList<Move> horizontalMoves(Piece[][] state) {
        ArrayList<Move> ret = new ArrayList<>();
        // Left
        int targetCol = this.getCol() + 1;
        while (targetCol < 8) {
            Piece target = state[this.getRow()][targetCol];
            if (target == null) {
                ret.add(newMove(this.getRow(), targetCol));
            } else if (target.isPlayer() != this.player) {
                ret.add(newMove(this.getRow(), targetCol));
                break;
            } else {
                break;
            }
            targetCol++;
        }
        // Right
        targetCol = this.getCol() - 1;
        while (targetCol >= 0) {
            Piece target = state[this.getRow()][targetCol];
            if (target == null) {
                ret.add(newMove(this.getRow(), targetCol));
            } else if (target.isPlayer() != this.player) {
                ret.add(newMove(this.getRow(), targetCol));
                break;
            } else {
                break;
            }
            targetCol--;
        }
        // Up
        int targetRow = this.getRow() + 1;
        while (targetRow < 8) {
            Piece target = state[targetRow][this.getCol()];
            if (target == null) {
                ret.add(newMove(targetRow, this.getCol()));
            } else if (target.isPlayer() != this.player) {
                ret.add(newMove(targetRow, this.getCol()));
                break;
            } else {
                break;
            }
            targetRow++;
        }
        // Down
        targetRow = this.getRow() - 1;
        while (targetRow >= 0) {
            Piece target = state[targetRow][this.getCol()];
            if (target == null) {
                ret.add(newMove(targetRow, this.getCol()));
            } else if (target.isPlayer() != this.player) {
                ret.add(newMove(targetRow, this.getCol()));
                break;
            } else {
                break;
            }
            targetRow--;
        }
        return ret;
    }

    public ArrayList<Move> diagonalMoves(Piece[][] state) {
        ArrayList<Move> ret = new ArrayList<>();
        int targetRow;
        int targetCol;
        // Up Right
        targetRow = this.getRow() + 1;
        targetCol = this.getCol() + 1;
        while (targetRow < 8 && targetCol < 8) {
            Piece target = state[targetRow][targetCol];
            if (target == null) {
                ret.add(newMove(targetRow, targetCol));
            } else if (target.isPlayer() != this.player) {
                ret.add(newMove(targetRow, targetCol));
                break;
            } else {
                break;
            }
            targetRow++;
            targetCol++;
        }
        // Up Left
        targetRow = this.getRow() + 1;
        targetCol = this.getCol() - 1;
        while (targetRow < 8 && targetCol >= 0) {
            Piece target = state[targetRow][targetCol];
            if (target == null) {
                ret.add(newMove(targetRow, targetCol));
            } else if (target.isPlayer() != this.player) {
                ret.add(newMove(targetRow, targetCol));
                break;
            } else {
                break;
            }
            targetRow++;
            targetCol--;
        }
        // Down Right
        targetRow = this.getRow() - 1;
        targetCol = this.getCol() + 1;
        while (targetRow >= 0 && targetCol < 8) {
            Piece target = state[targetRow][targetCol];
            if (target == null) {
                ret.add(newMove(targetRow, targetCol));
            } else if (target.isPlayer() != this.player) {
                ret.add(newMove(targetRow, targetCol));
                break;
            } else {
                break;
            }
            targetRow--;
            targetCol++;
        }
        // Down Left
        targetRow = this.getRow() - 1;
        targetCol = this.getCol() - 1;
        while (targetRow >= 0 && targetCol >= 0) {
            Piece target = state[targetRow][targetCol];
            if (target == null) {
                ret.add(newMove(targetRow, targetCol));
            } else if (target.isPlayer() != this.player) {
                ret.add(newMove(targetRow, targetCol));
                break;
            } else {
                break;
            }
            targetRow--;
            targetCol--;
        }
        return ret;
    }

    public void draw(Graphics g, int x, int y) {
        try {
            BufferedImage img = ImageIO.read(new File(this.imgPath));
            g.drawImage(img, x + 20, y + 20, null);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public String toString() {
        return this.type.substring(0, 1).toUpperCase();
    }

    public abstract ArrayList<Move> availableMoves(Piece[][] state);

    public abstract Piece copy();

}
