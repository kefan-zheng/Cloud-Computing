package hadoop.mapreduce1;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reduce extends Reducer<Text, Text, Text, IntWritable>{
    @Override
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        //平均通话次数=计算一月中的总通话次数/天数
        //遍历计算次数
        int call_num = 0;
        for (Text value : values) {
            call_num++;
        }
        int average_call_num = call_num/29;
        context.write(key, new IntWritable(average_call_num));
    }
}
