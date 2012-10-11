package concreteWorld;

import gameworld.Actor;
import gameworld.Reporter;
import gameworld.World;

public class Main {

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		
		Actor player1 = new RandomPlayer("Timmy-1");
		Actor player2 = new RandomPlayer("Ricky-2");
		
		World.initWorld( player1, player2 );
		
		World world = World.getInstance();
		
		Reporter reporter = new StdOutReporter(world);
		
//		while( world.reportLastUpdate().isTerminal() )
		for( int i = 0; i < 10; i++ )
		{
			System.out.println( "turn: " + (i+1) );
			
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
