package com.john.tsp;

import java.util.ArrayList;

import com.john.tsp.data.Problem;
import com.john.tsp.data.Solution;
import com.john.tsp.data.SolutionSpace;
import com.john.tsp.data.Tasks;

import mpi.MPI;

public class Solver {
	private Solution minimumSolution = new Solution(null, Double.MAX_VALUE);
	private Tasks tasks = new Tasks();
	private Integer numberOfSolvers;
	private Integer id;
	
	public void solve(Integer n, Problem problem, Integer id, Integer numberOfSolvers) {
		
		this.numberOfSolvers = numberOfSolvers;
		this.id = id;
		
		Long start = System.currentTimeMillis();
		
		tasks.add(new SolutionSpace(problem.getProblemSize(), problem.getVertices(), problem.getEdges(), 0));
		tasks.initialize(id, numberOfSolvers, this);
		
		ArrayList<Worker> workers = new ArrayList<Worker>(n);
		for (int i = 0; i < n; i++) {
			Worker worker = new Worker(this);
			worker.start();
			
			workers.add(worker);
		}
		
		for (Worker worker : workers) {
			try {
				worker.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Long end = System.currentTimeMillis();
		MPI.COMM_WORLD.Barrier();
		
        if (id == 0) {
        	for (int i = 1; i < numberOfSolvers ; i++) {
        		Solution solution = Communication.recieve(i);
        		checkForNewMinimum(solution);
        		
        	}
        	System.out.println(minimumSolution.getCost() + " solution");
        } else {
        	Communication.send(minimumSolution, id, 0);
        }
        
        
        if (id == 0) System.out.println(end - start +  " ms");
		
		
	}
	
	public void checkForNewMinimumAndSend(SolutionSpace space) {
		synchronized(minimumSolution) {
			
			if (space.minCost() < minimumSolution.getCost()) {
				System.out.println(minimumSolution.getCost());
				minimumSolution = new Solution(space.getVertices(), space.minCost());
				Communication.sendAll(minimumSolution, id, numberOfSolvers);
				tasks.clear(minimumSolution.getCost());
			}
			
		}
		
	}
	
	public void checkForNewMinimum(SolutionSpace space) {
		synchronized(minimumSolution) {
			
			if (space.minCost() < minimumSolution.getCost()) {
				minimumSolution = new Solution(space.getVertices(), space.minCost());
				tasks.clear(minimumSolution.getCost());
			}
		}
	}
	
	public void checkForNewMinimum(Solution solution) {
		synchronized(minimumSolution) {
			
			if (solution.getCost() < minimumSolution.getCost()) {
				minimumSolution = solution;
			}
			
		}
		
	}
	
	
	
	public Tasks getTasks() {
		return tasks;
	}
	
	public Double getMin() {
		return minimumSolution.getCost();
	}
}
