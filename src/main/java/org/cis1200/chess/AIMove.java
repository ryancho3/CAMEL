package org.cis1200.chess;

public class AIMove implements Comparable<AIMove> {

    private Move move;
    private int evaluation;

    public AIMove(Move move, int evaluation) {
        this.move = move;
        this.evaluation = evaluation;
    }

    public AIMove(Move move) {
        this.move = move;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public int compareTo(AIMove o) {
        int compare = ((AIMove)o).getEvaluation();
        return compare - this.evaluation;
    }
}
