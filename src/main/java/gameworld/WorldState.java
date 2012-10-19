package gameworld;

import java.util.Set;
import java.util.TreeSet;

public class WorldState implements Comparable<WorldState> {

	Set<Stone> redStones = new TreeSet<Stone>();
	Set<Stone> blueStones = new TreeSet<Stone>();
	
	protected Set<Stone> getStones(Actor player) {
		if( player == World.getPlayer1() )  return redStones;
		if( player == World.getPlayer2() )  return blueStones;
		throw new IllegalArgumentException("wrong Actor provided");
	}
	
	// applies the update to the current state and returns a .Create.confirm() update or
	// a .Create.fail() update depending on whether it succeeds or fails.
	public WorldUpdate applyUpdate( WorldUpdate update ) {
		if( update.getType() == WorldUpdate.Type.PLACE_STONE ) {
			if( addStone( update.getActor(), update.getStone() ) )
				return WorldUpdate.Create.confirm();
			else
				return WorldUpdate.Create.fail();
		}
		
		if( update.getType() == WorldUpdate.Type.REMOVE_STONE ) {
			if( removeStone(update.getActor(), update.getStone()) )
				return WorldUpdate.Create.confirm();
			else
				return WorldUpdate.Create.fail();
		}
		
		throw new IllegalArgumentException("Wrong type of update requested");
	}
	
	// returns false if stone is already added by either player, true otherwise
	protected boolean addStone( Actor player, Stone stone ) {
		if( redStones.contains(stone) || blueStones.contains(stone) )
			return false;
		getStones(player).add(stone);
		return true;
	}
	
	protected boolean removeStone( Actor player, Stone stone ) {
		return getStones(player).remove(stone);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("WorldState{");
		sb.append(redStones);
		sb.append(" ");
		sb.append(blueStones);
		sb.append("}");
		return sb.toString();
	}

	@Override
	public int compareTo(WorldState s) {
		//TODO:  any better ideas for this?  Perhaps a state "utility" evaluation function?
		if( redStones.equals(s.redStones) && blueStones.equals(s.blueStones) )
			return 0;
		int dif = redStones.hashCode() - s.redStones.hashCode();
		if( dif != 0 )  return dif;
		return blueStones.hashCode() - s.blueStones.hashCode();
	}
}
