package com.john.tsp.data;

import java.util.ArrayList;
import java.util.TreeSet;

public class Problem {
	
	private Integer problemSize;
	private ArrayList<Vertex> vertices;
	private TreeSet<Edge> edges;
	private Double[][] weights;
	
	public Problem(Integer problemSize, ArrayList<Vertex> vertices, TreeSet<Edge> edges, Double[][] weights) {
		super();
		this.problemSize = problemSize;
		this.vertices = vertices;
		this.edges = edges;
		this.weights = weights;
	}
	public Integer getProblemSize() {
		return problemSize;
	}
	public void setProblemSize(Integer problemSize) {
		this.problemSize = problemSize;
	}
	public ArrayList<Vertex> getVertices() {
		return vertices;
	}
	public void setVertices(ArrayList<Vertex> vertices) {
		this.vertices = vertices;
	}
	public TreeSet<Edge> getEdges() {
		return edges;
	}
	public void setEdges(TreeSet<Edge> edges) {
		this.edges = edges;
	}
	public Double[][] getWeights() {
		return weights;
	}
	
	
	

}
