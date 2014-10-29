import java.util.Vector;

//--Acts as server class
public class Bin {
   int numObjects;
   Vector<Object>bin = new Vector<Object>();
   int capacity;
   int maxCapacity;
   public Bin(){
	   maxCapacity=100; 
   }
   public void addObject(Object o ){
	   bin.add(o);
	   capacity+=o.capacity_;
	   numObjects+=1;
   }
   public void removeObject(Object o){
	   bin.remove(o);
	   capacity-=o.capacity_;
	   numObjects-=1;
   }
}
