package graph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Madhuri
 *
 * The CapNode represents a Twitter user.
 *
 */
public class CapNode implements Node {
	
	private int name;
	
	// the neighbors are a user's followers.
	private ArrayList<Integer> neighbors;
	private ArrayList<Integer> followers;
	private boolean isTrendSetter = false;
	
	public CapNode(int name) {
		this.name = name;
		this.neighbors = new ArrayList<Integer>();
		this.followers = new ArrayList<Integer>();
	}
	public CapNode() {
		this.neighbors = new ArrayList<Integer>();
		this.followers = new ArrayList<Integer>();
	}
	

	public int getName() {
		return this.name;
	}
	
	public void addFollower(int follower) {
		followers.add(follower);
	}
	public List<Integer> getFollowers(){
		return followers;
	}
	
	public void addNeighbor(int neighbor) {
		neighbors.add(neighbor);
	}
	
	public void removeNeighbor(int neighbor) {
		int index = neighbors.indexOf(neighbor);
		if (index == -1) {
			System.out.println("removeNeighbor couldn't find the object");
		}
		neighbors.remove(index);
	}
	
	public List<Integer> getNeighbors() {
		return neighbors;
	}
	
	public Integer getNumNeighbors() {
		return neighbors.size();
	}
	
	public int getID() {
		return name;
	}
	
	public boolean getIsTrendSetter() {
		return isTrendSetter;
	}
	
	public void setIsTrendSetter(boolean bool) {
		
		this.isTrendSetter = bool;
	}
	
}