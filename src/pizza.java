import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class pizza {
    public static int[] sliceCounts;
    public static int n, m;

    public static void main(String[] args) throws Exception {
        String filename = "b_small";
        BufferedReader br = new BufferedReader(new FileReader(filename + ".in"));
        StringTokenizer st = new StringTokenizer(br.readLine());
        long startTime = System.currentTimeMillis();

        m = Integer.parseInt(st.nextToken()); //max number of pizza slices to order
        n = Integer.parseInt(st.nextToken()); //number of types of pizza
        st = new StringTokenizer(br.readLine());

        //set this!
        long timeLimitMS = 5000; //millis

//        CoolingSchedule schedule = new CoolingSchedule(5000, m * 0.99999999, m);
//        System.out.println("m:" + m);
//        System.out.println("initial temp: " + schedule.initialTemp);
//        System.out.println("cooling rate: " + schedule.coolingRate);

        sliceCounts = new int[n];
        for (int i = 0; i < n; i++) {
            sliceCounts[i] = Integer.parseInt(st.nextToken());
        }

        int permsTested = 0;
        double temp = 45;
        double coolingRate = 0.00126;
        double scoreDivisor = Math.ceil(.000001 * m);

        SubArray currentSA = getBestSubarray(getRandomIndexList(n));
        SubArray bestSA = currentSA;

        //loop with a bunch of random lists
        while (bestSA.sum < m && temp > 0.01 && System.currentTimeMillis() - startTime < timeLimitMS) {
            //order = getRandomIndexList(n);
            SubArray newSA = getBestSubarray(shuffleList(currentSA.order, 0.9));

            if (acceptanceProbability(currentSA.sum, newSA.sum, temp, scoreDivisor) > Math.random()) {
                currentSA = newSA;
            }

            if (currentSA.sum > bestSA.sum) {
                bestSA = currentSA;
                System.out.println("best sum: " + bestSA.sum);
                System.out.println("best order: " + bestSA.order);
                System.out.println("starting index: " + bestSA.bor);
                System.out.println("ending index: " + bestSA.eor);
                System.out.println("best list: " + bestSA.getSublist());
            }

            temp *= (1 - coolingRate);
            permsTested++;
        }
        System.out.println("runtime: " + (System.currentTimeMillis() - startTime));
        System.out.println("perms tested: " + permsTested);
        System.out.println("final temp: " + temp);

        //output best solution
        StringBuilder pizzasToOrderSB = new StringBuilder();
        List<Integer> bestList = bestSA.getSublist();
        Collections.sort(bestList); //just in case they want sorted
        for (int j = 0; j < bestList.size(); j++) {
            pizzasToOrderSB.append(bestList.get(j)).append(" "); //add pizza to output
        }

        PrintWriter pw = new PrintWriter(new FileWriter(filename + ".out"));
        pw.println(bestList.size()); //print number of pizzas to order
        pw.println(pizzasToOrderSB); //print pizza indexes
        pw.close();
    }

    public static SubArray getBestSubarray (List<Integer> order) {
        //sum first elements until you reach max (m)
        int sum = 0, bor = 0, eor = 0; //beginning and end of included range
        while (sum + sliceCounts[order.get(eor)] < m) {
            sum += sliceCounts[order.get(eor)];
            eor++;
        }

        SubArray bestSA = new SubArray(bor, eor, order);
        while (eor + 1 < n) {
            //slide right end of window while sum is still < m
            while (SubArray.getSum(bor, eor, order) < m) {
                eor++;
                System.out.println("bor: " + bor + " eor: " + eor);
            }
            //slide left end of window until sum is below m
            while (SubArray.getSum(bor, eor, order) > m) {
                bor++;
                System.out.println("bor: " + bor + " eor: " + eor);
            }
        }

        return bestSA;
    }

    public static List<Integer> getRandomIndexList (int length) {
        List<Integer> range = IntStream.rangeClosed(0, length - 1).boxed().collect(Collectors.toList());
        Collections.shuffle(range, new Random());
        return range;
    }

    //shuffleRatio should be a number between 0 and 1 (represents no shuffle -> complete shuffle)
    public static List<Integer> shuffleList (List<Integer> list, double shuffleRatio) {
        for (int i = 0; i < list.size() * shuffleRatio; i++) {
            int pos1 = (int) (Math.random() * list.size());
            int pos2 = (int) (Math.random() * list.size());
            Collections.swap(list, pos1, pos2);
        }
        return list;
    }

    public static double acceptanceProbability (double currentMean, double newMean, double temp, double scoreDivisor) {
        if (newMean > currentMean) return 1.0; //better, accept automatically
        return Math.exp((newMean - currentMean) / scoreDivisor / temp);
    }
}

class SubArray {
    int bor, eor, sum;
    List<Integer> order;
    public SubArray (int bor, int eor, List<Integer> order, int sum) {
        this.bor = bor;
        this.eor = eor;
        this.order = order;
        this.sum = sum;
    }

    public SubArray (int bor, int eor, List<Integer> order) {
        this(bor, eor, order, getSum(bor, eor, order));
    }

    public List<Integer> getSublist () {
        return order.subList(bor, eor+1);
    }

    //takes inclusive eor (end of range)
    public static int getSum (int bor, int eor, List<Integer> order) {
        return order.subList(bor, eor + 1).stream().mapToInt(i -> pizza.sliceCounts[i]).sum();
    }
}

//todo: make less dependent on score range
class CoolingSchedule {
    double initialTemp, coolingRate;
    double endTemp = 0.05;
    public CoolingSchedule (int iterations, double lowScore, double highScore) {
        double scoreRange = Math.abs(lowScore - highScore);
        coolingRate = 1 - (Math.pow(endTemp * 0.5 / scoreRange, 1 / (0.5 * iterations)));
        initialTemp = scoreRange / (Math.pow(1 - coolingRate, iterations / 2.0) * 0.5);
    }

    public CoolingSchedule (long millisToRun, long millisPerIteration, double lowScore, double highScore) {
        this((int)(millisToRun / millisPerIteration), lowScore, highScore);
    }
}