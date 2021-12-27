package hadoop.mapreduce3;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Map extends Mapper<LongWritable, Text, Text, Text>{
    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException{
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

        //获取通话时间，转换成整形便于后续计算
        String[] startTime = start_time.split(":");
        int startHour = Integer.parseInt(startTime[0]);
        int startMinute = Integer.parseInt(startTime[1]);
        int startSecond = Integer.parseInt(startTime[2]);

        //计算各时间段通话持续时间（以秒为单位）
        int[] boundary = {3,6,9,12,15,18,21,24};
        int[] res = {0,0,0,0,0,0,0,0};
        //计算开始时间的边界，和通话时长比较，有以下两种情况
        //1.若通话时长小于到边界的时间，则结束
        //2.若通话时长大于到边界的时间，说明跨了时间段，更新起始时间和剩余通话时长，直到情况1出现
        int remainTime = Integer.parseInt(raw_dur);
        int period = startHour/3;
        int remainBoundaryTime = 3600*(boundary[period]-startHour)-60*startMinute-startSecond;
        computePeriodCallTime(res, remainTime, remainBoundaryTime, period);
        for (int i = 0;i<res.length;i++){
            String str = i + ":" + res[i];
            context.write(new Text(calling_nbr), new Text(str));
            context.write(new Text(called_nbr), new Text(str));
        }
    }

    public void computePeriodCallTime(int[] res, int remainTime, int remainBoundaryTime, int curPeriod){
        //剩余通话时长<剩余边界时长，结束
        int period = curPeriod;
        if (remainTime <= remainBoundaryTime){
            res[period]+=remainTime;
        }else {
            //剩余通话时长>剩余边界时长
            //更新剩余通话时长、剩余边界时长和时间段
            remainTime -= remainBoundaryTime;
            period = (period + 1) % 8;
            remainBoundaryTime = 10800;
            computePeriodCallTime(res, remainTime, remainBoundaryTime, period);
        }
    }
}
