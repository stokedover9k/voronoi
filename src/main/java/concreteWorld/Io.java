package concreteWorld;

import java.util.Random;
import java.util.Scanner;

public class Io
{

    public static void main(String[] args) throws Exception
    {
        System.out.println("rky");
        
        Scanner scan = new Scanner(System.in);
        while (scan.hasNext()) // stop when all stones placed?
        {
            String input = scan.nextLine();
            System.err.println("Got from server: " + input);
            // parse stone placements and score
            int x = Math.abs(new Random().nextInt() % 1000);
            int y = Math.abs(new Random().nextInt() % 1000);
            System.out.println(x + "," + y);
            System.err.println("Outputting to server: " + (x + "," + y));
        }

        scan.close();
    }
}