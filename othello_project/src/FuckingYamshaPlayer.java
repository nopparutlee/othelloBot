
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class FuckingYamshaPlayer extends Player {

	public FuckingYamshaPlayer(byte player) {
		super(player);
		this.team = "Mars";
		// TODO Auto-generated constructor stub
	}

	private static final int LIMIT = 5;
	private static byte MyPlayer;
	private static byte HisPlayer;

	@Override
	public Move move(OthelloState state, HashSet<Move> legalMoves) throws InterruptedException {
		double max = Double.MIN_VALUE;
		Move maxMove = null;
		MyPlayer = state.getPlayer();
		HisPlayer = (byte) ((--MyPlayer ^ 1) + 1);
//		Thread.sleep(5000);
		for (Move move : legalMoves) {
			System.out.println(move.toString());
			double tempMax = minValue(OthelloGame.transition(state, move), Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY, 1);
			if (max < tempMax) {
				max = tempMax;
				maxMove = move;
			}
		}
		return maxMove;
	}

	public double minValue(OthelloState state, double a, double b, int depth) {
		// System.out.println(depth);
		HashSet<Move> legalMoves = OthelloGame.getAllLegalMoves(state.getBoard(), state.getPlayer());
		if (legalMoves.size() == 0 || depth > LIMIT) {
			return eval(state);
		}
		double min = Double.MAX_VALUE;

		ArrayList<OthelloState> next = new ArrayList<OthelloState>();
		for (Move move : legalMoves) {
			next.add(OthelloGame.transition(state, move));
		}
		Collections.sort(next, new Comparator<OthelloState>() {
			@Override
			public int compare(OthelloState arg0, OthelloState arg1) {
				return (int) (eval(arg0) - eval(arg1));
			}

		});

		for (OthelloState newState : next) {
			min = Math.min(min, maxValue(newState, a, b, depth + 1));
			if (min <= a) {
				return min;
			}
			b = Math.min(b, min);
		}

//        for (Move move : legalMoves) {
//            OthelloState newState = OthelloGame.transition(state, move);
//            min = Math.min(min, maxValue(newState,a,b, depth + 1));
//            if(min<=a) {
//            	return min;
//            }
//            b = Math.min(b, min);
//        }
		return min;
	}

	public double maxValue(OthelloState state, double a, double b, int depth) {
		// System.out.println(depth);
		HashSet<Move> legalMoves = OthelloGame.getAllLegalMoves(state.getBoard(), state.getPlayer());
		if (legalMoves.size() == 0 || depth > LIMIT) {
			return eval(state);
		}
		double max = Double.MIN_VALUE;

		ArrayList<OthelloState> next = new ArrayList<OthelloState>();
		for (Move move : legalMoves) {
			next.add(OthelloGame.transition(state, move));
		}
		Collections.sort(next, new Comparator<OthelloState>() {
			@Override
			public int compare(OthelloState arg0, OthelloState arg1) {
				return (int) (eval(arg1) - eval(arg0));
			}

		});

		for (OthelloState newState : next) {
			max = Math.max(max, minValue(newState, a, b, depth + 1));
			if (max >= b) {
				return max;
			}
			a = Math.max(a, max);
		}

//        for (Move move : legalMoves) {
//            OthelloState newState = OthelloGame.transition(state, move);
//            max = Math.max(max, minValue(newState,a,b, depth + 1));
//            if(max>=b) {
//            	return max;
//            }
//            a = Math.max(a, max);
//        }
		return max;
	}

	// 3 stages based on the paper; early, middle, and ending stages
	static final double[][] early = { { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, -0.02231, 0.05583, 0.02004, 0.02004, 0.05583, -0.02231, 0 },
			{ 0, 0.05583, 0.10126, -0.10927, -0.10927, 0.10126, 0.05583, 0 },
			{ 0, 0.02004, -0.10927, -0.10155, -0.10155, -0.10927, 0.02004, 0 },
			{ 0, 0.02004, -0.10927, -0.10155, -0.10155, -0.10927, 0.02004, 0 },
			{ 0, 0.05583, 0.10126, -0.10927, -0.10927, 0.10126, 0.05583, 0 },
			{ 0, -0.02231, 0.05583, 0.02004, 0.02004, 0.05583, -0.02231, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } },
			middle = { { 6.32711, -3.32813, 0.53907, -2.00512, -2.00512, 0.53907, -3.32813, 6.32711 },
					{ -3.32813, -1.52928, -1.87550, -0.18176, -0.18176, -1.87550, -1.52928, -3.32813 },
					{ 0.33907, -1.87550, 1.06939, 0.62415, 0.62415, 1.06939, -1.87550, 0.33907 },
					{ -2.00512, -0.18176, 0.62415, 0.10539, 0.10539, 0.62415, -0.18176, -2.00512 },
					{ -2.00512, -0.18176, 0.62415, 0.10539, 0.10539, 0.62415, -0.18176, -2.00512 },
					{ 0.33907, -1.87550, 1.06939, 0.62415, 0.62415, 1.06939, -1.87550, 0.33907 },
					{ -3.32813, -1.52928, -1.87550, -0.18176, -0.18176, -1.87550, -1.52928, -3.32813 },
					{ 6.32711, -3.32813, 0.33907, -2.00512, -2.00512, 0.33907, -3.32813, 6.32711 } },
			ending = { { 5.50062, -0.17812, -2.58948, -0.59007, -0.59007, -2.58948, -0.17812, 5.50062 },
					{ -0.17812, 0.96804, -2.16084, -2.01723, -2.01723, -2.16084, 0.96804, -0.17812 },
					{ -2.58948, -2.16084, 0.49062, -1.07055, -1.07055, 0.49062, -2.16084, -2.58948 },
					{ -0.59007, -2.01723, -1.07055, 0.73486, 0.73486, -1.07055, -2.01723, -0.59007 },
					{ -0.59007, -2.01723, -1.07055, 0.73486, 0.73486, -1.07055, -2.01723, -0.59007 },
					{ -2.58948, -2.16084, 0.49062, -1.07055, -1.07055, 0.49062, -2.16084, -2.58948 },
					{ -0.17812, 0.96804, -2.16084, -2.01723, -2.01723, -2.16084, 0.96804, -0.17812 },
					{ 5.50062, -0.17812, -2.58948, -0.59007, -0.59007, -2.58948, -0.17812, 5.50062 } };

	public static double eval(OthelloState state) {
		double Heuristic = 0.0;
		// First feature, amount of coin
//		int Mycoin = OthelloGame.computeScore(state.getBoard(), MyPlayer);
//		int Hiscoin = OthelloGame.computeScore(state.getBoard(), HisPlayer);
//		double coin_count = 100 * ((double)Mycoin - Hiscoin) / (Mycoin + Hiscoin);
		// Second feature, score of a tile based on a research paper
		byte[][] current = state.getBoard();
		double board = 0.0;
		int edge = 0, corner1 = 0, corner2 = 0;
		for (int i = 0; i < current.length; i++) {
			for (int j = 0; j < current[i].length; j++) {
				if (current[0][j] != 0 || current[i][0] != 0 || current[i][current.length - 1] != 0
						|| current[current.length - 1][j] != 0) {
					edge++;
				}
				if (current[0][0] == 1 || current[0][current.length - 1] == 1 || current[current.length - 1][0] == 1
						|| current[current.length - 1][current.length - 1] == 1) {
					corner1++;
				}
				if (current[0][0] == 2 || current[0][current.length - 1] == 2 || current[current.length - 1][0] == 2
						|| current[current.length - 1][current.length - 1] == 2) {
					corner2++;
				}
			}
		}
		if (corner1 >= 2 || corner2 >= 2) {
			for (int i = 0; i < current.length; i++) {
				for (int j = 0; j < current[i].length; j++) {
					if (current[i][j] == MyPlayer) {
						board += ending[i][j];
					} else if(current[i][j] != 0){
						board -= ending[i][j];
					}
				}
			}
		} else if (edge != 0) {
			for (int i = 0; i < current.length; i++) {
				for (int j = 0; j < current[i].length; j++) {
					if (current[i][j] == MyPlayer) {
						board += middle[i][j];
					} else if(current[i][j] != 0){
						board -= middle[i][j];
					}
				}
			}
		} else {
			for (int i = 0; i < current.length; i++) {
				for (int j = 0; j < current[i].length; j++) {
					if (current[i][j] == MyPlayer) {
						board += early[i][j];
					} else if(current[i][j] != 0){
						board -= early[i][j];
					}
				}
			}
		}
		//Third feature, mobility i.e. available moves
		double mobility = 0.0;
		int Mymove = OthelloGame.getAllLegalMoves(state.getBoard(), MyPlayer).size();
		int Hismove = OthelloGame.getAllLegalMoves(state.getBoard(), HisPlayer).size();
		mobility = 100 * ((double)Mymove - Hismove)/(Mymove + Hismove);
		Heuristic = mobility + board;
		return 66 - Heuristic;

	}

}