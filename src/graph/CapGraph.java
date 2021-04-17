/**
 * 
 */
package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.Deque;
import java.util.TreeMap;

/**
 * @author Madhuri Rudrabhatla.
 * 
 *         For the warm up assignment, you must implement your Graph in a class
 *         named CapGraph. Here is the stub file.
 *
 */
public class CapGraph implements Graph {

	private Map<Integer, CapNode> listMap;
	private Set<Integer> vertices;

	public CapGraph() {

		listMap = new HashMap<Integer, CapNode>();
	}

	public CapGraph(int node) {
		listMap = new HashMap<Integer, CapNode>();
		this.addVertex(node);
	}

	/*
	 * returns a vertex with the passed number as its id.
	 */
	public CapNode getVertex(int num) {
		return listMap.get(num);
	}

	/*
	 * Returns the set of all vertex IDs.
	 */
	public Set<Integer> getVertexIDs() {
		return listMap.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {
		listMap.put(num, new CapNode(num));
	}

	/*
	 * Add several vertices to a subgraph.
	 */
	private void addVertices(CapGraph subGraph, Set<Integer> nodes) {

		for (int nodeID : nodes) {
			if (!subGraph.getVertexIDs().contains(nodeID)) {
				subGraph.addVertex(nodeID);
				CapNode origiNode = listMap.get(nodeID);
				CapNode newNode = subGraph.getVertex(nodeID);
				for (int neighbor : origiNode.getNeighbors()) {
					newNode.addNeighbor(neighbor);
				}

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		CapNode fromNode = this.getVertex(from);

		fromNode.addNeighbor(to);

		CapNode toNode = this.getVertex(to);
		toNode.addFollower(from);

	}

	public void removeEdge(int from, int to) {
		CapNode fromNode = listMap.get(from);
		fromNode.removeNeighbor(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {

		CapNode centerNode = this.getVertex(center);

		List<Integer> egoNodes = new ArrayList<Integer>();
		egoNodes.addAll(centerNode.getNeighbors());
		egoNodes.add(center);

		CapGraph egonet = new CapGraph();

		// add nodes
		for (int n : egoNodes) {
			egonet.addVertex(n);
		}
		// add edges
		for (int n : egonet.getVertexIDs()) {
			CapNode egoNode = egonet.getVertex(n);
			CapNode origiNode = this.getVertex(n);

			List<Integer> origiNeighbors = origiNode.getNeighbors();

			for (int neighbor : origiNeighbors) {

				if (egoNodes.contains(neighbor)) {
					egoNode.addNeighbor(neighbor);
				}
			}
		}
		return egonet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {

		List<Integer> sccNodeIDs = new ArrayList<Integer>();

		vertices = getVertexIDs();
		Deque<Integer> vertexStack = new ArrayDeque<Integer>();
		vertexStack.addAll(vertices);
		Deque<Integer> finished;

		finished = dfs(this, vertexStack, sccNodeIDs);

		CapGraph transposedGraph = transpose(this);
		sccNodeIDs.clear();
		List<Graph> graphList = dfsGraphList(transposedGraph, finished, sccNodeIDs);

		return graphList;
	}

	/*
	 * Performs depth first search on a graph. Returns a stack of nodes in the
	 * order that they were finished.
	 */
	public Deque<Integer> dfs(CapGraph graph, Deque<Integer> vertices, List<Integer> sccNodeIDs) {

		Set<Integer> visited = new HashSet<Integer>();
		Deque<Integer> finished = new ArrayDeque<Integer>();

		while (!vertices.isEmpty()) {
			int v = vertices.pop();
			if (!visited.contains(v)) {
				dfsVisit(graph, v, visited, finished, sccNodeIDs);
			}
		}
		return finished;
	}

	/*
	 * Performs depth first search on a graph. Returns a list of strongly
	 * connected components.
	 */
	public List<Graph> dfsGraphList(CapGraph graph, Deque<Integer> vertices, List<Integer> sccNodeIDs) {

		Set<Integer> visited = new HashSet<Integer>();
		Deque<Integer> finished = new ArrayDeque<Integer>();
		CapGraph scc;

		List<Graph> sccList = new ArrayList<Graph>();

		while (!vertices.isEmpty()) {

			int v = vertices.pop();

			if (!visited.contains(v)) {
				sccNodeIDs = dfsVisit(graph, v, visited, finished, sccNodeIDs);

				vertices.removeAll(sccNodeIDs); // remove all node IDs to get it
												// working.
			}

			scc = new CapGraph(v);

			// build the nodes of the new graph
			for (int nodeID : sccNodeIDs) {
				scc.addVertex(nodeID);
				CapNode origiNode = getVertex(nodeID);
				CapNode sccNode = scc.getVertex(nodeID);

				for (int n : origiNode.getNeighbors()) {
					sccNode.addNeighbor(n);
				}

			}
			sccList.add(scc);
			sccNodeIDs.clear();
		}
		return sccList;
	}

	/*
	 * Used by dfs. Recursively visits each neighbor's neighbors adding them to
	 * the visited set. If a node has no unvisited neighbors, it is pushed on
	 * the finished stack.
	 * 
	 */
	public List<Integer> dfsVisit(CapGraph graph, int v, Set<Integer> visited, Deque<Integer> finished,
			List<Integer> sccNodeIDs) {
		visited.add(v);
		CapNode vNode = graph.getVertex(v);

		List<Integer> neighbors;
		neighbors = vNode.getNeighbors();

		for (int n : neighbors) {
			if (!visited.contains(n)) {
				dfsVisit(graph, n, visited, finished, sccNodeIDs);
			}
		}

		finished.push(v);
		sccNodeIDs.add(v);
		return sccNodeIDs; // if this returns visited that would help. but then
							// I'm not getting the grouped IDs.
	}

	/*
	 * Return the transpose of the passed graph.
	 */
	public CapGraph transpose(CapGraph graph) {

		Set<Integer> vertices = graph.getVertexIDs();
		CapGraph transposedGraph = new CapGraph();

		// access each node
		for (int v : vertices) {
			CapNode origiNode = graph.getVertex(v);

			if (!transposedGraph.getVertexIDs().contains(v)) {
				transposedGraph.addVertex(v);
			}

			List<Integer> neighbors = origiNode.getNeighbors();

			for (int neighbor : neighbors) {

				if (!transposedGraph.getVertexIDs().contains(neighbor)) {
					transposedGraph.addVertex(neighbor);
				}
				CapNode transposedNeighbor = transposedGraph.getVertex(neighbor);
				transposedNeighbor.addNeighbor(v);
			}
		}
		return transposedGraph;
	}

	/*
	 * Find all trend setters. Set their isTrendSetter attribute to true.
	 */
	public void identifyTrendSetters() {

		List<CapGraph> SCCList = getSCCList();

		for (CapGraph scc : SCCList) {

			int graphSize = scc.getVertexIDs().size();
			if (graphSize > 2) {

				// assume top 10% in graph, rounded up, are trend setters
				double percentOfTrendSetters = .1;
				double numTrendSetters = calculatePercent(graphSize, percentOfTrendSetters);

				TrendSetterComparator comparator = new TrendSetterComparator();
				PriorityQueue<CapNode> followersQueue = buildNodeQueue(graphSize, scc, comparator);

				CapNode trendyNode = new CapNode();

				for (int i = 0; i < numTrendSetters; i++) {
					trendyNode = followersQueue.remove();
					trendyNode.setIsTrendSetter(true);
				}
			}
		}
	}

	/*
	 * Return a priority queue containing all of the nodes in the provided graph
	 * ordered by the provided comparator.
	 */
	private PriorityQueue<CapNode> buildNodeQueue(int pqSize, CapGraph graph, Comparator comparator) {

		PriorityQueue<CapNode> nodeQueue = new PriorityQueue<CapNode>(pqSize, comparator);
		// add scc nodes to the numFollowerQueue.
		for (int nodeID : graph.getVertexIDs()) {
			CapNode node = this.getVertex(nodeID);
			nodeQueue.add(node);
		}
		return nodeQueue;
	}

	/*
	 * Calculate and return n percent of a number based on the number and a
	 * provided percentage.
	 */
	private double calculatePercent(double number, double percentage) {
		return number * percentage;
	}

	/*
	 * Return a list of SCC's as CapGraphs.
	 */
	private List<CapGraph> getSCCList() {
		List<CapGraph> SCCList = new ArrayList<CapGraph>();
		for (Graph graph : this.getSCCs()) {
			CapGraph capgraph = (CapGraph) graph;
			SCCList.add(capgraph);
		}
		return SCCList;

	}

	/*
	 * Continues the flow of information if preconditions are met.
	 */
	private void shareVideo(int nodeID, Deque<Integer> toVisit, Set<Integer> alreadyViewed) {

		CapNode node = this.getVertex(nodeID);
		if (!alreadyViewed.contains(nodeID)) {
			alreadyViewed.add(nodeID);
		}

		List<Integer> nodeFollowers = node.getFollowers();
		int numNodeFollowers = nodeFollowers.size();

		if (node.getIsTrendSetter()) {
			int followersExposed = 0;

			// first loop for trend setters
			// determines if they should share.
			// I don't want to modify followers
			// unless the video is shared
			// and all followers are modified.
			for (int followerID : nodeFollowers) {

				double percent = .2;
				double maxFollowerViews = calculatePercent(numNodeFollowers, percent);

				if (alreadyViewed.contains(followerID)) {
					followersExposed++;

					// This might make sense as a heuristic,
					// but realistically they're not going to know
					// how many of their followers have seen the video.
					// However it does help to gauge how many people it has
					// reached.

					// I might have considered eliminating it entirely
					// because it's almost identical to toVisit.

					if (followersExposed > maxFollowerViews) {
						return;
					}
				}
			}
		}
		for (int followerID : nodeFollowers) {
			if (!toVisit.contains(followerID)) {
				toVisit.addLast(followerID);
			}
			alreadyViewed.add(followerID);
		}
	}

	/*
	 * 
	 */
	public void startViralSharing(int startingNodeID) {

		Deque<Integer> toVisit = new LinkedList<Integer>();
		Set<Integer> Visited = new HashSet<Integer>();
		Set<Integer> alreadyViewed = new HashSet<Integer>();

		// add starting node
		alreadyViewed.add(startingNodeID);
		CapNode startingNode = this.getVertex(startingNodeID);

		toVisit.add(startingNodeID);
		toVisit.addAll(startingNode.getFollowers());

		int n = 1;
		int currentGraphLength = 0;
		int previousGraphLength = 0;
		CapGraph subGraph = new CapGraph();

		while (!toVisit.isEmpty()) {
			int nodeID = toVisit.pop();
			if (!Visited.contains(nodeID)) {
				shareVideo(nodeID, toVisit, alreadyViewed);
				Visited.add(nodeID);
			}

			addVertices(subGraph, Visited);
			currentGraphLength = subGraph.getVertexIDs().size();

			if (shouldContinueGenerating(n, 10, previousGraphLength, currentGraphLength)) {
				previousGraphLength = currentGraphLength;
				System.out.println("Generation " + n + " " + subGraph.exportGraph());
				n++;
			}
		}
	}

	/*
	 * Returns true if the max generation number hasn't been reached and the
	 * information can flow further.
	 */
	private boolean shouldContinueGenerating(int generationNum, int maxGenerationNum, int previousLength,
			int currentLength) {

		if ((generationNum <= maxGenerationNum) && (previousLength != currentLength)) {
			return true;
		}

		return false;
	}

	/*
	 * Return a subgraph comprised of the starting node and the set of
	 * additional nodes.
	 */
	private CapGraph buildSubGraph(int startingNodeID, Set<Integer> nodes) {

		CapGraph subGraph = new CapGraph(startingNodeID);

		for (int nodeID : nodes) {
			subGraph.addVertex(nodeID);
			CapNode origiNode = listMap.get(nodeID);
			CapNode newNode = subGraph.getVertex(nodeID);
			for (int neighbor : origiNode.getNeighbors()) {
				newNode.addNeighbor(neighbor);
			}
		}
		return subGraph;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {

		HashMap<Integer, HashSet<Integer>> graphRepresentation = new HashMap<Integer, HashSet<Integer>>();
		for (int num : listMap.keySet()) {
			CapNode node = listMap.get(num);
			HashSet<Integer> neighbors = new HashSet<Integer>();
			neighbors.addAll(node.getNeighbors());

			graphRepresentation.put(num, neighbors);
		}
		return graphRepresentation;
	}

	public static void main(String[] args) {

		Graph TwitterGraph = new CapGraph();
		util.GraphLoader.loadGraph(TwitterGraph, "data/twitter_combined.txt");

		Graph TestGraph = new CapGraph();
		util.GraphLoader.loadGraph(TestGraph, "data/small_test_graph.txt");

		CapGraph CapGraphWithNodes = new CapGraph();

		for (int i = 1; i < 6; i++) {
			CapGraphWithNodes.addVertex(i);
		}
		CapGraphWithNodes.addEdge(1, 2);
		CapGraphWithNodes.addEdge(2, 1);
		CapGraphWithNodes.addEdge(1, 5);
		CapGraphWithNodes.addEdge(3, 4);
		CapGraphWithNodes.addEdge(5, 2);
		// System.out.println("Checking graph...");
		System.out.println(CapGraphWithNodes.exportGraph());
		//
		// Graph egoNet = CapGraphWithNodes.getEgonet(1);
		//
		// System.out.println("Checking egonet...");
		// System.out.println(egoNet.exportGraph());
		//
		// System.out.println("Checking transpose...");
		//
		// Graph transposedGraph =
		// CapGraphWithNodes.transpose(CapGraphWithNodes);
		//
		// System.out.println(transposedGraph.exportGraph());
		//
		// System.out.println("Checking getSCCs...");
		//
		// CapGraphWithNodes.identifyTrendSetters();
		// System.out.println(CapGraphWithNodes.exportGraph());
		//
		// for (int nodeName : CapGraphWithNodes.getVertexIDs()) {
		// CapNode node = CapGraphWithNodes.getVertex(nodeName);
		// if (node.getIsTrendSetter()) {
		// System.out.println(nodeName + " is a trend setter");
		// System.out.println(node.getFollowers().toString());
		// }
		// }

		CapGraphWithNodes.startViralSharing(2);

		System.out.println("Checking twitter SCCs");
		int i = 0;
		for (Graph graph : TwitterGraph.getSCCs()) {
			if (i == 0) {
				System.out.println("a twitter scc: " + graph.exportGraph());
			}
			i++;

		}

	}

}
