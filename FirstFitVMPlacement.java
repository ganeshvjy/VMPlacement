////////////////////////////////////////////////////////////////////////
// Program Name: FirstFitVMPlacement.java                             //
// Language    : JAVA                                                 //
// Platform    : Toshiba Satellite Windows 8                          //
// Application : Efficient Fit Algorithm for Virtual Machine Placement//
// Author      : Ganesh Thiagarajan,                                  //
//               ganesh_vjy@yahoo.com                                 //
////////////////////////////////////////////////////////////////////////

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

//Input: CSV file with CPU utilization,memory and Bandwidth consumption details
//Output: Place VMs on PM's based on BEST FIT algorithm
public class FirstFitVMPlacement {
	Vector<Bin>unit = new Vector<Bin>();
	int numBins;
	public FirstFitVMPlacement(){
		
	}
	//---<First fit algorithm>-------
	public boolean firstFitAlgorithm(Vector<Bin> sys,Object obj,int numBins){
		for(int i=0;i<numBins;i++){
			int existingCapacity = sys.elementAt(i).capacity;
			if(existingCapacity == 100) continue;
			if(existingCapacity + obj.capacity_ <=100){
				sys.elementAt(i).bin.add(obj);
				sys.elementAt(i).numObjects++;
				sys.elementAt(i).capacity+= obj.capacity_;
				return true;
			}
		}
		return false;
	}
	public static void main(String []args){
		//Get input values from CSV file
    	System.out.println("First Fit Algorithm Demonstration");
    	BufferedReader br = null;
    	String comma = ",";
    	try{
    		String line;
    		br=new BufferedReader(new FileReader("input.txt"));
    		while((line =br.readLine())!= null){
    			String[] inElements = null;
    			inElements = line.split(comma);
    			FirstFitVMPlacement fftVM = new FirstFitVMPlacement();
    			long timeBegin = System.currentTimeMillis();
    			//--<Prints the time taken for placing VM's on PM>------
    			for(int i=0;i<inElements.length;i++){
    				Object obj=new Object(Integer.parseInt(inElements[i]));
    				if(fftVM.numBins > 0){
    					if(fftVM.firstFitAlgorithm(fftVM.unit,obj,fftVM.numBins)==false){
    						Bin b = new Bin();
    						b.addObject(obj);
    						fftVM.numBins++;
    						fftVM.unit.add(b);
    					}
    				}
    				else{
    					fftVM.numBins++;
    					Bin b = new Bin();
    					b.addObject(obj);
    					fftVM.unit.add(b);
    				}
    			}
    			
    			long timeEnd = System.currentTimeMillis();
    			long time = timeEnd-timeBegin;
    			System.out.println("Time:");
    			System.out.println(time);
    			System.out.println("Number of Servers: "+ fftVM.numBins);
    			//---Prints the server and its corresponding allocation
    			//---Prints Server's CPU and memory utilization
    			//---Prints Time taken for handling each request among various algorithms
    			
    			for(int i=0;i<fftVM.numBins;i++){
    				Bin server = fftVM.unit.elementAt(i);
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
