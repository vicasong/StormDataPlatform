package me.vica.storm;

import me.vica.tools.DBWriter;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Summary Count Data And Write DB
 * Created by Vica-tony on 10/31/2016.
 */
public class SummaryBolt extends BaseBasicBolt {

    private DBWriter dbWriter ;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        dbWriter = new DBWriter();
        String url = stormConf.get("mysql.url").toString();
        String username = stormConf.get("mysql.username").toString();
        String password = stormConf.get("mysql.password").toString();
        String tabname = stormConf.get("mysql.tablename").toString();
        dbWriter.initConnection(url,username,password,tabname);
        super.prepare(stormConf, context);
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        //"time","resource","pv","uv","4xxPv","3xxPv","otherPv","maxMethod", "maxUser"
        String time = input.getStringByField("time");
        String resource = input.getStringByField("resource");
        int pv = input.getIntegerByField("pv");
        int uv = input.getIntegerByField("uv");
        int pv4xx = input.getIntegerByField("4xxPv");
        int pv3xx = input.getIntegerByField("3xxPv");
        int otherPv = input.getIntegerByField("otherPv");
        String maxMethod = input.getStringByField("maxMethod");
        String maxUser = input.getStringByField("maxUser");

        dbWriter.write(time,resource,pv,uv,pv3xx,pv4xx,otherPv,maxMethod,maxUser);
    }

    @Override
    public void cleanup() {
        dbWriter.close();
        super.cleanup();
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //Nothing
    }
}
