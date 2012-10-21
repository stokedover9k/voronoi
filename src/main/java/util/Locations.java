package util;

import util.Vecs.Vec;


public class Locations {
	
	public static class Loc {
		final protected float[] values;
		
		public Loc( int degree ) {
			values = new float[degree]; 
		}
		
		public Loc( Loc otherLoc ) {
			values = otherLoc.values.clone();
		}

		public int compareTo(Loc loc) {
			int degree = Math.min(values.length, loc.values.length);
			for( int i = 0; i < degree; i++ ) {
				if( values[i] < loc.values[i] )  return -1;
				if( values[i] > loc.values[i] )  return  1;
			}
			return 0;
		}

		public float get(int dimensionIndex) {
			return values[dimensionIndex];
		}
		
		public void set(int dimensionIndex, float value) {
			values[dimensionIndex] = value;
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for( float f : values )
				sb.append(f + ", ");
			if( sb.length() > 0 )
				sb.setLength(sb.length()-2);
			return sb.toString();
		}
		
		public int hashCode() {
			return 29 * toString().hashCode();
		}
		
		public int getDegree() {
			return values.length;
		}

		public Loc midpointTo(Loc location) {
			Loc midpoint = new Loc(values.length);
			for( int i = 0; i < values.length; i++ )
				midpoint.values[i] = (this.values[i] + location.values[i]) / 2;
			return midpoint;
		}
	}

	
	
	public static class Loc2d extends Loc {

		public Loc2d(float x, float y) {
			super(2);
			setX(x);
			setY(y);
		}
		
		public float getX() { return get(0); }
		public float getY() { return get(1); }

		public void setX(float x) { set(0, x); }
		public void setY(float y) { set(1, y); }
		
	}
}
