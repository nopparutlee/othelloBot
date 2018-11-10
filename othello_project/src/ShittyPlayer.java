import java.util.HashSet;
import java.util.Random;

public class ShittyPlayer extends Player {
	
	private static final int LIMIT = 5;
	byte opponent;
	
    public ShittyPlayer(byte player) {
        super(player);
        this.team = "Wankers";
        opponent = (player == OthelloGame.B ? OthelloGame.W : OthelloGame.B);
    }

    @Override
    public Move move(OthelloState state, HashSet<Move> legalMoves) throws InterruptedException {
        //For each legal move, see whether which one has the highest minimax value
        double maxValue = Double.NEGATIVE_INFINITY;
        Move bestMove = null;
        for(Move move : legalMoves) {
            System.out.println(move.toString());
            double thisMax = minValue(OthelloGame.transition(state, move), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1);
            if(maxValue < thisMax) {
                maxValue = thisMax;
                bestMove = move;
            }
        }
        return bestMove;
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
		double myScore = OthelloGame.computeScore(state.getBoard(), player);
		double opScore = OthelloGame.computeScore(state.getBoard(), opponent);
		return (100 * (myScore - opScore)) / (myScore + opScore);
	}
	
	private double computeScoreWithHeuristic(OthelloState state){
		
		return 0.0;
	}
}