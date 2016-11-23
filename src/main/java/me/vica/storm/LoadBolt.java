package me.vica.storm;

import com.google.gson.JsonParseException;
import me.vica.po.AccessLog;
import me.vica.tools.LogParser;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * The Bolt Of AccessLog Json Message Load And First Process
 * Created by Vica-tony on 10/31/2016.
 */
public class LoadBolt extends BaseBasicBolt {

    //按小时统计，格式化时间精度为每小时
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH");

    public void execute(Tuple input, BasicOutputCollector collector) {
        String sourceLine = input.getString(0);
        if(sourceLine.trim().length() < 1) return;
        try {
            AccessLog entity = LogParser.parse(sourceLine);
            collector.emit(new Values(format.format(entity.getTime_local())+" "+entity.getRequest_url(),
                    entity));
        }catch (JsonParseException ex){
            // Nothing To Do
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("id","entity"));
    }
}
