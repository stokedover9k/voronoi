package gameworld;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import concreteWorld.Main;

/*
 * World is a SINGLETON implementation of Moderator. It maintains two Actor players,
 * an instance of the WorldState, and a sequence of WorldUpdates received (AND ACCEPTED)
 * from the players.
 */

public class World implements Moderator {
	
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 1000;
	private static World world = new World();
	
	private WorldState worldState = new WorldState();
    private List<Actor> players = new ArrayList<Actor>();
	private LinkedList<WorldUpdate> updateSequence = new LinkedList<WorldUpdate>();
	
	private Actor currentPlayer;
	private Actor warnedPlayer;
	
	private World() { }
	
	public static World getInstance() {
		return world;
	}
	
	public void addPlayer(Actor player) {
	    if(player.number() == 1) {
	        currentPlayer = player;
	    }
	    players.add(player);
	}
	
    public Actor getPlayerByNumber(int playerNumber)
    {
        for(Actor a : this.players) {
            if(a.number() == playerNumber)
                return a;
        }
        throw new RuntimeException("Player number invalid " + playerNumber);
    }
	
    public Actor getNextPlayer(Actor player)
    {
        int nextPlayerNumber = player.number() + 1;
        if (nextPlayerNumber > Main.GAME_PARAMS.numPlayers) 
            nextPlayerNumber = 1;
        return getPlayerByNumber(nextPlayerNumber);
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
    public void continueGame()
    {

        WorldUpdate update = currentPlayer.proposeAction();
        WorldUpdate result = worldState.applyUpdate(update);

        if (result.isConfirm())
        {
            updateSequence.add(update);
            notifyAllPlayers(update);
            currentPlayer = getNextPlayer(currentPlayer);
            warnedPlayer = null;
        }
        else
        {
            if (warnedPlayer != currentPlayer) // first offense
            {
                currentPlayer.receiveUpdate(result);
                currentPlayer.receiveState(worldState);
                warnedPlayer = currentPlayer;
            }
            else // second offense
            {
                currentPlayer.receiveUpdate(WorldUpdate.Create.loss());
                currentPlayer = getNextPlayer(currentPlayer);
                currentPlayer.receiveUpdate(WorldUpdate.Create.win());

                updateSequence.add(WorldUpdate.Create.loss());
            }
        }
    }
	
	private void notifyAllPlayers(WorldUpdate update) {
	  for(Actor a : this.players) {
	      a.receiveUpdate(update);
	  }   
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
	
}
