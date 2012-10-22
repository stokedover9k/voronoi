package util;



public class Locations {
	
	public static class Loc {
		final protected double[] values;
		
		public Loc( int degree ) {
			values = new double[degree]; 
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
		
		public boolean equals(Object o) {
			Loc loc = (Loc)o;
			int degree = Math.min(values.length, loc.values.length);
			for( int i = 0; i < degree; i++ ) {
				if( values[i] != loc.values[i] )  return false;
			}
			return true;
		}

		public double get(int dimensionIndex) {
			return values[dimensionIndex];
		}
		
		public void set(int dimensionIndex, double value) {
			values[dimensionIndex] = value;
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for( double f : values )
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

		public Loc2d(double x, double y) {
			super(2);
			setX(x);
			setY(y);
		}
		
		public double getX() { return get(0); }
		public double getY() { return get(1); }

		public void setX(double x) { set(0, x); }
		public void setY(double y) { set(1, y); }
		
	}
}
