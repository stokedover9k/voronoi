package concreteWorld;

import gameworld.Actor;
import gameworld.Moderator;
import gameworld.Reporter;
import gameworld.WorldState;
import gameworld.WorldUpdate;

public class ServerReporter implements Reporter
{
    final Moderator moderator;
    Actor           player;

    public ServerReporter(Moderator mod, Actor player)
    {
        this.moderator = mod;
    }

    @Override
    public WorldUpdate reportUpdateToViewer()
    {
        WorldUpdate u = moderator.reportLastUpdate();
        if (u.getActor() == player)
        {
            System.out.println(u.getStone().x + "," + u.getStone().y);
        }
        
        return u;
    }

    @Override
    public WorldState reportStateToViewer()
    {
        return moderator.reportWorldState();
    }

}
