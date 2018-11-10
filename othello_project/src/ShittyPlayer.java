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
    	double increment = 0.0001;
    	Move best = null;
        TreeMap<Double,Move> orderMap = new TreeMap<Double,Move>(Collections.reverseOrder());
        
        for(Move move: legalMoves) {
        	double max = minValue(OthelloGame.transition(state, move), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 3);
        	if(!orderMap.containsKey(max)) {
        		orderMap.put(max + moveWeight[move.row()][move.col()], move);
        	}
        	else {
        		orderMap.put(max + moveWeight[move.row()][move.col()] + increment, move);
        		increment += 0.0001;
        	}
        }
        
        double temp = Double.NEGATIVE_INFINITY;
        for(Move move: orderMap.values()) {
        	System.out.println(move.toString());
        	double maxVal = minValue(OthelloGame.transition(state, move), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 6);
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
		HashSet<Move> legalMoves = OthelloGame.getAllLegalMoves(state.getBoard(), state.getPlayer());
		for(Move move:legalMoves){
			double moveScore = moveWeight[move.row()][move.col()];
			if(myScore < moveScore)
				myScore = moveScore;
		}
		return myScore;
	}
	
}