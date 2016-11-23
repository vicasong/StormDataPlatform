package me.vica.storm;

import me.vica.po.AccessLog;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;

/**
 * The Next Bolt To Count PV/UV Info And Other Analysis
 * Created by Vica-tony on 10/31/2016.
 */
public class CountBolt extends BaseBasicBolt {

    private Map<String, Integer> counter = null;
    private Map<String, Integer> status = null;
    private Map<String, Integer> method = null;
    private String pvKey = "pv";

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        counter = new HashMap<String, Integer>();
        status = new HashMap<String, Integer>();
        method = new HashMap<String, Integer>();
        super.prepare(stormConf, context);
    }

    private void countPvUvByIp(AccessLog entity){

        //Count The Page View
        Integer pv = counter.get(pvKey);
        if(pv == null){
            pv = 0;
        }
        counter.put(pvKey, ++ pv);

        //Count The User View
        String uvKey = "_uv";
        if(!counter.containsKey(entity.getRemote_addr()+ uvKey)){
            counter.put(entity.getRemote_addr()+ uvKey, 1);
        }
    }

    private void countStatus(AccessLog entity){
        //Count Status Pv
        Integer pv3xx = status.get("3xxPv");
        Integer pv4xx = status.get("4xxPv");
        Integer pvOther = status.get("otherPv");
        String stat = String.valueOf(entity.getStatus());
        if(stat.startsWith("3"))
            status.put("3xxPv", pv3xx == null? 1:++ pv3xx);
        else if(stat.startsWith("4"))
            status.put("4xxPv", pv4xx == null? 1:++ pv4xx);
        else
            status.put("otherPv", pvOther == null? 1:++ pvOther);
        //Count Method Pv By User
        String mKey = entity.getRequest_method() + " " +entity.getRemote_addr();
        Integer mCount = method.get(mKey);
        if(mCount == null){
            mCount = 0;
        }
        method.put(mKey, ++ mCount);
    }

    private int safeGet(Map<String,Integer> map, String key){
        return map.get(key) == null ? 0: map.get(key);
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        AccessLog entity = (AccessLog) input.getValueByField("entity");
        String timeId = input.getValueByField("id").toString().split(" ")[0];
        countPvUvByIp(entity);
        countStatus(entity);

        String maxMethod = "";
        int maxMethodCount = 0;
        for(String key : method.keySet()){
            if(method.get(key) > maxMethodCount){
                maxMethodCount = method.get(key);
                maxMethod = key;
            }
        }

        String[] temp = maxMethod.split(" ");

        collector.emit(new Values(timeId, entity.getRequest_url(), counter.get(pvKey), counter.size() - 1,
                safeGet(status, "4xxPv"), safeGet(status, "3xxPv"), safeGet(status, "otherPv"),
                temp[0], temp[1]));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("time","resource","pv","uv","4xxPv",
                "3xxPv","otherPv","maxMethod", "maxUser"));
    }
}
