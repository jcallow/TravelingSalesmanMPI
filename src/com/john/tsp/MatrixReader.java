package com.john.tsp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

import com.john.tsp.data.Edge;
import com.john.tsp.data.Problem;
import com.john.tsp.data.Vertex;

public class MatrixReader {

	
	public static Problem read(String file) throws FileNotFoundException { 
			Double[][] weights = readFile(file);
			
			ArrayList<Vertex> vertices = new ArrayList<Vertex>();
			
			for (int i = 0; i < weights.length; i++) {
				vertices.add(new Vertex(i,i,1,new HashSet<Edge>(), new TreeSet<Edge>()));
			}
			
			TreeSet<Edge> edges = new TreeSet<Edge>();
			
			for (int i = 0; i < weights.length; i++) {
				for (int j = i+1; j < weights.length; j++) {
					Edge edge = new Edge(weights[i][j], i , j);
					edges.add(edge);
					vertices.get(i).addEdge(edge);
					vertices.get(j).addEdge(edge);
				}
			}
			
			return new Problem(weights.length, vertices, edges, weights);
	}
	
	private static Double[][] readFile(String file) throws FileNotFoundException {
		Scanner s = new Scanner(new File(file));
		ArrayList<String> lines = new ArrayList<String>();
		while (s.hasNextLine()){
		    lines.add(s.nextLine());
		}
		s.close();
		
		Double[][] weights = new Double[lines.size()][lines.size()];
		
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			String[] splitLine = line.split(" ");
			for (int j = 0; j < lines.size(); j++) {
				weights[i][j] = Double.parseDouble(splitLine[j]);
			}
		}
		
		return weights;
	}
}
