import java.util.HashSet;

public class MiniMaxPlayer extends Player {

    private static final int LIMIT = 5;

    public MiniMaxPlayer(byte player) {
        super(player);
        this.team = "42";
    }

    @Override
    public Move move(OthelloState state, HashSet<Move> legalMoves) throws InterruptedException {
        double max = Double.MIN_VALUE;
        Move maxMove = null;
        for (Move move : legalMoves) {
            System.out.println(move.toString());
            double tempMax = minValue(OthelloGame.transition(state, move), 1);
            if (max < tempMax) {
                max = tempMax;
                maxMove = move;
            }
        }
        return maxMove;
    }

    public double minValue(OthelloState state, int depth) {
        // System.out.println(depth);
        HashSet<Move> legalMoves = OthelloGame.getAllLegalMoves(state.getBoard(), state.getPlayer());
        if (legalMoves.size() == 0 || depth > LIMIT) {
            return OthelloGame.computeScore(state.getBoard(), this.player);
        }
        double min = Double.MAX_VALUE;
        for (Move move : legalMoves) {
            OthelloState newState = OthelloGame.transition(state, move);
            min = Math.min(min, maxValue(newState, depth + 1));
        }
        return min;
    }

    public double maxValue(OthelloState state, int depth) {
        // System.out.println(depth);
        HashSet<Move> legalMoves = OthelloGame.getAllLegalMoves(state.getBoard(), state.getPlayer());
        if (legalMoves.size() == 0 || depth > LIMIT) {
            return OthelloGame.computeScore(state.getBoard(), this.player);
        }
        double max = Double.MIN_VALUE;
        for (Move move : legalMoves) {
            OthelloState newState = OthelloGame.transition(state, move);
            max = Math.max(max, minValue(newState, depth + 1));
        }
        return max;
    }

}