package hadoop.mapreduce1;
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


//Map和Reduce都以键值对作为输入和输出
//Map阶段也比较适合处理数据
//Mapper四个参数分别表示输入键、输入值、输出键、输出值的类型
//Hadoop不使用java内嵌类型
//LongWritable 相当于 long；Text 相当于 String； IntWritable 相当于 Integer
class Map extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        //通过空格分割字符串
        //所有字段都设为String，便于后续的字符串拼接
        //这里只获取了第一个计算用例中需要的字段
        String line = value.toString();
        String[] srcData= line.split("\\s+");
        String day_id=srcData[0];
        String calling_nbr=srcData[1];
        String called_nbr=srcData[2];
        String start_time=srcData[9];

        context.write(new Text(calling_nbr), new Text(day_id+start_time));
        context.write(new Text(called_nbr), new Text(day_id+start_time));
    }
}




