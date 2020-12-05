import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//record current heroPool and heroes that two teams have picked
public class State {
	
	// record hero data
	private HeroData heroData;
	
	private Set<String> heroPool = new HashSet<>();
	private List<String> picked1 = new ArrayList<>();
	private List<String> picked2 = new ArrayList<>();
	
	public State(HeroData heroData, Set<String> heroPool, List<String> picked1, List<String> picked2) {
		this.heroData = heroData;
		this.heroPool = heroPool;
		this.picked1 = picked1;
		this.picked2 = picked2;
		
	}
	
	public List<String> getPool(){
		return new ArrayList<String>(heroPool);
	}
	
	// update state when banning or picking is executed
	public void update(boolean isPick, boolean isTeam1, String hero) {
		heroPool.remove(hero);
		if (isPick) {
			if (isTeam1) {
				picked1.add(hero);
			} else {
				picked2.add(hero);
			}
		}
	}
	
	// recover state after "minimax" recursion is done
	public void recover(boolean isPick, boolean isTeam1, String hero) {
		heroPool.add(hero);
		if (isPick) {
			if (isTeam1) {
				picked1.remove(hero);
			} else {
				picked2.remove(hero);
			}
		}
	}
	
	// get final difference of 1 against 2
	public double diff() {
		double value = 0;
		for (int i = 0; i < 5; i++) {
			value += heroData.getStrength(picked1.get(i));
			value -= heroData.getStrength(picked2.get(i));
			for (int j = 0 ; j < 5; j++) {
				if (j > i) {
					value += heroData.getSynergyRate(picked1.get(i), picked1.get(j));
					value -= heroData.getSynergyRate(picked2.get(i), picked2.get(j));
				}
				value += heroData.getCounterRate(picked1.get(i), picked2.get(j));
			}
		}
		return value;
	}
}
