package gameworld;

import java.util.LinkedList;

/*
 * World is a SINGLETON implementation of Moderator. It maintains two Actor players,
 * an instance of the WorldState, and a sequence of WorldUpdates received (AND ACCEPTED)
 * from the players.
 */

public class World implements Moderator {
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;

	private static World world = new World();
	
	private Actor player1;
	private Actor player2;	
	private WorldState worldState = new WorldState();
	
	private LinkedList<WorldUpdate> updateSequence = new LinkedList<WorldUpdate>();
	
	private Actor currentPlayer = null;
	private Actor warnedPlayer = null;
	
	private World() { }
	
	public static World getInstance() {
		if( world == null )
			// use initWorld to initialize world with players first
			throw new RuntimeException("World has not been initialized");
		return world;
	}
	
	// initialize the world with two players
	public static void initWorld(Actor player1, Actor player2) {
		if( player1 == player2 ) {
			throw new IllegalArgumentException("cannot have same player for both roles");
		}
		
		world.player1 = player1;
		world.player2 = player2;
		world.currentPlayer = player1;
	}
	
	public static Actor getPlayer1() {
		return world.player1;
	}
	
	public static Actor getPlayer2() {
		return world.player2;
	}
	
	public static Actor getOpponentOf(Actor player) {
		if( player == getPlayer1() )  return getPlayer2();
		if( player == getPlayer2() )  return getPlayer1();
		throw new IllegalArgumentException("No such player");
	}
	
	@Override
	public WorldUpdate receiveOmnipotentUpdate(WorldUpdate worldUpdate) {
		return world.worldState.applyUpdate(worldUpdate);
	}

	@Override
	public WorldState receiveOmnipotentState(WorldState worldState) {
		world.worldState = worldState;
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
		
		WorldUpdate action = currentPlayer.proposeAction();
		WorldUpdate result = worldState.applyUpdate(action);
		
		if( result.isConfirm() )
		{
			updateSequence.add(action);
			warnedPlayer = null;
			
			// inform the players of world change
			player1.receiveUpdate(action);
			player2.receiveUpdate(action);
			
			currentPlayer = World.getOpponentOf(currentPlayer);
		}
		else  // failed to apply action
		{
			if( warnedPlayer != currentPlayer )      //first offense
			{
				currentPlayer.receiveUpdate(result);
				currentPlayer.receiveState(worldState);
				warnedPlayer = currentPlayer;
			}
			else                                     //second offense
			{
				currentPlayer.receiveUpdate(WorldUpdate.Create.loss());
				currentPlayer = World.getOpponentOf(currentPlayer);
				currentPlayer.receiveUpdate(WorldUpdate.Create.win());
				
				updateSequence.add(WorldUpdate.Create.loss());
			}
		}
		
	}
	
}
