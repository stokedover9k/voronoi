package concreteWorld;

import gameworld.Actor;
import gameworld.WorldState;
import gameworld.WorldUpdate;

import java.util.Scanner;

public class ServerPlayer implements Actor
{
    private final String     name;
    private final Scanner    scan  = new Scanner(System.in);
    private WorldState state = new WorldState();

    public ServerPlayer(String name)
    {
        this.name = name;
    }

    @Override
    public void receiveState(WorldState worldState)
    {
        this.state = worldState;
        System.err.println(name + ": State received");
    }

    @Override
    public void receiveUpdate(WorldUpdate worldUpdate)
    {
        if (this.state.applyUpdate(worldUpdate) == null) 
            throw new RuntimeException(name + ": could not apply state");
        System.err.println(name + ": applied update successfully");
    }

    @Override
    public WorldUpdate proposeAction()
    {
        WorldUpdate u = new WorldUpdate(WorldUpdate.Type.PLACE_STONE, this);
        if (scan.hasNextLine())
        {
            String input = scan.nextLine();
            System.err.println("Got from server: " + input);
            // TODO - take the first two tokens and build update
        }
        return u;
    }

}
