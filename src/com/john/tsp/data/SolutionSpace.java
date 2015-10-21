package com.john.tsp.data;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class SolutionSpace implements Comparable<SolutionSpace> {
	private Integer problemSize;
	private ArrayList<Vertex> vertices;
	private TreeSet<Edge> edges;
	private Integer assigned;
	private Double minimumCost;
	
	public SolutionSpace(Integer problemSize, ArrayList<Vertex> vertices, TreeSet<Edge> edges, Integer assigned) {
		super();
		this.problemSize = problemSize;
		this.vertices = vertices;
		this.edges = edges;
		this.assigned = assigned;
	}
	
	public SolutionSpace copy() {
		ArrayList<Vertex> newVertices = new ArrayList<Vertex>();
		
		for (Vertex vertex : vertices) {
			newVertices.add(vertex.copy());
		}
		
		TreeSet<Edge> newEdges = new TreeSet<Edge>(edges);
		
		return new SolutionSpace(problemSize, newVertices, newEdges, assigned);
	}
	
	public Double minCost() {
		if (minimumCost != null) return minimumCost;
		Double sum = 0.0;
		
		for (Vertex v : vertices) {
			sum += v.currentCost();
		}
		
		sum /= 2.0;
		minimumCost = sum;
		return sum;
	}
	
	public SolutionSpace leftBranch() {
		Edge edge = edges.first();
		edges.remove(edge);
		edge.assign(vertices);
		assigned += 1;
		
		return this.infer();
	}
	
	public SolutionSpace rightBranch() {
		Edge edge = edges.first();
		edges.remove(edge);
		edge.reject(vertices);
		return this.infer();
	}
	
	private SolutionSpace infer() {
		Boolean changed = true;
		
		while(changed) {
			changed = removeCycles();
			changed = changed || removeFull();
			changed = changed || inferAdd();
		}
		
		for (Vertex vertex : vertices) {
			if (vertex.getAssigned().size() + vertex.getAvailable().size() < 2) {
				return null;
			}
		}
		
		return this;
	}
	
	private Boolean removeCycles() {
		Boolean changed = false;
		
		ArrayList<Edge> edgesCopy = new ArrayList<Edge>(edges);
		for (Edge edge : edgesCopy) {
			Vertex root1 = vertices.get(edge.getIndex_i()).getRoot(vertices);
			Vertex root2 = vertices.get(edge.getIndex_j()).getRoot(vertices);
			if ((root1 == root2) && (root1.getSize() < problemSize)) {
				this.edges.remove(edge);
				edge.reject(vertices);
				changed = true;
			}
		}
		return changed;
	}
	
	private Boolean removeFull() {
		Boolean changed = false;
		for (Vertex vertex : vertices) {
			if ((vertex.getAssigned().size() == 2) && !vertex.getAvailable().isEmpty()) {
				changed = true;
				ArrayList<Edge> edgesCopy = new ArrayList<Edge>(vertex.getAvailable());
				for (Edge edge : edgesCopy) {
					this.edges.remove(edge);
					edge.reject(vertices);
				}
			}
		}
		return changed;
	}
	
	private Boolean inferAdd() {
		ArrayList<Edge> edgesCopy = new ArrayList<Edge>(edges);
		
		for(Edge edge : edgesCopy) {
			Vertex v1 = vertices.get(edge.getIndex_i());
			Vertex v2 = vertices.get(edge.getIndex_j());
			
			if ((v1.getAssigned().size() + v1.getAvailable().size() == 2) || ((v2.getAssigned().size() + v2.getAvailable().size() == 2))) {
				this.edges.remove(edge);
				edge.assign(vertices);
				assigned += 1;
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<Vertex> getVertices() {
		return vertices;
	}
	
	public Boolean isSolution() {
		return assigned == problemSize;
	}

	@Override
	public int compareTo(SolutionSpace o) {
		
		int comp = -(this.assigned.compareTo(o.assigned));
		if (comp != 0) return comp;
		
		comp = this.minCost().compareTo(o.minCost());
		if (comp != 0) return comp;
		return Integer.compare(this.hashCode(), o.hashCode());
	}
}
