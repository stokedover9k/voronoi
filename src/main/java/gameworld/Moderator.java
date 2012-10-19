package gameworld;

/*
 * The Moderator interface hides the player moderation. Having been instanciated,
 * the Moderator is expected to advance the game turn by turn via continueGame() : void.
 * 
 * After each such turn, reportLastUpdate() : WorldUpdate should represent the change
 * which occurred in the game during that turn.
 * 
 * The Moderator can also be queried about the current state of the world via
 * reportWorldState() : WorldState.
 * 
 * The Omnipotent functions are there to externally influence the world with our
 * omnipotent hands and should be used carefully (for example, to reset the game).
 */

public interface Moderator {

	public WorldUpdate receiveOmnipotentUpdate( WorldUpdate worldUpdate );
	public WorldState  receiveOmnipotentState( WorldState worldState );

	// report the last change to the world state
	public WorldUpdate reportLastUpdate();
	// report the current world state
	public WorldState  reportWorldState();
	
	// advance game by one turn
	public void continueGame();
}
