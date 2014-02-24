package com.fska.map.voronoi;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Code is based off of the C++ Version by 
 * http://blog.ivank.net/fortunes-algorithm-and-implementation.html#impl_cpp
 * 
 * This set of code is purely intended to be used as an educational learning tool 
 * for transposing C++ code to Java
 * @author fskallos
 *
 */
public class VoronoiGenerator {
	/*
		places		: container of places with which we work
		edges		: container of edges which will be teh result
		width		: width of the diagram
		height		: height of the diagram
		root		: the root of the tree, that represents a beachline sequence
		ly			: current "y" position of the line (see Fortune's algorithm)
	*/
	private List<VPoint> places;
	private List<VEdge> edges;
	private float width, height;
	private VParabola root;
	private float ly;
	
	/*
		deleted		: set  of deleted (false) Events (since we can not delete from PriorityQueue
		points		: list of all new points that were created during the algorithm
		queue		: priority queue with events to process
    */
	
	private Set<VEvent> deleted;
	private List<VPoint> points;
	//TODO This config will probably have to change
	//Original C++:
	//std::priority_queue<VEvent *, std::vector<VEvent *>, VEvent::CompareEvent> queue;
	private PriorityQueue<VEvent> queue;
	
	public VoronoiGenerator(){
		edges = null;
	}
	
	public List<VEdge> getEdges(List<VPoint> v, int width, int height){
		places = v;
		this.width = width;
		this.height = height;
		this.root = null;
		deleted = new HashSet<VEvent>();
		if (edges == null)
			edges = new LinkedList<VEdge>();

		if (points == null)
			points = new LinkedList<VPoint>();
		points.clear();
		edges.clear();

		if(queue == null) queue = new PriorityQueue<VEvent>();
		queue.clear();
		for(VPoint vPoint : places){
			queue.add(new VEvent(vPoint, true));
		}
		
		VEvent e;
		while(!queue.isEmpty()){
			e = queue.poll();
			ly = e.point.y;
			
			// if(deleted.find(e) != deleted.end()) { delete(e); deleted.erase(e); continue;}
			if(deleted.contains(e)){continue;}
			if(e.pe) insertParabola(e.point);
			else removeParabola(e);
			
			//delete(e); // Needed for cleanup in C++
			e = null;
		}
		
		finishEdge(root);
		
		for(VEdge edge : edges){
			if(edge.getNeighbour() != null){
				edge.setStart(edge.getNeighbour().getEnd());
				edge.setNeighbour(null);
			}
		}
		return edges;
	}
	
	private void insertParabola(VPoint p){
		if(root == null){
			root = new VParabola(p);
			return;
		}
		if(root.isLeaf && (root.site.y - p.y < 1f)){
			VPoint fp = root.site;
			root.isLeaf = false;
			root.setLeft(new VParabola(fp));
			root.setRight(new VParabola(p));
			VPoint s = new VPoint((p.x + fp.x) / 2f, height); //The beginning edge of the middle seats
			points.add(s);
			if(p.x > fp.x) root.edge = new VEdge(s, fp, p); //Decide which, left or right
			else root.edge = new VEdge(s, p, fp);
			edges.add(root.edge);
			return;
		}
		
		VParabola par = getParabolaByX(p.x);
		
		if(par.cEvent != null){
			deleted.add(par.cEvent);
			par.cEvent = null;
		}
		
		VPoint start = new VPoint(p.x, getY(par.site, p.x));
		points.add(start);
		
		VEdge el = new VEdge(start, par.site, p);
		VEdge er = new VEdge(start, p, par.site);
		
		el.setNeighbour(er);
		edges.add(el);
		
		par.edge = er;
		par.isLeaf = false;
		
		VParabola p0 = new VParabola(par.site);
		VParabola p1 = new VParabola(p);
		VParabola p2 = new VParabola(par.site);
		
		par.setRight(p2);
		par.setLeft(new VParabola());
		par.Left().edge = el;
		
		par.Left().setLeft(p0);
		par.Left().setRight(p1);
		
		checkCircle(p0);
		checkCircle(p2);
	}
	
