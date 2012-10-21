package concreteWorld;

import gameworld.Actor;
import gameworld.Reporter;
import gameworld.World;

public class Main {

	public static void main(String[] args) {
		
	    System.out.println("rky");
	    
		Actor player1 = new RandomPlayer("Timmy-1");
		Actor player2 = new RandomPlayer("Ricky-2");
		World.initWorld( player1, player2 );
		World world = World.getInstance();
		
		Reporter reporter = new StdOutReporter(world);
		
//		while( world.reportLastUpdate().isTerminal() )
		for( int i = 0; i < 10; i++ )
		{
			world.continueGame();
			reporter.reportUpdateToViewer();
			reporter.reportStateToViewer();
			
			if( world.reportLastUpdate().isTerminal() ) {
				System.out.println("End game");
				break;
			}
		}
		
		System.out.println("done");
	}

}
