import java.util.HashSet;
import java.util.Random;

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