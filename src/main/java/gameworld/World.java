package gameworld;

import java.util.LinkedList;

/*
 * World is a SINGLETON implementation of Moderator. It maintains two Actor players,
 * an instance of the WorldState, and a sequence of WorldUpdates received (AND ACCEPTED)
 * from the players.
 */

public class World implements Moderator {
	
	static final int worldWidth = 1000;
	static final int worldHeight = 1000;

	static World instance = null;
	
	Actor player1 = null;
	Actor player2 = null;
	
	WorldState worldState;
	
	LinkedList<WorldUpdate> updateSequence = new LinkedList<WorldUpdate>();
	
	Actor turnOfPlayer = null;
	Actor warnedOfError = null;
	
	private World() { 
	}
	
	public static World getInstance() {
		if( instance == null )
			// use initWorld to initialize world with players first
			throw new RuntimeException("World has not been instanciated");
		return instance;
	}
	
	// initialize the world with two players
	public static void initWorld(Actor player1, Actor player2) {
		
		instance = new World();
		
		if( player1 == player2 ) {
			throw new IllegalArgumentException("cannot have same player for both roles");
		}
		
		instance.player1 = player1;
		instance.player2 = player2;
		
		instance.turnOfPlayer = player1;
		
		instance.worldState = new WorldState();
	}
	
	public static Actor getPlayer1() {
		return instance.player1;
	}
	
	public static Actor getPlayer2() {
		return instance.player2;
	}
	
	public static Actor getOpponentOf(Actor player) {
		if( player == getPlayer1() )  return getPlayer2();
		if( player == getPlayer2() )  return getPlayer1();
		throw new IllegalArgumentException("No such player");
	}
	
	
	
	@Override
	public WorldUpdate receiveOmnipotentUpdate(WorldUpdate worldUpdate) {
		return instance.worldState.applyUpdate(worldUpdate);
	}

	@Override
	public WorldState receiveOmnipotentState(WorldState worldState) {
		instance.worldState = worldState;
		return worldState;
	}

	@Override
	public WorldUpdate reportLastUpdate() {
		return updateSequence.getLast(); 
	}

	@Override
	public WorldState reportWorldState() {
		return worldState;
	}

	@Override
	public void continueGame() {
		
		WorldUpdate action = turnOfPlayer.proposeAction();
		WorldUpdate result = worldState.applyUpdate(action);
		
		if( result.isConfirm() )
		{
			updateSequence.add(action);
			warnedOfError = null;
			
			// inform the players of world change
			player1.receiveUpdate(action);
			player2.receiveUpdate(action);
			
			turnOfPlayer = World.getOpponentOf(turnOfPlayer);
		}
		else  // failed to apply action
		{
			if( warnedOfError != turnOfPlayer )      //first offense
			{
				turnOfPlayer.receiveUpdate(result);
				turnOfPlayer.receiveState(worldState);
				warnedOfError = turnOfPlayer;
			}
			else                                     //second offense
			{
				turnOfPlayer.receiveUpdate(WorldUpdate.Create.loss());
				turnOfPlayer = World.getOpponentOf(turnOfPlayer);
				turnOfPlayer.receiveUpdate(WorldUpdate.Create.win());
				
				updateSequence.add(WorldUpdate.Create.loss());
			}
		}
		
	}
	
	public static double getWidth() {
		return worldWidth;
	}
	
	public static double getHeight() {
		return worldHeight;
	}
	
}
