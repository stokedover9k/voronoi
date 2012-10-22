package concreteWorld;

import gameworld.Actor;
import gameworld.Stone;
import gameworld.World;
import gameworld.WorldState;
import gameworld.WorldUpdate;

public class RandomPlayer implements Actor {
	
    final String name;
    final int    playerNumber;
    final Team   team;
    
	public RandomPlayer(String name, int playerNumber, Team team)
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
	public WorldUpdate proposeAction() {
	    System.err.println(name + ": generating action");
		int x = (int) (World.WIDTH  * Math.random());
		int y = (int) (World.HEIGHT * Math.random());
		return new WorldUpdate(WorldUpdate.Type.PLACE_STONE, this, new Stone(x,y));
	}

    @Override
    public void receiveState(WorldState worldState) { }

    @Override
    public void receiveUpdate(WorldUpdate worldUpdate) { }

}
