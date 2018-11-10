import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeMap;

public class ShittyPlayer extends Player {
	
	private static final int LIMIT = 5;
	byte opponent;
	int[][] moveWeight = new int[][] {{99,-8,8,6,6,8,-8,99},{-8,-24,-4,-3,-3,-4,-24,-8},{8,-4,7,4,4,7,-4,8},{6,-3,4,0,0,4,-3,6}
										,{6,-3,4,0,0,4,-3,6},{8,-4,7,4,4,7,-4,8},{-8,-24,-4,-3,-3,-4,-24,-8},{99,-8,8,6,6,8,-8,99}};
	
    public ShittyPlayer(byte player) {
        super(player);
        this.team = "Wankers";
        opponent = (player == OthelloGame.B ? OthelloGame.W : OthelloGame.B);
    }

    @Override
    public Move move(OthelloState state, HashSet<Move> legalMoves) throws InterruptedException {
    	Move best = null;
        double temp = Double.NEGATIVE_INFINITY;
        for(Move move: legalMoves) {
        	System.out.println(move.toString());
        	double maxVal = minValue(OthelloGame.transition(state, move), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 10);
        	if(temp < maxVal) {
        		temp = maxVal;
        		best = move;
        	}
        }
        return best;
	}

	private double maxValue(OthelloState state, double alpha, double beta, int depth) {
		HashSet<Move> legalMoves = OthelloGame.getAllLegalMoves(state.getBoard(), state.getPlayer());
		if(depth > LIMIT || legalMoves.size() == 0) return utility(state);
		double v = Double.NEGATIVE_INFINITY;
		for(Move move : legalMoves) {
			v = Math.max(v, minValue(OthelloGame.transition(state, move), alpha, beta, depth+1)); //state???
			if(v >= beta) return v; //Pruning here
			alpha = Math.max(alpha, v);
		}
		return v;
	}

	private double minValue(OthelloState state, double alpha, double beta, int depth) {
		HashSet<Move> legalMoves = OthelloGame.getAllLegalMoves(state.getBoard(), state.getPlayer()); //rival player???
		if(depth > LIMIT || legalMoves.size() == 0) return utility(state);
		double v = Double.POSITIVE_INFINITY;
		for(Move move : legalMoves) {
			v = Math.min(v, maxValue(OthelloGame.transition(state, move), alpha, beta, depth+1)); //state???
			if(v <= alpha) return v; //Pruning here
			beta = Math.min(beta, v);
		}
		return v;
	}
	
	private double utility(OthelloState state) {
		double myScore = 0.0;
		int myPieces = 0;
		int enemyLegalMoves = 64;
		HashSet<Move> legalMoves = OthelloGame.getAllLegalMoves(state.getBoard(), state.getPlayer());
		for(Move move:legalMoves){
			OthelloState nextPossibleState = OthelloGame.transition(state, move);
			double moveScore = moveWeight[move.row()][move.col()];
			int enemyPossibleMove = OthelloGame.getAllLegalMoves(nextPossibleState.getBoard(), opponent).size();
			if(enemyPossibleMove == 0){
				enemyPossibleMove = -30;
			}
			int possibleMyPieces = OthelloGame.computeScore(nextPossibleState.getBoard(), player);
			if(myScore + myPieces - enemyLegalMoves < moveScore + possibleMyPieces - enemyPossibleMove){
				myScore = moveScore;
				myPieces = possibleMyPieces;
				enemyLegalMoves = enemyPossibleMove;
			}
		}
		return myScore + myPieces - enemyLegalMoves;
	}
	
}