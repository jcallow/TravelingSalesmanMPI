package com.john.tsp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import com.john.tsp.data.Solution;
import com.john.tsp.data.SolutionSpace;

import mpi.MPI;
import mpi.Request;
import mpi.Status;

public class Communication {
	private static Request rstatus, srequest;
	private static Object lock = new Object();
	
	public static void sendAll(Solution minimumSolution, int rank, int numberOfSolvers) {
		try
        {
			if (srequest != null) srequest.wait();
			ByteBuffer byteBuff = ByteBuffer.allocateDirect(2000 + MPI.SEND_OVERHEAD);
	        MPI.Buffer_attach(byteBuff);
            double[] send = new double[1];
            send[0] = minimumSolution.getCost();
            for (int i = 0; i < numberOfSolvers; i++) {
            	if (i != rank) MPI.COMM_WORLD.Isend(send, 0, 1, MPI.DOUBLE, i, 0);
            	
            }
            MPI.Buffer_detach();
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
	}
	
	private static double[] recBuff = {0.0};
	
	public static Solution iRecieve() {
		synchronized(lock) {
	        if (rstatus == null) {
	        	rstatus = MPI.COMM_WORLD.Irecv(recBuff, 0, 1, MPI.DOUBLE, MPI.ANY_SOURCE, 0);
	        }
	        
	        Status status = rstatus.Test();
	        if (status != null ) {
	        	rstatus = null;
	        	return new Solution(null, recBuff[0]);
	        } 
	        return null;
		}
	}
	
	public static Solution recieve(int sender) {
        MPI.COMM_WORLD.Recv(recBuff, 0, 1, MPI.DOUBLE, MPI.ANY_SOURCE, sender);
    	return new Solution(null, recBuff[0]);

	}
	
	public static void send(Solution minimumSolution, int myRank, int sendTo) {
		try
        {
			
			ByteBuffer byteBuff = ByteBuffer.allocateDirect(2000 + MPI.SEND_OVERHEAD);
	        MPI.Buffer_attach(byteBuff);
            double[] send = new double[1];
            send[0] = minimumSolution.getCost();
            MPI.COMM_WORLD.Send(send, 0, 1, MPI.DOUBLE, sendTo, myRank);
            MPI.Buffer_detach();
            
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
	}
}
