package concreteWorld;

import gameworld.Actor;
import gameworld.Stone;
import gameworld.World;
import gameworld.WorldState;
import gameworld.WorldUpdate;

public class RandomPlayer implements Actor {
	
	final String name;
	
	WorldState state = new WorldState();
	
	public RandomPlayer(String name) {
		this.name = name;
	}

	@Override
	public void receiveState(WorldState worldState) {
		this.state = worldState;
		System.err.println(name + ": State received");
	}

	@Override
	public void receiveUpdate(WorldUpdate worldUpdate) {
		if( this.state.applyUpdate(worldUpdate) == null)
			throw new RuntimeException(name + ": could not apply state");
		System.err.println(name + ": applied update successfully");
	}

	@Override
	public WorldUpdate proposeAction() {
	    System.err.println(name + ": generating action");
		int x = (int) (World.WIDTH  * Math.random());
		int y = (int) (World.HEIGHT * Math.random());
		return new WorldUpdate(WorldUpdate.Type.PLACE_STONE, this, new Stone(x,y));
	}

}
