package com.john.tsp.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Vertex implements Serializable {
	private Integer index;
	private Integer head;
	private Integer size;
	private Set<Edge> assigned;
	private TreeSet<Edge> available;
	
	public Vertex(Integer index, Integer head, Integer size, Set<Edge> assigned, TreeSet<Edge> available) {
		super();
		this.index = index;
		this.head = head;
		this.size = size;
		this.assigned = assigned;
		this.available = available;
	}
	
	public Vertex copy() {
		Set<Edge> newAssigned = new HashSet<Edge>(assigned);
		TreeSet<Edge> newAvailable = new TreeSet<Edge>(available);
		return new Vertex(index, head, size, newAssigned, newAvailable);
	}
	
	public Vertex getRoot(ArrayList<Vertex> vertices) {
		if (index == head) return this;
		return vertices.get(head).getRoot(vertices);
	}
	
	public void addEdge(Edge edge) {
			available.add(edge);
	}
	
	public void assignEdge(Edge edge) {
		assigned.add(edge);
		available.remove(edge);
	}
	
	public void rejectEdge(Edge edge) {
		available.remove(edge);
	}
	
	public Double currentCost() {
		double cost = 0;
		
		if (assigned.size() == 2) {
			for (Edge edge : assigned) {
				cost += edge.getCost();
			}
		} else if (assigned.size() == 1) {
			for (Edge edge : assigned) {
				cost += edge.getCost();
			}
			cost += available.first().getCost();
		} else {
			int count = 0;
			for (Edge edge : available) {
				if (count < 2) {
					cost += edge.getCost();
					count++;
				} else {
					break;
				}
			}
		}
		return cost;
	}
	
	public Integer getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void setHead(Integer head) {
		this.head = head;
	}

	public Set<Edge> getAssigned() {
		return assigned;
	}

	public TreeSet<Edge> getAvailable() {
		return available;
	}
	
	
}
