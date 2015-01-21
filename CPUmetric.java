package com.vmware.vim25.mo.samples;
import com.vmware.vim25.*;

import com.vmware.vim25.mo.*;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.text.SimpleDateFormat;


public class CPUmetric {
	
	// To Write Virtual Machine Information
	private static void writeVmInfo(VirtualMachine vm) {
		System.out.println("Virtual Machine:" + vm.getName());
		VirtualMachineConfigInfo config = vm.getConfig();
		VirtualHardware hw = config.getHardware();
		System.out.println("Memory in MB: " + hw.getMemoryMB());
		System.out.println("# of CPUs: " + hw.getNumCPU());
		VirtualDevice[] devices = hw.getDevice();
		for (int i = 0; i < devices.length; i++) {
			VirtualDevice device = devices[i];
			Description deviceInfo = device.getDeviceInfo();
			System.out.println("Device (" + device.getKey() + "): " + deviceInfo.getLabel() + " : " + deviceInfo.getSummary());
		}
	}

	public static void MigrateMachine(ServiceInstance si,String VMname)
	{
		String newHostName="128.230.96.111";
		Folder rootFlder = si.getRootFolder();
		 
		 try {
			 
			 HostSystem newHostSystem = (HostSystem) new InventoryNavigator(rootFlder).searchManagedEntity("HostSystem",newHostName);
             ComputeResource resource = (ComputeResource) newHostSystem.getParent();
             InventoryNavigator rootNavigator = new InventoryNavigator(rootFlder);
             ManagedEntity MyVM = rootNavigator.searchManagedEntity("VirtualMachine",VMname);
             
             VirtualMachine newVM = (VirtualMachine) MyVM;
			 Task task = newVM.migrateVM_Task(resource.getResourcePool(), newHostSystem, VirtualMachineMovePriority.highPriority, VirtualMachinePowerState.poweredOn);
			 if(task.waitForTask()==Task.SUCCESS)
			 {
				 System.out.println("Virtula Machine Moved");
			 }
			 else
			 {
				 System.out.println("Migration Failed!");
				 TaskInfo info = task.getTaskInfo();
				 System.out.println(info.getError().getFault());
			 }
			 si.getServerConnection().logout();
		 }
		 catch(Exception e)
		 {e.printStackTrace();}
	}
	
	 public static void main(String [] args) throws Exception {
	        
	        String url ="https://128.230.96.117/sdk";
		    String login = "AD\\gthiagar";
		    String password = "ADmpl34!";
		    String MyHost="128.230.96.39";
		    String VMName="gthiagar";
		    
		    int CPUlimit=250;
		    int Memlimit=6000;
		    
		    int Timelimit = 120000;
		    int count = 0 ;
		    ServiceInstance si = new ServiceInstance(new URL(url),login,password,true);
	    
		    // To Write Memory Usage information on to the file and print them in console
		    BufferedWriter File = null;
		    
		    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		    File logFile = new File(timestamp);
		    Folder root = si.getRootFolder();
		    InventoryNavigator rootNavigator = new InventoryNavigator(root);
		    
		    try{
		    	System.out.println(logFile.getCanonicalPath());
				File = new BufferedWriter(new FileWriter(logFile));
				
				FileWriter fWriter = new FileWriter(logFile,true);
				File = new BufferedWriter(fWriter);
				
		    	ManagedEntity[] managedEntities = new InventoryNavigator(si.getRootFolder()).searchManagedEntities("HostSystem");
				System.out.println("   Current Host");
			    for (ManagedEntity managedEntity : managedEntities) {
			    	HostSystem host = (HostSystem) managedEntity;
			    	
			    	ManagedEntity[] mEntities = new InventoryNavigator(host).searchManagedEntities("VirtualMachine");
			    	for (ManagedEntity mEntity : mEntities){
			    		VirtualMachine vm = (VirtualMachine) mEntity;
			    		VMName = vm.getName();
			    		if("gthiagar".compareTo(VMName)==0)
			    		{
			    			MyHost = host.getName();
			    			System.out.println("Host: '" + MyHost + "'");
			    			writeVmInfo(vm);	
			    		}
			    	}
			    	
			    }
		    	
		        ManagedEntity myhost = rootNavigator.searchManagedEntity("HostSystem",MyHost);
		    	ManagedEntity myvm   = rootNavigator.searchManagedEntity("VirtualMachine", VMName);
		    	HostSystem MyHostSystem = (HostSystem) myhost;
		    	VirtualMachine MyVirtualMachine = (VirtualMachine) myvm;
				
		    	while(true)
				{
		    		FileWriter filewrite = new FileWriter(logFile,true);
		    		File = new BufferedWriter(filewrite);
		    		String currtime = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		    		
		    		File.newLine();
					File.append(currtime);
					System.out.println("Virtual Machine Memory:"+MyVirtualMachine.getSummary().quickStats.getGuestMemoryUsage());
					System.out.println("Virtual Machine CPU Usage:"+MyVirtualMachine.getSummary().quickStats.getOverallCpuUsage());
					
					File.append("Virtual Machine Memory:"+MyVirtualMachine.getSummary().quickStats.getGuestMemoryUsage());
					File.append("Virtual Machine CPU Usage:"+MyVirtualMachine.getSummary().quickStats.getOverallCpuUsage());
					
					System.out.println("Host Memory:"+MyHostSystem.getSummary().quickStats.getOverallMemoryUsage());
					System.out.println("Host USage:"+MyHostSystem.getSummary().quickStats.getOverallCpuUsage());
				    
				    File.append("Host Memory:"+MyHostSystem.getSummary().quickStats.getOverallMemoryUsage());
				    File.append("Host USage:"+MyHostSystem.getSummary().quickStats.getOverallCpuUsage());
				    
				    File.newLine();
				    
				    Integer CPUUsage= MyHostSystem.getSummary().quickStats.getOverallCpuUsage();
				    Integer MemUsage=MyHostSystem.getSummary().quickStats.getOverallMemoryUsage();
				    
				    Thread.sleep(10000);
				    count = count + 10000;
				    
				    if((count > Timelimit) && (CPUUsage > CPUlimit || MemUsage >Memlimit))
			          {
				    	  count =0;
			        	  System.out.println("Migrate VM");
			        	  MigrateMachine(si,VMName);
			        	  break;
			        	  
			          }
				    File.close();
				}
		    }
		    catch(Exception e){
		    	e.printStackTrace();
		    	}
		    finally
		    	{
		    		try{File.close();}
		    		catch(Exception e){}
		    	}	
	 }
}
	 
