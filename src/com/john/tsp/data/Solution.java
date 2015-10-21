package com.john.tsp.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Solution implements Serializable {
	private ArrayList<Vertex> vertices;
	private Double cost;
	public Solution(ArrayList<Vertex> vertices, Double cost) {
		super();
		this.vertices = vertices;
		this.cost = cost;
	}
	public ArrayList<Vertex> getVertices() {
		return vertices;
	}
	public Double getCost() {
		return cost;
	}
	
	

}
