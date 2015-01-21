
/*
 * HomeworkMapper.java
 *
 * Created on Oct 22, 2012, 5:41:50 PM
 */

package org.sample;



import java.io.IOException;
import java.util.HashMap;



import java.util.Vector;

// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class HomeworkMapper extends Mapper<Text,Text,Text,Text> {
    
	HashMap<String,Vector<String>> inNodes = new HashMap<String,Vector<String>>();
	HashMap<String,Vector<String>> outNodes = new HashMap<String,Vector<String>>();
	
    @Override
    protected void map(Text key, Text value, Context context)
                    throws IOException, InterruptedException {
    	if(outNodes.get(key.toString())== null){
    		Vector<String>oct = new Vector<String>();
    		oct.add(value.toString());
    		outNodes.put(key.toString(),oct);
    	}
    	else{
    		outNodes.get((key).toString()).add(value.toString());
    	}
    	if(inNodes.get(value.toString())== null){
    		Vector<String>vct = new Vector<String>();
    		vct.add(key.toString());
    		inNodes.put(value.toString(),vct);
    	}
    	else{
    		inNodes.get(value.toString()).add(key.toString());
    	}
        if(inNodes.size()>1){
        	if(inNodes.containsKey(key.toString()))
        	{
        		for(String source:inNodes.get(key.toString())){
        			if(!value.toString().equals(source)){
        				System.out.println(value.toString() +","+ "<"+key.toString()+","+source+">");
        				context.write(new Text(value.toString() + ","+ key.toString()),new Text(source));
        			}
        		}
        	}
    		if(outNodes.containsKey(value.toString())){
    			for(String target:outNodes.get(value.toString())){
        			if(!key.toString().equals(target)){
        				System.out.println(target+","+"<"+value.toString()+","+key.toString()+">");
        				context.write(new Text(target+ ","+ value.toString()), new Text(key.toString()));
        			}
        		}
    		}
    	}
    }
}
