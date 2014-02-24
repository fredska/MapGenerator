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
	public boolean equals(Object obj) {
		if(!(obj instanceof VEvent)) return false;
		
		VEvent vEvent = (VEvent)obj;
		return this.point.equals(vEvent.point);
	}
	
	@Override
	public String toString(){
		String eventType = pe?"Site Event" : "Circle Event";
		return this.y + " :: " + eventType + " :: " + this.point;
	}

	@Override
	public int compareTo(VEvent o) {
		if(this.y == o.y) return 0;
		if(this.y < o.y) return -1;
		return 1;
	}
}
