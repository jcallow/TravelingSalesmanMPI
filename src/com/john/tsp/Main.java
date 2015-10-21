package com.john.tsp;

import java.io.FileNotFoundException;

import com.john.tsp.data.Problem;

import mpi.MPI;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException {
		
		MPI.Init(args);
		
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		
		if (args.length == 2) {

		} else {
			String file = "huge.txt";
			Problem problem = MatrixReader.read(file);
			Solver solver = new Solver();
			solver.solve(3,  problem, rank, size);
		}
		
		MPI.Finalize();
		
	}

}
