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

import mpi.MPI;
import mpi.Request;

public class Test {
	    public static void main(String[] args)
	    {
	        MPI.Init(args);
	        int me = MPI.COMM_WORLD.Rank();
	        int tasks = MPI.COMM_WORLD.Size();

	        MPI.COMM_WORLD.Barrier();

	        if(me == 0)
	        {
	        	
	        	Solution cat = new Solution(null, 15.0);
	        	Communication.sendAll(cat, 0, 3);
//	           // cat.speak();
//
//	            ByteBuffer byteBuff = ByteBuffer.allocateDirect(2000 + MPI.SEND_OVERHEAD);
//	            MPI.Buffer_attach(byteBuff);
//
//	            try
//	            {
//	                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//	                ObjectOutput out = null;
//	                out = new ObjectOutputStream(bos);
//	                out.writeObject(cat);
//	                byte[] bytes = bos.toByteArray();
//
//	                System.out.println("Serialized to " + bytes.length);
//
//	                MPI.COMM_WORLD.Isend(bytes, 0, bytes.length, MPI.BYTE, 1, 0);
//	            }
//	            catch(IOException ex)
//	            {
//	            	ex.printStackTrace();
//	            }
	        }
	        else
	        {
	        	Boolean burgy = true;
	        	while(burgy) {
	        		burgy = (Communication.iRecieve() == null);
	        	}
	        	
//	        	for (int i = 0; i < 2; i++) {
//	        		byte[] bytes = new byte[200];
//	                Solution recv = null;
//	                Request status = MPI.COMM_WORLD.Irecv(bytes, 0, 200, MPI.BYTE, MPI.ANY_SOURCE, 0);
//
//						status.Wait();
//				
//						System.out.println(status.Test().countInBytes);
//	                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
//	                ObjectInput in = null;
//	                try
//	                {
//	                    in = new ObjectInputStream(bis);
//	                    Object obj = in.readObject();
//	                    recv = (Solution)obj;
//
//	                    System.out.println(recv.getCost());
//	                }
//	                catch(IOException ex)
//	                {
//	                	ex.printStackTrace();
//	                }
//	                catch(ClassNotFoundException cnf)
//	                {
//	                	cnf.printStackTrace();
//	                }
//	        	}
	            
	        }

	        MPI.COMM_WORLD.Barrier();
	        MPI.Finalize();
	    }
}