	private void removeParabola(VEvent e){
		VParabola p1 = e.arch;
		VParabola xl = VParabola.getLeftParent(p1);
		VParabola xr = VParabola.getRightParent(p1);
		
		VParabola p0 = VParabola.getLeftChild(xl);
		VParabola p2 = VParabola.getRightChild(xr);
		
		if(p0.equals(p2))
			System.err.println("I'm betting something goes very wrong here!");
		
		if(p0.cEvent != null){
			deleted.add(p0.cEvent); p0.cEvent = null;
		}
		if(p2.cEvent != null){
			deleted.add(p2.cEvent); p2.cEvent = null;
		}
		
		VPoint p = new VPoint(e.point.x, getY(p1.site, e.point.x));
		points.add(p);
		
		xl.edge.setEnd(p);
		xr.edge.setEnd(p);
		
		VParabola higher = null;
		VParabola par = p1;
		while(!par.equals(root)){
			par = par.parent;
			if(par.equals(xl)) higher = xl;
			if(par.equals(xr)) higher = xr;
		}
		
		higher.edge = new VEdge(p, p0.site, p2.site);
		edges.add(higher.edge);
		
		VParabola gParent = p1.parent.parent;
		if(p1.parent.Left().equals(p1)){
			if(gParent.Left().equals(p1.parent)) gParent.setLeft(p1.parent.Right());
			if(gParent.Right().equals(p1.parent)) gParent.setRight(p1.parent.Right());
		} else {
			if(gParent.Left().equals(p1.parent)) gParent.setLeft(p1.parent.Left());
			if(gParent.Right().equals(p1.parent)) gParent.setRight(p1.parent.Left());
		}
		
		p1 = null;
		
		checkCircle(p0);
		checkCircle(p2);
	}
	
	private void finishEdge(VParabola n){
		if(n == null) return;
		if(n.isLeaf){n = null; return;}
		float mx;
		if(n.edge.getDirection().x > 0f)
			mx = (width >= (n.edge.getStart().x))? width: n.edge.getStart().x;
		else
			mx = (width < (n.edge.getStart().x))? width: n.edge.getStart().x;	
		
		VPoint end = new VPoint(mx, mx * n.edge.getF() + n.edge.getG());
		n.edge.setEnd(end);
		points.add(end);
		
		finishEdge(n.Left());
		finishEdge(n.Right());
		n = null;
	}
	
	private float getXOfEdge(VParabola par, float y){
		VParabola left = VParabola.getLeftChild(par);
		VParabola right = VParabola.getRightChild(par);
		if(left == null || right == null) return -1;
		VPoint p = left.site;
		VPoint r = right.site;
		
		float dp = 2f * (p.y - y);
		float a1 = 1f / dp;
		float b1 = -2f * p.x / dp;
		float c1 = y + dp / 4f + p.x * p.x / dp;
		
		dp = 2f * (r.y - y);
		float a2 = 1f / dp;
		float b2 = -2f * r.x / dp;
		float c2 = ly + dp / 4f + r.x * r.x / dp;
		
		float a = a1 - a2;
		float b = b1 - b2;
		float c = c1 - c2;
		
		float disc = b * b - 4 * a * c;
		float x1 = (-b + (float)Math.sqrt(disc)) / (2f * a);
		float x2 = (-b - (float)Math.sqrt(disc)) / (2f * a);
		
		float ry;
		if(p.y < r.y) ry = (x1 >= x2)?x1:x2;
		else ry = (x1 < x2)? x1: x2;
		
		return ry;
	}
	private VParabola getParabolaByX(float xx){
		VParabola par = root;
		float x = 0f;
		while(!par.isLeaf){
			x = getXOfEdge(par, ly);
			if(x > xx) par = par.Left();
			else par = par.Right();
		}
		return par;
	}
	private float getY(VPoint p, float x){
		float dp = 2f * (p.y - ly);
		float a1 = 1f / dp;
		float b1 = -2f * p.x / dp;
		float c1 = ly + dp / 4f + p.x * p.x / dp;
		
		return(a1*x*x + b1*x + c1);
	}
	
	private void checkCircle(VParabola b){
		VParabola lp = VParabola.getLeftParent(b);
		VParabola rp = VParabola.getRightParent(b);
		
		VParabola a = null, c = null;
		if(lp != null)
			a = VParabola.getLeftChild(lp);
		if (rp != null)
			c = VParabola.getRightChild(rp);
		
		if((a == null) || (c == null) || (a.site.equals(c.site))) return;
		
		VPoint s = null;
		s = getEdgeIntersection(lp.edge, rp.edge);
		if(s == null) return;
		
		float dx = a.site.x - s.x;
		float dy = a.site.y - s.y;
		
		float d = (float)Math.sqrt((dx * dx) + (dy * dy));
		
		if(s.y - d >= ly) return;
		
		VEvent e = new VEvent(new VPoint(s.x, s.y - d), false);
		points.add(e.point);
		b.cEvent = e;
		e.arch = b;
		queue.add(e);
	}
	private VPoint getEdgeIntersection(VEdge a, VEdge b){
		float x = (b.getG() - a.getG()) / (a.getF() - b.getF());
		float y = a.getF() * x + a.getG();
		
		if((x-a.getStart().x) / a.getDirection().x < 0) return null;
		if((y-a.getStart().y) / a.getDirection().y < 0) return null;
		
		if((x-b.getStart().x) / b.getDirection().x < 0) return null;
		if((y-b.getStart().y) / b.getDirection().y < 0) return null;
		
		VPoint p = new VPoint(x,y);
		points.add(p);
		return p;
	}
}
