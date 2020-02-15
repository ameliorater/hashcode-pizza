import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class template {
    public static void main(String[] args) throws Exception {
        String filename = "INSERT NAME HERE";
        BufferedReader br = new BufferedReader(new FileReader(filename + ".in"));
        StringTokenizer st = new StringTokenizer(br.readLine());


        PrintWriter pw = new PrintWriter(new FileWriter(filename + ".out"));
        pw.println("OUTPUT HERE");
        pw.close();
    }
}