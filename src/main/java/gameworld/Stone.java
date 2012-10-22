package gameworld;

import util.Locations.Loc2d;
import util.Vecs.Vec;

/*
 * Stone is the "point-sized stone" as described by the Voronoi game description.
 * These are what we use to make each move.
 */
public class Stone implements Comparable<Stone>
{

    public Stone(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public final int x;
    public final int y;

    public int compareTo(Stone s)
    {
        return x == s.x ? y - s.y : x - s.x;
    }

    public boolean equals(Object o)
    {
        return compareTo((Stone) o) == 0;
    }

    public String toString()
    {
        return "<" + x + "," + y + ">";
    }

	public Vec toVec() {
		return new Vec( new Loc2d((double)x, (double)y) );
	}
}
