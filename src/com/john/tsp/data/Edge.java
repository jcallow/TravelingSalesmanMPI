package com.john.tsp.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Edge implements Comparable<Edge>, Serializable {

	private Double cost;
	private Integer index_i;
	private Integer index_j;
	
	public Edge(Double cost, Integer index_i, Integer index_j) {
		super();
		this.cost = cost;
		this.index_i = index_i;
		this.index_j = index_j;
	}
	
	public void reject(ArrayList<Vertex> vertices) {
		vertices.get(index_i).rejectEdge(this);
		vertices.get(index_j).rejectEdge(this);
	}
	
	public void add(ArrayList<Vertex> vertices) {
		vertices.get(index_i).addEdge(this);
		vertices.get(index_j).addEdge(this);
	}
	
	public void assign(ArrayList<Vertex> vertices) {
		Vertex v1 = vertices.get(index_i);
		Vertex v2 = vertices.get(index_j);
		v1.assignEdge(this);
		v2.assignEdge(this);
		
		Vertex root1 = v1.getRoot(vertices);
		Vertex root2 = v2.getRoot(vertices);
		
		if (root1.getSize() < root2.getSize()) {
			root1.setHead(vertices.indexOf(root2));
			root2.setSize(root2.getSize() + root1.getSize());
		} else {
			root2.setHead(vertices.indexOf(root1));
			root1.setSize(root1.getSize() + root2.getSize());
		}
	}
	
	public Double getCost() {
		return cost;
	}

	public Integer getIndex_i() {
		return index_i;
	}

	public Integer getIndex_j() {
		return index_j;
	}

	@Override
	public int compareTo(Edge o) {
		Integer comp = (this.getCost().compareTo(o.getCost()));
		if (comp == 0) {
			comp = (index_i.compareTo(o.index_i));
		}
		
		if (comp == 0) {
			comp = (index_j.compareTo(o.index_j));
		}
		return comp;
	}


	
	
	
}
