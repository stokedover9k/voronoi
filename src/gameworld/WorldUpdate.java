package gameworld;

/*
 * Class WorldState is a representation for whatever change might occur in a world state.
 * 
 * Also notice the static class Create used to store a number of typical updates (like
 * fail, confirm, loss, win, etc.).
 */
public class WorldUpdate {
	
	public static enum Type {
		IDLE, FAIL, CONFIRM, 
		LOSS, WIN, 
		PLACE_STONE, REMOVE_STONE
	}

	Type type = Type.IDLE;
	Actor actor = null;
	Stone stone = null;
	
	public WorldUpdate( WorldUpdate.Type type, Actor actor, Stone stone )
	{
		this.type = type;
		
		if( actor == null && type == Type.PLACE_STONE )
			throw new IllegalArgumentException("Actor expected for this type of action");
		if( stone == null && (type == Type.PLACE_STONE || type == Type.REMOVE_STONE) )
			throw new IllegalArgumentException("Stone expected for this type of action");
		this.actor = actor;
		this.stone = stone;
	}
	
	public WorldUpdate( WorldUpdate.Type type, Actor actor ) 
	{
		this( type, actor, null );
	}
	
	public WorldUpdate( WorldUpdate.Type type ) 
	{
		this( type, null );
	}
	
	

	public Actor getActor() {
		return actor;
	}
	
	public Type getType() {
		return type;
	}
	
	public Stone getStone() {
		return stone;
	}
	
	public boolean isConfirm()  { return type == Type.CONFIRM; }
	public boolean isFail()     { return type == Type.FAIL;    }
	public boolean isWin()      { return type == Type.WIN;     }
	public boolean isLoss()     { return type == Type.LOSS;    }
	public boolean isTerminal() { return isWin() || isLoss();  }

	
	
	public static class Create {
		private static final WorldUpdate fail = new WorldUpdate(Type.FAIL);
		private static final WorldUpdate confirm = new WorldUpdate(Type.CONFIRM);
		private static final WorldUpdate loss = new WorldUpdate(Type.LOSS);
		private static final WorldUpdate win = new WorldUpdate(Type.WIN);

		public static WorldUpdate fail() {
			return fail;
		}
		
		public static WorldUpdate confirm() {
			return confirm;
		}
		
		public static WorldUpdate loss() {
			return loss;
		}
		
		public static WorldUpdate win() {
			return win;
		}
	}

	public String toString() {
		return "Update{" + type + " " + stone + "}";
	}
}
