import java.util.List;

// with alpha-beta pruning
public class MinimaxPruning {
	private double minimax(State state, double alpha, double beta, int round, boolean isTeam1) {
		// leaf node
		if (round == 7) {
			return state.diff();
		}
		
		List<String> curHeroPool = state.getPool();
		
		if (isTeam1) {
			double value = Double.MIN_VALUE;
			for (String banned : curHeroPool) {    // ban 1 hero
				state.update(false, true, banned);
				// in round 1, team 1 only needs to ban 1 hero
				if (round == 1) {
					value = Math.max(value, minimax(state, alpha, beta, round, false));
					alpha = Math.max(alpha,  value);
				} else {
					for (String picked : curHeroPool) { // pick 1 hero
						if (!picked.equals(banned)) {
							state.update(true, true, picked);
							value = Math.max(value,  minimax(state, alpha, beta, round, false));
							state.recover(true, true, picked);
							alpha = Math.max(alpha,  value);
							if (alpha >= beta) break;
						}
					}
				}
				state.recover(false, true, banned);
				if (alpha > beta) break;
			}
			return value;
		} else { // Team2
			double value = Double.MAX_VALUE;
			boolean isPick = round % 2 == 0; ////
			for (int i = 0; i < curHeroPool.size(); i++) { // pick/ban first hero
				String firstPick = curHeroPool.get(i);
				state.update(isPick, false, firstPick);
				// in round 6, team 2 only needs to pick 1 hero
				if (round == 6) {
					value = Math.min(value,  minimax(state, alpha, beta, round + 1, true));
					beta = Math.min(beta,  value);
				} else {
					for (int j = i + 1; j < curHeroPool.size(); j++) { // pick/ban second hero
						String secondPick = curHeroPool.get(j);
						state.update(isPick, false, secondPick);
						value = Math.min(value,  minimax(state, alpha, beta, round + 1, true));
						state.recover(isPick, false, secondPick);
						beta = Math.min(beta,  value);
						if (beta <= alpha) break;
					}
				}
				state.recover(isPick, false, firstPick);
				if (beta <= alpha) break;
			}
			return value;
		}
	}
	
	public String[] nextStep(State state, int round) {
		double value = Double.MIN_VALUE;  
		double alpha = Double.MIN_VALUE;
		double beta = Double.MAX_VALUE;
		String[] banPick = new String[2];//banPick[0]: banned hero; banPick[1]: picked hero
		List<String> curHeroPool = state.getPool();
		for (String banned : curHeroPool) {
			state.update(false, true, banned);
			// In round 1, Team 1 only need to ban 1 hero;
			if (round == 1) {
				double newValue = minimax(state, alpha, beta, round, false);
				if (value < newValue) {
					value = newValue;
					banPick[0] = banned;
				}
			} else {
				for (String picked : curHeroPool) {
					if (!picked.equals(banned)) {
						state.update(true, true, picked);
						double newValue = Math.max(value,  minimax(state, alpha, beta, round, false));
						if (value < newValue) {
							value = newValue;
							alpha = value;
							banPick[0] = banned;
							banPick[1] = picked;
						}
						state.recover(true, true, picked);
					}
				}
			}
			state.recover(false, true, banned);
		}
		return banPick;
	}
}
