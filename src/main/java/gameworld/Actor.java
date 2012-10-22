package gameworld;

/*
 * An interface for interacting with the game world.
 * 
 * ---SERVER-CLIENT NOTE---
 * This interface is for influencing the world. The client should implement this
 * interface and use it to accept the opponent's moves. Reporting of our moves to the
 * server should be done with the Reporter interface (see gameworld.Reporter.java).
 * 
 * The Actor must be able to receive a WorldState from the World if the World decides
 * that the Actor has desynchronized.
 * 
 * The Actor must be able to receive updates from the World and to propose updates when
 * queried.  If the proposed action is accepted, the World will follow up with the
 * adequate update.
 */
public interface Actor {
	
    public enum Team { RED, BLUE }
    
	// The Actor is placed into the given state.
	public void receiveState( WorldState worldState );
	
	// The World in which the Actor is has been updated and the Actor is being
	// informed of that update.
	public void receiveUpdate( WorldUpdate worldUpdate );
	
	// Actor generates an action in response to the WorldState it's in. The proposition
	// of the action is not equivalent to performing that action. If the action is
	// accepted (a.k.a. performed), the Actor will be notified about it via 
	// Actor.receiveUpdate( WorldUpdate worldUpdate ) : void
	public WorldUpdate proposeAction();
	
	// returns team which the player is on
	public Team team();
	
	// returns turn number of the player (what order they take their turn in)
	public int number();
	
	public String toString();
}
