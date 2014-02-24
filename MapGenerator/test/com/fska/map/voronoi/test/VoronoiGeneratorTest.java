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
		int width = 100, height = 100;
		List<VPoint> points = new LinkedList<VPoint>();
		//Initialize points here
		points.add(new VPoint(width * 0.1f, height * 0.11f));
		points.add(new VPoint(width * 0.1f, height * 0.9f));
		points.add(new VPoint(width / 2f, height / 2f));
		points.add(new VPoint(width * 0.91f, height * 0.15f));
		points.add(new VPoint(width * 0.915f, height * 0.915f));
		List<VEdge> edges = vg.getEdges(points, width, height);
		assertTrue(true);
	}

}
