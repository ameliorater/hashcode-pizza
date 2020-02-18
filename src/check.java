import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.BitSet;
import java.util.StringTokenizer;

public class check {
    public static void main(String[] args) throws Exception {
        String filename = "b_small";
        BufferedReader br = new BufferedReader(new FileReader("bestSolutions/" + filename + ".out"));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());

        BitSet mask = new BitSet();
        int maxPizzaIndex = 0;
        for (int i = 0; i < n; i++) {
            int pizzaIndex = Integer.parseInt(st.nextToken());
            mask.set(pizzaIndex);
            maxPizzaIndex = Math.max(pizzaIndex, maxPizzaIndex);
        }

        //read in 'in' file to get slice counts
        br = new BufferedReader(new FileReader(filename + ".in"));
        st = new StringTokenizer(br.readLine());

        int maxSlices = Integer.parseInt(st.nextToken());
        int totalPizzas = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());

        int sum = 0;
        for (int i = 0; i <= maxPizzaIndex; i++) {
            int slices = Integer.parseInt(st.nextToken());
            //System.out.println("mask: " + ((mask & (1 << i)) != 0 ? 1 : 0) + "  slices: " + slices);
            sum += (mask.get(i) ? 1 : 0) * slices;
        }
        System.out.println("sum: " + sum + "  max: " + maxSlices);
    }
}
