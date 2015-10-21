package com.john.tsp.data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import com.john.tsp.Solver;
import com.john.tsp.Worker;

public class Tasks {
	private Set<Worker> currentWorkers;
	private ArrayDeque<SolutionSpace> initialWork;
	private PriorityQueue<SolutionSpace> work;
	
	private Object workerLock = new Object();
    private Object taskLock = new Object();
    
    public Tasks() {
    	this.currentWorkers = new HashSet<Worker>();
    	this.work = new PriorityQueue<SolutionSpace>();
    	this.initialWork = new ArrayDeque<SolutionSpace>();
    }
	
	public void add(SolutionSpace space) {
		synchronized(taskLock) {
			work.add(space);
			taskLock.notifyAll();
			
		}
	}
	
	public void removeWorker(Worker worker) {
		synchronized(taskLock) {
			currentWorkers.remove(worker);
			taskLock.notifyAll();
		}
	}
	
	public SolutionSpace take(Worker worker) {
		synchronized(workerLock) {
			synchronized(taskLock) {
				while(work.size() == 0 && currentWorkers.size() > 0) {
					try {
						taskLock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (work.size() == 0 && currentWorkers.size() == 0) return null;
				
				SolutionSpace space = work.poll();
				currentWorkers.add(worker);
				return space;
			}
		}
	}
	
	public void initialize(int id, int numberOfSolvers, Solver solver) {
		if (numberOfSolvers == 1) {
			return;
		}
		
		SolutionSpace space = work.poll();
		
		while(space != null && initialWork.size() < numberOfSolvers) {
			add(space.copy().leftBranch(), solver);
			add(space.copy().rightBranch(), solver);

			space = initialWork.poll();
		}
		
		if (initialWork.size() > 0) {
			
			for (int i = 0; i < id; i++) {
				initialWork.poll();
			}
			
			SolutionSpace startingSpace = initialWork.poll();
			System.out.println(startingSpace.minCost());
			initialWork.clear();
			work.add(startingSpace);
		}
	}
	
	private void add(SolutionSpace space, Solver solver) {
		if (space == null) return;
		else if (space.minCost() >= solver.getMin()) return;
		else if (space.isSolution()) {
			solver.checkForNewMinimumAndSend(space);
		}
		else {
			add(space);
		}
	}
	
	public void clear(double value) {
		synchronized(taskLock) {
			work.removeIf(p -> {
				return p.minCost() > value;
			});
		}
	}
}
