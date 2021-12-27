package hadoop.mapreduce1;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class MyJob {

    public static void main(String[] args) throws Exception {
        //输入路径
        //String inputPath = "hdfs://Master:9000/input";
        //输出路径，必须是不存在的，空文件加也不行。
        //String outputPath = "hdfs://Master:9000/output/mr1";

        Configuration conf = new Configuration();
        Job job = new Job(conf);
        //指定运行的作业类
        job.setJarByClass(MyJob.class);
        job.setJobName("Compute Case 1");

        //指定输入数据的位置
        FileInputFormat.addInputPath(job, new Path(args[0]));
        //指定输出数据的位置
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //指定job的map处理类和reduce处理类
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        //指定输出的key和value
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //提交执行
        job.waitForCompletion(true);
        System.out.println("Finished");
    }
}
