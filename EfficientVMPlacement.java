import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


public class EfficientVMPlacement {
	Vector<Bin>unit = new Vector<Bin>();
	int numBins;
    public EfficientVMPlacement(){
    	
    }
    public void addBin(Bin b){
    	unit.add(b);
    }
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
    
    public void efficientBestFitAlgorithm(Vector<Bin> sys,Object obj,int numBins){
    	for(int i=0;i<numBins;i++){
    		int prevCapacity = sys.elementAt(i).capacity;
    		if(prevCapacity==100)continue;
    		int migrateVM = -1;
    		Bin b = sys.elementAt(i);
    		//--Identify the migratable VM
    		for(int j=0;sys.elementAt(i).numObjects>1 && j<sys.elementAt(i).numObjects;j++){
    			int temp = prevCapacity - b.bin.elementAt(j).capacity_ + obj.capacity_;
    			if((temp > prevCapacity)&& (temp<=100)){
    				if(migrateVM == -1){
    					migrateVM = j;
    				}
    				else{
    					int prevMigrateVM = prevCapacity - b.bin.elementAt(migrateVM).capacity_ + obj.capacity_;
    					if(temp > prevMigrateVM){
    						migrateVM = j;
    					}
    				}
    			}
    		}
    		//--If migratable VM exists
    		if(migrateVM !=-1){
    			Object nextMigrateVM = sys.elementAt(i).bin.elementAt(migrateVM);
    			sys.elementAt(i).bin.remove(migrateVM);
    			sys.elementAt(i).bin.add(obj);
    			sys.elementAt(i).capacity -= nextMigrateVM.capacity_ ;
    			sys.elementAt(i).capacity += obj.capacity_;
    			obj = nextMigrateVM;
    		}	
    	}
    	//Adding server is the only go, add new Server
    	Bin b = new Bin();
    	b.addObject(obj);
    	numBins++;
    	sys.add(b);
    }
    public static void main(String []args)throws Exception
    {
    	//Get the values online or off line
    	System.out.println("Online Efficient Best Fit Algorithm Demonstration");
    	BufferedReader br = null;
    	String comma = ",";
    	try{
    		String line;
    		br=new BufferedReader(new FileReader("C:\\Users\\MANJUSHRI\\Desktop\\input.txt"));
    		while((line =br.readLine())!= null){
    			String[] inElements = null;
    			inElements = line.split(comma);
    			EfficientVMPlacement effVM = new EfficientVMPlacement();
    			for(int i=0;i<inElements.length;i++)
    			{
    				Object obj=new Object(Integer.parseInt(inElements[i]));
    				if(effVM.numBins > 0){
    					if(effVM.bestFitAlgorithm(effVM.unit,obj,effVM.numBins)==false){
    					 effVM.efficientBestFitAlgorithm(effVM.unit,obj,effVM.numBins);	
    					 effVM.numBins = effVM.unit.size();
    					}
    				}
    				else{
    					effVM.numBins++;
    					Bin b = new Bin();
    					b.addObject(obj);
    					effVM.unit.add(b);
    				}
    			}
    			//---Prints the server and its corresponding allocation
    			//---Prints Server's CPU and memory utilization
    			//---Prints Time taken for handling each request among various algorithms
    			//--Prints number of migrated VMs
    			System.out.println("Number of Servers: "+ effVM.numBins);
    			for(int i=0;i<effVM.numBins;++i){
    				Bin server = effVM.unit.elementAt(i);
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
