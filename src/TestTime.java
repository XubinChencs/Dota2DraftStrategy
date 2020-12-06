import java.io.File;
import java.util.*;

public class TestTime {
    static final String ANTI_FILE = "anti.txt";
    static final String COMB_FILE = "comb.txt";
    static final String WIN_RATE_FILE = "winRate.txt";

    static final String INPUT_FILE = "input.txt";

    public static void main(String[] args) {
        HeroData heroData = new HeroData(ANTI_FILE, COMB_FILE, WIN_RATE_FILE);
        List<String> input = IOHelper.getInstance().readFromFileToStringList(new File(INPUT_FILE));
        if (input.size() != 4) System.err.println("Wrong input");

        ArrayList<String> heros = new ArrayList<>(heroData.winRate.keySet());

        //start with round 1
        //end with round 3
        //2 heros each
        final int round = 1;
        final int currentDepth = 0;
        final int depth = 6;

        for (int heroNum = 11; heroNum < 50; heroNum++) {
            double minimaxTime = 0; //milliseconds
            double minimaxPruningTime = 0; //milliseconds
            for (int time = 0; time < 5; time++) {
                ArrayList<String> heroPoolArray = new ArrayList<>();
                Set<String> heroPool;
                List<String> picked1 = new ArrayList<>();
                List<String> picked2 = new ArrayList<>();

                //get heroPool
                Random random = new Random();
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
                Minimax minimax = new Minimax();

                final int repeat = 1;

                long startTime = System.nanoTime();
                for (int i = 0; i < repeat; i++) {
//                    minimax.nextStep(state, round, currentDepth, depth);
//            String[] res = minimax.nextStep(state, round);
//            System.out.println("ban: " + res[0] + ", pick: " + res[1]);
                }
                long stopTime = System.nanoTime();
//                System.out.println(heroNum);
//                System.out.println("minimax");
                minimaxTime += (stopTime - startTime) / 1e6 / repeat;
//                System.out.println((stopTime - startTime) / 1e6);

                startTime = System.nanoTime();
                for (int i = 0; i < repeat; i++) {
                    minimaxPruning.nextStep(state, round, currentDepth, depth);
                }
                stopTime = System.nanoTime();
//                System.out.println("minimax pruning");
                minimaxPruningTime += (stopTime - startTime) / 1e6 / repeat;
//                System.out.println((stopTime - startTime) / 1e6);
            }
            System.out.println(heroNum + ":");
            System.out.println("minimax: " + minimaxTime / 5);
            System.out.println("pruning: " + minimaxPruningTime / 5);
        }
    }
}
