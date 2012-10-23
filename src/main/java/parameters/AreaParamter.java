package parameters;

import gameworld.Actor;

import java.util.List;

import concreteWorld.OwnedPolygon;
import concreteWorld.Polygons;
import concreteWorld.Polygons.PolygonEvaluator;
import concreteWorld.VoronoiGameField;


public class AreaParamter extends Parameter {

	VoronoiGameField gameField;

	public AreaParamter(VoronoiGameField gameField) {

		this.gameField = gameField;
	}

	@Override
	public void evaluate() {
		for(OwnedPolygon polygon : gameField.getAllPolygons()){

//			if(polygonBelongsToOpponent(polygon)){

				//compute Area and assign weight
				double cummulative_weight = polygon.getWeight();
				cummulative_weight += Polygons.getArea(polygon);
				polygon.setWeight(cummulative_weight);
//			}else{//we would never place stone in our own polygon
//				polygon.setWeight(0);
//			}
		}
	}

//	private boolean polygonBelongsToOpponent(OwnedPolygon polygon) {
//		
//		boolean polygonBelongsToOpponent = true;
//		
//		for(Actor team_member : team){
//			if(team_member == polygon.getOwner()){
//				polygonBelongsToOpponent = false;
//			}
//		}
//		
//		return polygonBelongsToOpponent;
//	}

}
