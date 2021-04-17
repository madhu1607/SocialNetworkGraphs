package graph;

import java.util.List;

/**
 * @author Madhuri Rudrabhatla
 * 
 * A Node interface to be implemented
 * with the Graph interface.
 *
 */
public interface Node {
	
	public void addNeighbor(int neighbor);
	
	public List<Integer> getNeighbors();
	
	public int getID();
}