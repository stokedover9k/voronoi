package concreteWorld;

import gameworld.Moderator;
import gameworld.Reporter;
import gameworld.WorldState;
import gameworld.WorldUpdate;

public class StdOutReporter implements Reporter {
	
	final Moderator moderator;
	
	public StdOutReporter(Moderator moderatorToReport) {
		this.moderator = moderatorToReport;
	}

	@Override
	public WorldUpdate reportUpdateToViewer() {
		WorldUpdate lastUpdate = this.moderator.reportLastUpdate();
		System.out.println("Reporter " + this + ": " + lastUpdate);
		return lastUpdate;
	}

	@Override
	public WorldState reportStateToViewer() {
		WorldState worldState = this.moderator.reportWorldState();
		System.out.println("Reporter " + this + ": " + worldState);
		return worldState;
	}

}
