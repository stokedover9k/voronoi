package concreteWorld;

import gameworld.Actor;
import gameworld.Stone;
import gameworld.WorldState;
import gameworld.WorldUpdate;

import java.util.Scanner;

public class ServerPlayer implements Actor
{
    final String  name;
    final int     playerNumber;
    final Team    team;

    private final Scanner    scan  = new Scanner(System.in);

    public ServerPlayer(String name, int playerNumber, Team team)
    {
        this.name = name;
        this.playerNumber = playerNumber;
        this.team = team;
    }

    @Override
    public Team team()
    {
        return team;
    }
    
    @Override
    public int number()
    {
        return playerNumber;
    }

    @Override
    public WorldUpdate proposeAction()
    {
        Stone s = null;
        if (scan.hasNextLine())
        {
            //ex. 40,126 874,264 618,561 6,738 795,679 | 494767.00191,505232.998
            String input = scan.nextLine();
            System.err.println("Input from server " + input);
            String[] message = input.split("\\|");
            print("message", message);
            String[] coordinates = message[0].trim().split(" ");
            String[] stone = coordinates[0].trim().split(",");
            s = new Stone (
              Integer.parseInt(stone[0]),
              Integer.parseInt(stone[1])
            );
            System.err.println("Stone from server, " + s.toString());
        } 
        
        return new WorldUpdate(WorldUpdate.Type.PLACE_STONE, this, s);
    }

    private void print(String name, String[] arr)
    {
        System.err.println(name);
        int i = 0;
        for (String str : arr)
        {
            System.err.print("[" + i + "]=" + str);
            if (i < str.length() - 1) System.err.print(",");
            i++;
        }
        System.err.println();
    }
    
    @Override
    public void receiveState(WorldState worldState) { }

    @Override
    public void receiveUpdate(WorldUpdate worldUpdate) { }


}
