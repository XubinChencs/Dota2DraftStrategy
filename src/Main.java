import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
	static final String ANTI_FILE = "anti.txt";
	static final String COMB_FILE = "comb.txt";
	static final String WIN_RATE_FILE = "winRate.txt";
	
	static final String INPUT_FILE = "input.txt";
	
	public static void main(String[] args) {
		HeroData heroData = new HeroData(ANTI_FILE, COMB_FILE, WIN_RATE_FILE);
		List<String> input = IOHelper.getInstance().readFromFileToStringList(new File(INPUT_FILE));
		if (input.size() != 4) System.err.println("Wrong input");
		
		Set<String> heroPool = new HashSet<>();
		List<String> picked1 = new ArrayList<>();
		List<String> picked2 = new ArrayList<>();
		int round;
		
		for (String hero : input.get(0).split(" ")) {
			heroPool.add(hero);
		}
		
		if (input.get(1).length() > 0) {
			for (String hero : input.get(1).split(" ")) {
				picked1.add(hero);
			}
		}
		//System.out.println(picked1.size());
		if (input.get(2).length() > 0) {
			for (String hero : input.get(2).split(" ")) {
				picked2.add(hero);
			}
		}
		//System.out.println(picked2.size());
		round = Integer.parseInt(input.get(3));
		State state = new State(heroData, heroPool, picked1, picked2);
		
		Minimax minimax = new Minimax();
		String[] res = minimax.nextStep(state, round);
		System.out.println("ban: " + res[0] + ", pick: " + res[1]);
	}
}
