package com.john.tsp;

import com.john.tsp.data.Solution;
import com.john.tsp.data.SolutionSpace;

public class Worker extends Thread {
	Solver solver;

	public Worker(Solver solver) {
		super();
		this.solver = solver;
	}
	
	@Override
	public void run() {
		SolutionSpace space = solver.getTasks().take(this);
		
		while(space != null) {
			Solution solution = Communication.iRecieve();
			if (solution != null) solver.checkForNewMinimum(solution);

			add(space.copy().leftBranch());
			add(space.copy().rightBranch());
			solver.getTasks().removeWorker(this);
			space = solver.getTasks().take(this);
		}
	}
	
	private void add(SolutionSpace space) {
		if (space == null) return;
		else if (space.minCost() >= solver.getMin()) {
			return;
		}
		else if (space.isSolution()) {
			solver.checkForNewMinimumAndSend(space);
		}
		else {
			solver.getTasks().add(space);
		}
	}
}
