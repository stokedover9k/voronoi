package concreteWorld;

import gameworld.Actor;
import gameworld.Actor.Team;
import gameworld.Reporter;
import gameworld.World;
import gameworld.WorldUpdate;
import concreteWorld.Main.GameParams.Mode;

public class Main {

    public static GameParams GAME_PARAMS;
    
    //args = #STONES PLAYER# MODE(M=MELEE,T=TWO_PLAYER)
	public static void main(String[] args) 
	{
	    GAME_PARAMS = parseParams(args);
	    World world = World.getInstance();
	    
	    Actor us = new SymetricPlayer (
	                "rky", 
	                GAME_PARAMS.playerNumber == 1 ? 1 : 2, 
	                GAME_PARAMS.playerNumber == 1 ? Team.RED : Team.BLUE 
	    );
	    
	    System.err.println("Created ourselves as a symetric player. " + us.toString());
        
        Actor them = new ServerPlayer (
                "them", 
                GAME_PARAMS.playerNumber == 1 ? 2 : 1, 
                GAME_PARAMS.playerNumber == 1 ? Team.BLUE : Team.RED
        );
        
        System.err.println("Created opponent as a server player. " + them.toString());
        
        world.addPlayer(us);
        world.addPlayer(them);
        
        System.out.println("rky");
        
		Reporter stdErrReporter = new StdOutReporter(world);
		for(int i = 0 ; i < GAME_PARAMS.numStones*2 ; ++i)
		{
		    System.err.println("Current player is " + world.getCurrentPlayer().toString());
			world.continueGame();
			stdErrReporter.reportStateToViewer();
			
            WorldUpdate u = world.reportLastUpdate();
            if (u.getActor() == us)
            {
                System.out.println(u.getStone().x + "," + u.getStone().y + " ");
            }
		    
			if( world.reportLastUpdate().isTerminal() ) {
				System.err.println("End game");
				break;
			}
		}
	}
	
	public static class GameParams
    {
        public enum Mode { TWO_PLAYER, MELEE }

        public final int    numPlayers;
        public final int    numStones;
        public final int    playerNumber;
        public final Mode   mode;

        public GameParams(int numStones, int numPlayers, int playerNumber, Mode mode)
        {
            this.numPlayers = numPlayers;
            this.numStones = numStones;
            this.playerNumber = playerNumber;
            this.mode = mode;
        }
    }
	
	//args = #STONES PLAYER# MODE(M=MELEE,T=TWO_PLAYER)
    private static GameParams parseParams(String[] args)
    {
        int numStones = 0;
        int numPlayers = 2;
        int playerNum = 0;
        Mode mode = null;
        try
        {
            numStones = Integer.parseInt(args[0]);
            playerNum = Integer.parseInt(args[1]);
            
            try
            {
                if (args[2].equals("M"))
                {
                    mode = Mode.MELEE;
                    numPlayers = 3;
                }
                else 
                {
                    mode = Mode.TWO_PLAYER;
                }
            }
            catch (Exception e)
            {
                mode = Mode.TWO_PLAYER;
            }
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            usage(args);
            System.exit(-1);
        }

        return new GameParams(numStones, numPlayers, playerNum, mode);
    }
    
    private static void usage(String[] args)
    {
        String argStr = "";
        for(String a : args) argStr += a;
        System.err.println("Invalid/missing arguments. " + argStr);
        System.err.println("\tusage: java -jar voronoi-1.0.0.jar #STONES PLAYER# MODE(M=MELEE,T=TWO_PLAYER)");
    }

}
