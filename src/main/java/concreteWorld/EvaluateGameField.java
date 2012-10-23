 package concreteWorld;

import gameworld.Actor;

import java.util.List;

import parameters.Parameter;
import parameters.ParameterFactory;

public class EvaluateGameField {

	public static OwnedPolygon getBestPolygonForPlacingStone(VoronoiGameField gamefield,List<Actor> team){
		
		OwnedPolygon bestPolygon = null;
		
		//comupute score for all polygons
		for(OwnedPolygon polygon : gamefield.getAllPolygons()){
			polygon.setWeight(0); // clear state before every move
		}
		
		//compute the paramters for all polygons and store in weight
		List<Parameter> params = ParameterFactory.getParameters(gamefield,team);
		for(Parameter param : params){
			param.evaluate(); // the result is stored in weights for all polygons
		}
		
		
		//scan the polygons and return the polygon with maximum weight
		for(OwnedPolygon polygon : gamefield.getAllPolygons()){
			if(bestPolygon == null){
				bestPolygon = polygon;
			}else{
				//compare weights of the polygon
				if(polygon.weight > bestPolygon.weight){
					bestPolygon = polygon;
				}
			}
		}
		
		return bestPolygon;
		
	}
	
}
