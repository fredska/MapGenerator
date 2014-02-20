package com.fska.map.voronoi.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.fska.map.voronoi.VEdge;
import com.fska.map.voronoi.VPoint;
import com.fska.map.voronoi.VoronoiGenerator;

public class VoronoiGeneratorTest {

	@Test
	public void test() {
		VoronoiGenerator vg = new VoronoiGenerator();
		
		List<VPoint> points = new LinkedList<VPoint>();
		points.add(new VPoint(10, 10));
		points.add(new VPoint(30, 20));
		points.add(new VPoint(20, 30));
		points.add(new VPoint(80, 40));
		points.add(new VPoint(70, 50));
		points.add(new VPoint(60, 60));
		List<VEdge> edges = vg.getEdges(points, 100, 100);
		assertTrue(true);
	}

}
