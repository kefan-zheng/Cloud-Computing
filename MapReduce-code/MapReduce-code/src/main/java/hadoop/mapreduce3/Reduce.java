package hadoop.mapreduce3;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

public class Reduce extends Reducer<Text, Text, Text, Text>{
    @Override
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException{

        double[] periodTime = {0,0,0,0,0,0,0,0};
        double rate = 0;
        StringBuilder outputStr = new StringBuilder();
        for (Text value : values){
            String record = value.toString();
            String[] data = record.split(":");
            int period = Integer.parseInt(data[0]);
            int time = Integer.parseInt(data[1]);
            periodTime[period] += time;
        }

        for (int i=0;i<periodTime.length;i++){
            rate = periodTime[i]/ Arrays.stream(periodTime).sum();
            outputStr.append(rate);
            if (i != 7){
                outputStr.append("  ");
            }
        }

        context.write(key, new Text(outputStr.toString()));
    }
}
