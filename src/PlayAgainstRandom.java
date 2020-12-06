import java.io.File;
import java.util.*;

public class PlayAgainstRandom {
    static final String ANTI_FILE = "anti.txt";
    static final String COMB_FILE = "comb.txt";
    static final String WIN_RATE_FILE = "winRate.txt";

    static final String INPUT_FILE = "input.txt";

    public static void main(String[] args) {
        HeroData heroData = new HeroData(ANTI_FILE, COMB_FILE, WIN_RATE_FILE);
        List<String> input = IOHelper.getInstance().readFromFileToStringList(new File(INPUT_FILE));
        if (input.size() != 4) System.err.println("Wrong input");

        ArrayList<String> heros = new ArrayList<>(heroData.winRate.keySet());

        final int currentDepth = 0;
        final int depth =  2;

        final int repeat = 100;

        final int heroNum = 22;
        Random random = new Random();
        int win = 0;

        for (int time = 0; time < repeat; time++) {
            ArrayList<String> heroPoolArray = new ArrayList<>();
            Set<String> heroPool;
            List<String> picked1 = new ArrayList<>();
            List<String> picked2 = new ArrayList<>();

            //get heroPool
            for (int i = 0; i < heroNum; ) {
                int next = Math.abs(random.nextInt()) % heros.size();
                if (!heroPoolArray.contains(heros.get(next))) {
                    heroPoolArray.add(heros.get(next));
                    i++;
                }
            }

            heroPool = new HashSet<>(heroPoolArray);

            State state = new State(heroData, heroPool, picked1, picked2);

            MinimaxPruning minimaxPruning = new MinimaxPruning();
            for (int round = 1; round < 7; round++) {
                String[] banPick = minimaxPruning.nextStep(state, round, currentDepth, depth);
                //banPick[0] banned, banPick[1] picked
                if (banPick[0] != null) {
                    state.update(false, true, banPick[0]);
                }
                if (banPick[1] != null) {
                    state.update(true, true, banPick[1]);
                }

                //team2's random ban pick
                boolean isPick = round % 2 == 0;
                String firstPick = state.getPool().get(Math.abs(random.nextInt()) % state.getPool().size());
                state.update(isPick, false, firstPick);
                // in round 6, team 2 only needs to pick 1 hero
                if (round != 6) {
                    String secondPick = state.getPool().get(Math.abs(random.nextInt()) % state.getPool().size());
                    state.update(isPick, false, secondPick);
                }
            }
            if (state.diff() > 0) {
                win++;
                System.out.println(state.diff());
            }
        }
        System.out.println(win + "/" + repeat);
    }
}