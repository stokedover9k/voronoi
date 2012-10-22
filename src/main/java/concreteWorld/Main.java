package concreteWorld;

import gameworld.Actor;
import gameworld.Actor.Team;
import gameworld.Reporter;
import gameworld.World;
import concreteWorld.Main.GameParams.Mode;

public class Main {

    public static GameParams GAME_PARAMS;
    
    //args = #STONES PLAYER# #PLAYERS MODE(M=MELEE,T=TWO_PLAYER)
	public static void main(String[] args) 
	{
	    GAME_PARAMS = parseParams(args);
	    World world = World.getInstance();
	    
        Actor us = new RandomPlayer (
                "rky", 
                GAME_PARAMS.playerNumber == 1 ? 1 : 2, 
                GAME_PARAMS.playerNumber == 1 ? Team.RED : Team.BLUE
        );
        
        Actor them = new RandomPlayer (
                "opponent", 
                GAME_PARAMS.playerNumber == 1 ? 1 : 2, 
                GAME_PARAMS.playerNumber == 1 ? Team.RED : Team.BLUE
        );
        
        world.addPlayer(us);
        world.addPlayer(them);
        
		Reporter stdErrReporter = new StdOutReporter(world);
		Reporter serverReporter = new ServerReporter(world, us);
		
		for(int i = 0 ; i < GAME_PARAMS.numStones*2 ; ++i)
		{
			world.continueGame();
			stdErrReporter.reportUpdateToViewer();
			stdErrReporter.reportStateToViewer();
			serverReporter.reportUpdateToViewer();
			
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
	
	//args = #STONES PLAYER# #PLAYERS MODE(M=MELEE,T=TWO_PLAYER)
    private static GameParams parseParams(String[] args)
    {
        int numStones = 0;
        int numPlayers = 0;
        int playerNum = 0;
        Mode mode = null;
        try
        {
            numStones = Integer.parseInt(args[0]);
            playerNum = Integer.parseInt(args[1]);
            
            try
            {
                numPlayers = Integer.parseInt(args[2]);
            }
            catch (Exception e)
            {
                numPlayers = 2;
            }
            
            try
            {
                if (args[3].equals("M"))
                {
                    mode = Mode.MELEE;
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
        System.err.println("\tusage: java -jar voronoi-1.0.0.jar #STONES PLAYER# #PLAYERS MODE(M=MELEE,T=TWO_PLAYER)");
    }

}
