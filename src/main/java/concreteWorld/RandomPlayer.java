package concreteWorld;

import util.Vecs.Vec;
import gameworld.Actor;
import gameworld.Stone;
import gameworld.World;
import gameworld.WorldState;
import gameworld.WorldUpdate;

public class RandomPlayer implements Actor {
	
    final String name;
    final int    playerNumber;
    final Team   team;
    VoronoiGameField gameField;// this represents the actual game state
    VoronoiGameField previousgameStatefield; // this a cahced copy
    
    int lastMoveX;
    int lastMoveY;
    
	public RandomPlayer(String name, int playerNumber, Team team)
    {
        this.name = name;
        this.playerNumber = playerNumber;
        this.team = team;
        gameField = new VoronoiGameField();
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
		
		//makes a deep copy
        previousgameStatefield = new VoronoiGameField(gameField);

        //applyc hanges in previousgameStateField
        Vec nextMove = previousgameStatefield.getNextMove();
        
	    //int x = (int) (World.WIDTH  * Math.random());
		//int y = (int) (World.HEIGHT * Math.random());
        
        int x = (int) nextMove.get(0);
		int y = (int) nextMove.get(1);
		
		lastMoveX = x;
		lastMoveY = y;
		
		previousgameStatefield.placeStone(new Stone(x,y),World.getInstance().getCurrentPlayer());
		
		return new WorldUpdate(WorldUpdate.Type.PLACE_STONE, this, new Stone(x,y));
	}
	
	@Override
	public String toString() {
	    return "name=" + name + ",number=" + playerNumber + ",team=" + team;
	}

    @Override
    public void receiveState(WorldState worldState) {
    	
    	
    	
    }

    @Override
    public void receiveUpdate(WorldUpdate worldUpdate) { 
    	
    	if(worldUpdate.isConfirm()){
    		gameField.placeStone(new Stone(lastMoveX,lastMoveY),World.getInstance().getCurrentPlayer());
    	}
    	
    }

}
