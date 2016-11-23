package me.vica.storm;

import me.vica.tools.DBWriter;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * Summary Count Data And Write DB
 * Created by Vica-tony on 10/31/2016.
 */
public class SummaryBolt extends BaseBasicBolt {

    private String url,username,password,tabname;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        url = stormConf.get("mysql.url").toString();
        username = stormConf.get("mysql.username").toString();
        password = stormConf.get("mysql.password").toString();
        tabname = stormConf.get("mysql.tablename").toString();
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
        DBWriter.write(url, username, password, tabname, time,resource,pv,uv,pv3xx,pv4xx,otherPv,maxMethod,maxUser);
    }

    @Override
    public void cleanup() {
        DBWriter.close();
        super.cleanup();
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //Nothing
    }
}
