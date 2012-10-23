package concreteWorld;

import gameworld.Actor;
import gameworld.Stone;
import gameworld.World;
import gameworld.WorldState;
import gameworld.WorldUpdate;

public class SymetricPlayer implements Actor
{
    final String name;
    final int    playerNumber;
    final Team   team;
    
    private boolean isFirstMove = true;
    
    public SymetricPlayer(String name, int playerNumber, Team team)
    {
        this.name = name;
        this.playerNumber = playerNumber;
        this.team = team;
    }
    
    @Override
    public WorldUpdate proposeAction()
    {
        if (playerNumber == 1 && isFirstMove)
        {
            isFirstMove = false;
            return new WorldUpdate(WorldUpdate.Type.PLACE_STONE, this, new Stone(500, 500));
        }

        World world = World.getInstance();
        WorldUpdate update = world.reportLastUpdate();
        Stone s = new Stone(update.getStone());

        do
        {
            s.x = s.x + 1;
            s.y = s.y + 1;
        }
        while (isStoneOnGrid(s));

        if (s.x > 1000 || s.y > 1000)
        {
            do
            {
                s.x = (int) (World.WIDTH  * Math.random());
                s.y = (int) (World.HEIGHT * Math.random());
            }
            while (isStoneOnGrid(s));
        }

        return new WorldUpdate(WorldUpdate.Type.PLACE_STONE, this, s);
    }
    
    private boolean isStoneOnGrid(Stone s)
    {
        World world = World.getInstance();
        for (WorldUpdate u : world.getUpdateSequence())
        {
            if (u.getStone().equals(s))
            {
                return true;
            }
        }
        return false;
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
    public void receiveState(WorldState worldState)
    {
    }

    @Override
    public void receiveUpdate(WorldUpdate worldUpdate)
    {
    }

}
