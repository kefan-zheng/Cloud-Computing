package hadoop.mapreduce2;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


//Map和Reduce都以键值对作为输入和输出
//Map阶段也比较适合处理数据
//Mapper四个参数分别表示输入键、输入值、输出键、输出值的类型
//Hadoop不使用java内嵌类型
//LongWritable 相当于 long；Text 相当于 String； IntWritable 相当于 Integer
class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        //通过空格分割字符串
        //所有字段都设为String，便于后续的字符串拼接
        String line = value.toString();
        String[] srcData= line.split("\\s+");
        String day_id=srcData[0];
        String calling_nbr=srcData[1];
        String called_nbr=srcData[2];
        String calling_optr=srcData[3];
        String called_optr=srcData[4];
        String calling_city=srcData[5];
        String called_city=srcData[6];
        String calling_roam_city=srcData[7];
        String called_roam_city=srcData[8];
        String start_time=srcData[9];
        String end_time=srcData[10];
        String raw_dur=srcData[11];
        String call_type=srcData[12];
        String calling_cell = "#";
        if(srcData.length == 14){
            calling_cell = srcData[13];
        }
        // 使用通话类型加运营商类别作为key 使用硬编码值1作为value
        context.write(new Text(call_type + calling_optr), new IntWritable(1));
    }
}
