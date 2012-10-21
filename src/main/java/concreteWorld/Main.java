package concreteWorld;

import gameworld.Actor;
import gameworld.Reporter;
import gameworld.World;

public class Main {

	public static void main(String[] args) {
	    
        int numStones = getNumStones(args);
	    
	    System.out.println("rky");
	    
		Actor player1 = new RandomPlayer("Timmy-1");
		Actor player2 = new RandomPlayer("Ricky-2");
		World.initWorld( player1, player2 );
		World world = World.getInstance();
		
		Reporter stdErrReporter = new StdOutReporter(world);
		Reporter serverReporter = new ServerReporter(world, player1);
		
		for(int i = 0 ; i < numStones*2 ; ++i)
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

    private static int getNumStones(String[] args)
    {
        int numStones = 0;
        try
        {
            if (args.length != 1)
            {
                throw new Exception();
            }
            numStones = Integer.parseInt(args[0]);
        }
        catch (Exception e)
        {
            System.err.println("Must provide number of stones.");
            System.exit(-1);
        }
        return numStones;
    }

}
