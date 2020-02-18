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
        String filename = "e_also_big";
        BufferedReader br = new BufferedReader(new FileReader(filename + ".in"));
        StringTokenizer st = new StringTokenizer(br.readLine());
        long startTime = System.currentTimeMillis();

        m = Integer.parseInt(st.nextToken()); //max number of pizza slices to order
        n = Integer.parseInt(st.nextToken()); //number of types of pizza
        st = new StringTokenizer(br.readLine());

        //set this!
        long msToRun = 10000;

        sliceCounts = new int[n];
        for (int i = 0; i < n; i++) {
            sliceCounts[i] = Integer.parseInt(st.nextToken());
        }

        int bestSum = 0;
        List<Integer> bestList = new ArrayList<>();

        //loop with a bunch of random lists
        while (System.currentTimeMillis() - startTime < msToRun) {
            List<Integer> randomOrder = getRandomIndexList(n);
            //sum first elements until you reach max (m)
            int sum = 0, bor = 0, eor = 0; //beginning and end of included range
            while (sum + sliceCounts[randomOrder.get(eor)] < m) {
                sum += sliceCounts[randomOrder.get(eor)];
                eor++;
            }

            //slide right end of window while sum is still < m
            while (getSum(bor, eor, randomOrder) < m && eor + 1 < n) eor++;
            //slide left end of window until sum is below m
            while (getSum(bor, eor, randomOrder) > m && bor + 1 < eor) bor++;

            //update if sum is better than the best sum we've seen so far (doesn't matter per random list)
            sum = getSum(bor, eor, randomOrder);
            if (sum > bestSum) {
                bestSum = sum;
                bestList = randomOrder.subList(bor, eor+1); //+1 bc exclusive
                System.out.println("best sum: " + bestSum);
                //System.out.println("best list: " + bestList);
            }
        }

        //output best solution
        StringBuilder pizzasToOrderSB = new StringBuilder();
        Collections.sort(bestList); //just in case they want sorted
        for (int j = 0; j < bestList.size(); j++) {
            pizzasToOrderSB.append(bestList.get(j)).append(" "); //add pizza to output
        }

        PrintWriter pw = new PrintWriter(new FileWriter(filename + ".out"));
        pw.println(bestList.size()); //print number of pizzas to order
        pw.println(pizzasToOrderSB); //print pizza indexes
        pw.close();
    }

    //takes inclusive eor (end of range)
    public static int getSum (int bor, int eor, List<Integer> order) {
        return order.subList(bor, eor + 1).stream().mapToInt(i -> sliceCounts[i]).sum();
    }

    public static List<Integer> getRandomIndexList (int length) {
        List<Integer> range = IntStream.rangeClosed(0, length - 1).boxed().collect(Collectors.toList());
        Collections.shuffle(range, new Random());
        return range;
    }
}