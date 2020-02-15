import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class pizza {
    public static void main(String[] args) throws Exception {
        String filename = "d_quite_big";
        BufferedReader br = new BufferedReader(new FileReader(filename + ".in"));
        StringTokenizer st = new StringTokenizer(br.readLine());
        Scanner userInput = new Scanner(System.in);

        int m = Integer.parseInt(st.nextToken()); //max number of pizza slices to order
        int n = Integer.parseInt(st.nextToken()); //number of types of pizza
        st = new StringTokenizer(br.readLine());

        List<Integer> pizzasToOrder = new ArrayList<>(); //current iteration of list of pizzas to order
        List<Integer> bestPizzasToOrder = new ArrayList<>(); //best list of pizzas to order

        //set this!
        long secondsToRun = 5; //...before prompting

        HashMap<Integer, Integer> sliceCounts = new HashMap<>(); //key: pizza index, value: slice count
        for (int i = 0; i < n; i++) {
            sliceCounts.put(i, Integer.parseInt(st.nextToken()));
        }
        //System.out.println(sliceCounts);

        //loop until satisfactory result is achieved
        int slicesBought, bestSlicesBought = 0;
        int permsChecked = 0; //for curiosity only
        while (true) {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < secondsToRun * 1000) {
                pizzasToOrder = new ArrayList<>();
                slicesBought = 0;
                List<Integer> randomIndexList = getRandomIndexList(n);

                int currentPizzaIndex;
                do {
                    currentPizzaIndex = randomIndexList.get(pizzasToOrder.size());
                    slicesBought += sliceCounts.get(currentPizzaIndex);
                    pizzasToOrder.add(currentPizzaIndex);
                } while (slicesBought + sliceCounts.get(currentPizzaIndex) < m);
                if (slicesBought > m) slicesBought -= sliceCounts.get(currentPizzaIndex);

                //update best
                if (slicesBought > bestSlicesBought) {
                    bestSlicesBought = slicesBought;
                    bestPizzasToOrder = pizzasToOrder;
                    System.out.println("best slices bought: " + bestSlicesBought);
                }
                permsChecked++;
            }

            System.out.println("Continue? (yes/no)");
            while (!userInput.hasNext()) { }
            if (userInput.next().equals("yes")) continue;
            else break;
        }
        System.out.println("perms checked: " + permsChecked);

        //output best solution
        StringBuilder pizzasToOrderSB = new StringBuilder();
        Collections.sort(bestPizzasToOrder); //just in case they want sorted
        for (int j = 0; j < bestPizzasToOrder.size(); j++) {
            pizzasToOrderSB.append(bestPizzasToOrder.get(j)).append(" "); //add pizza to output
        }

        PrintWriter pw = new PrintWriter(new FileWriter(filename + ".out"));
        pw.println(pizzasToOrder.size()); //print number of pizzas to order
        pw.println(pizzasToOrderSB); //print pizza indexes
        pw.close();
    }

    public static List<Integer> getRandomIndexList (int length) {
        List<Integer> range = IntStream.rangeClosed(0, length - 1).boxed().collect(Collectors.toList());
        Collections.shuffle(range, new Random());
        return range;
    }
}