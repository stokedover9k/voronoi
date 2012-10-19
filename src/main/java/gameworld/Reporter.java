package gameworld;

/*
 * The Reporter interface is intended for spying on different objects in the world.
 * Such a spy should be implemented for the local player and used to report our moves
 * to the server at appropriate times.
 * Also, we may consider implementing a logger in the same way.
 */

public interface Reporter {
	
	public WorldUpdate reportUpdateToViewer();
	public WorldState  reportStateToViewer();
}
