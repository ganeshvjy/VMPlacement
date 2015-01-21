
/*
 * HomeworkReducer.java
 *
 * Created on Oct 22, 2012, 5:46:32 PM
 */

package org.sample;

import java.io.IOException;

import java.util.Vector;

// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class HomeworkReducer extends Reducer<Text,Text,Text,Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
                        throws IOException, InterruptedException {
    	
    	System.out.print(key);	
    	String src="";
    	for (Text val : values) {
    		src+= "," + val;
    	}
    	System.out.println(src);
    }
}
