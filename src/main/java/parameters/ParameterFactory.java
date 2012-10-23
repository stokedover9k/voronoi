package parameters;

import gameworld.Actor;

import java.util.ArrayList;
import java.util.List;

import concreteWorld.VoronoiGameField;


public class ParameterFactory {
	
	public static List<Parameter> getParameters(VoronoiGameField gameField,List<Actor> team){
		
		
		
		List<Parameter> returnValue = new ArrayList<Parameter>();
		
		//add pamaeter here
		returnValue.add(new AreaParamter(gameField,team));
	
		return returnValue;
	}

}
