import java.util.Collections;
import java.util.HashSet;
import java.util.TreeMap;

public class Neptune extends Player {
	
	private static final int TRUELIMIT = 5; //Depth limit used in actual search
	private static final int ORDERLIMIT = 3; //Depth limit used in action ordering
	byte opponent;
	
    public Neptune(byte player) {
        super(player);
        this.team = "Neptune";
        opponent = (player == OthelloGame.B ? OthelloGame.W : OthelloGame.B);
    }

    @Override
    public Move move(OthelloState state, HashSet<Move> legalMoves) throws InterruptedException {
    	/*Action ordering: Use shallow search (i.e. depth = 3) to sort the possible best moves first*/
        TreeMap<Double,Move> moveMap = new TreeMap<Double,Move>(Collections.reverseOrder());
        double inc = 0.00001; //Increment value
        for(Move move: legalMoves) { //For each legal move, calculate shallow minimax value & sort using TreeMap
        	double max = minValue(OthelloGame.transition(state, move), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1, ORDERLIMIT);
        	if(!moveMap.containsKey(max)) moveMap.put(max, move);
        	else{
        		moveMap.put(max+inc, move);
        		inc += 0.00001; //To prevent TreeMap from replacing existing keys with new Move
        	}
        }
        //System.out.println(legalMoves);
        //System.out.println(moveMap.values());
        
        //For each **sorted** legal move, see whether which one has the highest minimax value
        double maxValue = Double.NEGATIVE_INFINITY;
        Move bestMove = null;
        for(Move move : moveMap.values()) {
            System.out.println(move.toString());
            double thisMax = minValue(OthelloGame.transition(state, move), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1, TRUELIMIT);
            if(maxValue < thisMax) {
                maxValue = thisMax;
                bestMove = move;
            }
        }
        return bestMove;
	}
    
    
    /*NOTE: The Alpha-Beta searching algorithm is likely to be the same for every team in the division.
     * However, the action ordering methods & evaluation functions are also likely to be different for each team.*/
	private double maxValue(OthelloState state, double alpha, double beta, int depth, int depthLimit) {
		HashSet<Move> legalMoves = OthelloGame.getAllLegalMoves(state.getBoard(), state.getPlayer());
		if(depth > depthLimit || legalMoves.size() == 0) return utility(state);
		double v = Double.NEGATIVE_INFINITY;
		for(Move move : legalMoves) {
			v = Math.max(v, minValue(OthelloGame.transition(state, move), alpha, beta, depth+1, depthLimit)); //state???
			if(v >= beta) return v; //Pruning here
			alpha = Math.max(alpha, v);
		}
		return v;
	}

	private double minValue(OthelloState state, double alpha, double beta, int depth, int depthLimit) {
		HashSet<Move> legalMoves = OthelloGame.getAllLegalMoves(state.getBoard(), state.getPlayer()); //rival player???
		if(depth > depthLimit || legalMoves.size() == 0) return utility(state);
		double v = Double.POSITIVE_INFINITY;
		for(Move move : legalMoves) {
			v = Math.min(v, maxValue(OthelloGame.transition(state, move), alpha, beta, depth+1, depthLimit)); //state???
			if(v <= alpha) return v; //Pruning here
			beta = Math.min(beta, v);
		}
		return v;
	}
	
	/*Evaluation function*/
	private double utility(OthelloState state) {
		double myScore = OthelloGame.computeScore(state.getBoard(), player);
		double opScore = OthelloGame.computeScore(state.getBoard(), opponent);
		double myMobility = OthelloGame.getAllLegalMoves(state.getBoard(),player).size();
		double opMobility = OthelloGame.getAllLegalMoves(state.getBoard(),opponent).size();
        double mobilityScore = 100* (myMobility-opMobility)/(myMobility+opMobility); //Calculating mobility (remaining moves) score
        double parityScore = (100 * (myScore - opScore)) / (myScore + opScore); //Calculating coin parity score
        /*The deciding factor in this evaluation function: amount of coins in the corner*/
        double myCornerTaketh = 0;
        for(Move move: OthelloGame.getAllLegalMoves(state.getBoard(),player))
        {
            if(move.toString().equals("(0, 7)")||move.toString().equals("(0, 0)")||move.toString().equals("(7, 0)")||move.toString().equals("(7, 7)"));
            myCornerTaketh++;
        }
		return (mobilityScore + parityScore + 2*myCornerTaketh); //Corner coins has the highest priority
	}
}