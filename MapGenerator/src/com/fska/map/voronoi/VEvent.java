package com.fska.map.voronoi;

public class VEvent implements Comparable<VEvent>{
	public VPoint point;
	public boolean pe;
	public float y;
	public VParabola arch;
	
	public VEvent(VPoint point, boolean pev){
		this.point = point;
		this.pe = pev;
		this.y = point.y;
		arch = null;
	}

	@Override
	public int compareTo(VEvent o) {
		if(this.y == o.y) return 0;
		if(this.y < o.y) return -1;
		return 1;
	}
}
