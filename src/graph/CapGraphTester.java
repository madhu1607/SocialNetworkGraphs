package graph;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class CapGraphTester {
	
	CapGraph emptyCapGraph;
	CapGraph CapGraphWithNodes;
	Set<Integer> emptySet;
	Set<Integer> populatedSet;
	
	@Before
	public void setUp() throws Exception {
		// build the CapGraphs to test here.
		emptyCapGraph = new CapGraph();
		CapGraphWithNodes = new CapGraph();
		
		for (int i=1; i<6; i++) {
			CapGraphWithNodes.addVertex(i);
		}
		
		CapGraphWithNodes.addEdge(1, 2);
		CapGraphWithNodes.addEdge(2, 1);
		CapGraphWithNodes.addEdge(1, 5);
		CapGraphWithNodes.addEdge(3, 4);
		
		emptySet = new HashSet<Integer>();
		populatedSet = new HashSet<Integer>();
		
		for (int i = 1; i < 6; i++) {
			populatedSet.add(i);
		}
	}
	
	@Test
	public void testGetVertices() {
		// test empty graph vertices
		assertEquals(emptySet, emptyCapGraph.getVertexIDs());
		
		// test filled graph vertices
		assertEquals(populatedSet, CapGraphWithNodes.getVertexIDs());
	}
	
	@Test 
	public void testGetVertex() {
		
		for (int i = 1; i < 6; i++)	{
			CapNode node = CapGraphWithNodes.getVertex(i);
			int nodeID = node.getID();
			assertEquals(nodeID, i);	
		}
	}	
	
	public void TestAddEdge() {
		
		// tests against the edges added in setUp()
		
		List<Integer> testList = new ArrayList<Integer>();
		List<Integer> neighbors = new ArrayList<Integer>();
		
		for (int i = 1; i < 6; i++) {
			CapNode node = CapGraphWithNodes.getVertex(i);
			
			// test multiple edges.
			if (node.getID() == 1) {
				testList = Arrays.asList(2, 5);
			}
			
			// test two edges between nodes.
			if (node.getID() == 2) {
				testList = Arrays.asList(1);
			}
			
			if (node.getID() == 5) {
				
			}		
			neighbors = node.getNeighbors();
			
			assertEquals(testList.size(), neighbors.size());
			assertEquals(testList, neighbors);

		}
		
	}
	
	@Test
	public void testGetEgonet() {
		
		CapGraph egoNet1 = (CapGraph) CapGraphWithNodes.getEgonet(1);
		Set<Integer> egoNet1IDs = new HashSet<Integer>();
		egoNet1IDs.add(1);
		egoNet1IDs.add(2);
		egoNet1IDs.add(5);
		
		// check membership
		assertEquals(egoNet1IDs, egoNet1.getVertexIDs());
				
		
// should add another test to ensure the presence of the correct edges.
		
	}
}