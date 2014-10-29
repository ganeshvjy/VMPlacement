///////////////////////////////////////////////////////////////////
// Program Name: BestFitVMPlacement.java                         //
// Language    : JAVA                                            //
// Platform    : Toshiba Satellite Windows 8                     //
// Application : Best Fit Algorithm for Virtual Machine Placement//
// Author      : Ganesh Thiagarajan,                             //
//               ganesh_vjy@yahoo.com                            //
///////////////////////////////////////////////////////////////////

import java.util.Random;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//Input: CSV file with CPU utilization,memory and Bandwidth consumption details
//Output: Place VMs on PM's based on BEST FIT algorithm
public class BestFitVMPlacement {
	Vector<Bin>unit = new Vector<Bin>();
	int numBins;
	public BestFitVMPlacement(){	
	}
	//---<Best fit algorithm>-------
	public boolean bestFitAlgorithm(Vector<Bin> sys,Object obj,int numBins){
    	int bestFitPresent = -1;
    	Bin bestFitBin = null;
    	for(int i=0;i<numBins;i++){
    		int existingCapacity = sys.elementAt(i).capacity;
    		if(existingCapacity == 100) continue;
    		if(existingCapacity + obj.capacity_ <= 100){
    			
    			if(bestFitBin == null){
    				bestFitPresent = i;
    				bestFitBin = sys.elementAt(i);
    			}
    			else{
    				if(existingCapacity > bestFitBin.capacity){
    					bestFitPresent = i;
    					bestFitBin = sys.elementAt(i);
    				}
    			}
    		}
    		else{
    			continue;
    		}
    	}
    	if(bestFitPresent!=-1){
    		sys.elementAt(bestFitPresent).addObject(obj);
    		return true;
    	}
    	return false;
    }
	public static void main(String []args){
		//Get the values online or off line
    	System.out.println("Best Fit Algorithm Demonstration");
    	
    	
    	BufferedReader br = null;
    	String comma = ",";
    	try{
    		//Read the input CSV data sheet file>------
    		String line;
    		br=new BufferedReader(new FileReader("input.txt"));
    		while((line =br.readLine())!= null){
    			String[] inElements = null;
    			inElements = line.split(comma);
    			BestFitVMPlacement bftVM = new BestFitVMPlacement();
    			//--<Prints the time taken for placing VM's on PM>------
    			long timeBegin = System.currentTimeMillis();
    			for(int i=0;i<inElements.length;i++){
    				
    				Object obj = new Object(Integer.parseInt(inElements[i]));
    				
    				if(bftVM.bestFitAlgorithm(bftVM.unit,obj,bftVM.numBins)==false){
    					Bin b = new Bin();
    					b.addObject(obj);
    					bftVM.numBins++;
    					bftVM.unit.add(b);
    				}
    			}
    			long timeEnd = System.currentTimeMillis();
    			long time = timeEnd-timeBegin;
    			System.out.println("Time:");
    			System.out.println(time);
    			//---Prints the server and its corresponding allocation
    			//---Prints Server's CPU and memory utilization
    			//---Prints Time taken for handling each request among various algorithms
    			
    			System.out.println("Number of Servers: "+ bftVM.numBins);
    			for(int i=0;i<bftVM.numBins;i++){
    				Bin server = bftVM.unit.elementAt(i);
    				System.out.print("Server"+i+":");
    				for(int j=0;j< server.numObjects;j++){
    					int val = server.bin.elementAt(j).capacity_;
    					System.out.print(val+"|");
    				}
    				System.out.println();
    			}
    		}
	    }
    	catch(IOException e){
    		e.printStackTrace();
    	}
    	finally{
    		try 
    		{
    			if (br != null)br.close();
			} 
    		catch (IOException ex) 
			{
				ex.printStackTrace();
			}
    	}
	}
}
