package org.cis1200.chess;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;


public class Camel {
    // Chess Algorithm using Minimax EvaLuation.

    private Chess gameState;
    private int evaluation;

    private int numSearched;
    private int numStates;
    private int searchDepth;

    private JLabel status;
    private JLabel report;

    private JLabel candidates;

    private JLabel evalLabel;


    private static final int[] ROWMAP = {8, 7, 6, 5, 4, 3, 2, 1};
    private static final String[] COLMAP = {"a", "b", "c", "d", "e", "f", "g", "h"};
    public Camel(Chess gameState, JLabel status, JLabel report, JLabel candidates, JLabel evalLabel) {
        this.status = status;
        this.report = report;
        this.evalLabel = evalLabel;
        this.candidates = candidates;
        this.searchDepth = 4;
        updateGameState(gameState);
    }

    public void updateGameState(Chess gameState) {
        this.gameState = gameState.deepCopy();
        updateLabels();
        evaluateState(gameState);
    }

    public ArrayList<Move> allPossibleMoves(Piece[][] evalState, boolean turn) {
        ArrayList<Move> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = evalState[i][j];
                if (p != null) {
                    if (p.isPlayer() == turn) {
                        moves.addAll(p.availableMoves(evalState));
                    }
                }
            }
        }
        // Filter illegal moves
        ArrayList<Move> ret = new ArrayList<>();

        for (Move move : moves) {
            Chess temp = gameState.deepCopy();
            temp.setPlayer(turn);
            try {
                temp.makeMove(move);
                ret.add(move);
            } catch (IllegalArgumentException e) {
                System.out.println("Caught " + e);
            }
        }
        return ret;
    }

    public int evaluateState(Chess chessState) {
        int value = 0;
        Piece[][] state = chessState.getState();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = state[i][j];
                if (p != null) {
                    if (!p.isPlayer()) {
                        value += p.getValue();
                    } else {
                        value -= p.getValue();
                    }
                }
            }
        }

        if (chessState.getCurrentPlayer() && chessState.checkCheckmate()) {
            value += 100;
        } else if (!chessState.getCurrentPlayer() && chessState.checkCheckmate()) {
            value -= 100;
        } else if (chessState.checkStalemate()) {
            value -= 10;
        }
        this.evaluation = value;
        return value;
    }

    public int minimax(Chess c, int depth, int alpha, int beta, boolean max) {
        if (depth == 0 || c.checkCheckmate()) {
            return evaluateState(c);
        }
        int rec = 0;
        this.numStates++;
        if (max) {
            int maxValue = Integer.MIN_VALUE;
            ArrayList<Move> test = allPossibleMoves(c.getState(), false);
            for (Move move : allPossibleMoves(c.getState(), false)) {
                Chess next = c.deepCopy();
                next.setPlayer(false);
                next.makeMove(move);
                rec = minimax(next, depth - 1, alpha, beta, false);
                maxValue = Integer.max(maxValue, rec);
                alpha = Integer.max(alpha, rec);
                if (beta<=alpha) {
                    break;
                }
            }
            return maxValue;
        } else {
            int minValue = Integer.MAX_VALUE;
            ArrayList<Move> test = allPossibleMoves(c.getState(), false);
            for (Move move : allPossibleMoves(c.getState(), true)) {
                Chess next = c.deepCopy();
                next.setPlayer(true);
                next.makeMove(move);
                rec = minimax(next, depth - 1, alpha, beta, true);
                minValue = Integer.min(minValue, rec);
                beta = Integer.min(beta, rec);
                if (beta<=alpha) {
                    break;
                }
            }
            return minValue;
        }
    }

    public ArrayList<AIMove> getSortedAIMoveList(ArrayList<Move> moves) {
        ArrayList<AIMove> ret = new ArrayList<>();
        for (Move move : moves) {
            Chess c = this.gameState.deepCopy();
            c.setPlayer(false);
            c.makeMove(move);
            this.numSearched++;
            ret.add(new AIMove(move, minimax(c, searchDepth-1, Integer.MIN_VALUE, Integer.MAX_VALUE, false)));
        }
        Collections.sort(ret);
        String candidateString = "<html>Candidate Moves<br/>";
        for (int i = 0; i<6; i++) {
            candidateString += translateMove(ret.get(i).getMove()) + ": " + ret.get(i).getEvaluation() + "<br/>";
        }
        candidateString += "</html>";
        this.candidates.setText(candidateString);
        return ret;
    }

    public Move selectMove() {
//
//        ArrayList<Integer> values = new ArrayList<>();
//        for (Move move : moves) {
//            Chess c = this.gameState.deepCopy();
//            c.setPlayer(false);
//            c.makeMove(move);
//            values.add(minimax(c, 2, false));
//        }
//        int maxValue = Integer.MIN_VALUE;
//        ArrayList<Move> bestMoves = new ArrayList<>();
//        for (int i = 0; i < values.size(); i++) {
//            if (values.get(i) > maxValue) {
//                maxValue = values.get(i);
//                bestMoves.clear();
//            } else if (values.get(i) == maxValue) {
//                bestMoves.add(moves.get(i));
//            }
//        }
//        Move chosen;
//        if (!bestMoves.isEmpty()) {
//            chosen = bestMoves.get((int) (Math.random() * ((bestMoves.size()))));
//        } else {
//            chosen = moves.get(values.indexOf(maxValue));
//        }
//        System.out.println(
//                "Move selected: " + Arrays.toString(chosen.getStart()) + " -> " +
//                        Arrays.toString(chosen.getEnd())
//        );

        this.status.setText("Evaluating best move...");


        int count = 0;
        ArrayList<Move> moves = allPossibleMoves(this.gameState.getState(), false);
        if (moves.size() < 10) {
            this.searchDepth = 5;
        }
        if (moves.size() < 5) {
            this.searchDepth = 6;
        }
        ArrayList<AIMove> sortedMoves = getSortedAIMoveList(moves);
        int prev = sortedMoves.get(0).getEvaluation();
        for (AIMove move : sortedMoves) {
            if (prev != move.getEvaluation()) {
                break;
            } else {
                prev = move.getEvaluation();
                count++;
            }
        }
        AIMove chosen = sortedMoves.get((int) (Math.random() * ((count))));
        this.status.setText("Chose move " + translateMove(chosen.getMove()));
        return chosen.getMove();
    }

    public String translateMove(Move move) {
        int startRow = move.getStart()[0];
        int startCol = move.getStart()[1];
        String end = "";
        end += COLMAP[move.getEnd()[1]] + ROWMAP[move.getEnd()[0]];
        Piece piece = this.gameState.get(startRow, startCol);
        String pieceString;
        if (piece.getType().equals("knight")) {
            pieceString = "N";
        } else {
            pieceString = piece.toString().toUpperCase();
        }
        return pieceString + end;

    }


    public void updateLabels() {
        String evalString;
        int eval = evaluateState(this.gameState);
        if (eval>0) {
            evalString = "+" + eval;
        } else {
            evalString = "" + eval;
        }
        this.evalLabel.setText("Current Position Evaluation: " + evalString);
        this.report.setText("Searched " + this.numSearched + " moves and " + this.numStates + " game states.");
        this.numStates = 0;
        this.numSearched = 0;
    }
}
