package me.vica;

import me.vica.storm.CountBolt;
import me.vica.storm.LoadBolt;
import me.vica.storm.SummaryBolt;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import java.io.IOException;
import java.util.Properties;

/**
 * The Main Class
 * Created by Vica-tony on 10/31/2016.
 */
public class Program {

    public static void main(String[] args) {
        Properties properties = loadProperties("config.properties");

        //依次指定Zookeeper集群主机、Kafka消息主题、Zookeeper存储Kafka消息偏移数据的根目录及分区标识名称
        //因此，消息偏移量数据会存储在类似：<Zkroot>/<id>/partition_<partitionNumber>，注意Zkroot必须以/开始
        // TODO: 注意修改ZkRoot和id
        SpoutConfig kafkaConfig = new SpoutConfig(new ZkHosts(properties.getProperty("zookeeper.server")),
                properties.getProperty("kafka.topic"),"/mystorm","access");
        //设置消息解析的Scheme，此处为String字符串
        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        // TODO: 指定并发度,Fields
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("kafka-spout", new KafkaSpout(kafkaConfig));
        builder.setBolt("load-bolt", new LoadBolt(),6).shuffleGrouping("kafka-spout");
        builder.setBolt("count-bolt", new CountBolt(),6).fieldsGrouping("load-bolt", new Fields("id"));
        builder.setBolt("summary-bolt", new SummaryBolt(), 1).shuffleGrouping("count-bolt");

        Config config = new Config();
        config.setMaxSpoutPending(10);
        config.setNumWorkers(1);
        config.setMaxTaskParallelism(10);
        config.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 1000);
        config.put("mysql.url", properties.getProperty("mysql.url"));
        config.put("mysql.username", properties.getProperty("mysql.username"));
        config.put("mysql.password", properties.getProperty("mysql.password"));
        config.put("mysql.tablename", properties.getProperty("mysql.tablename"));

        StormTopology topology = builder.createTopology();
        try {
            StormSubmitter.submitTopology(properties.getProperty("topology.name"), config, topology);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load Properties
     * @param fileName The Properties FileName
     * @return Properties
     */
    private static Properties loadProperties(String fileName){
        Properties prop = new Properties();
        try {
            prop.load(Program.class.getClassLoader().getResourceAsStream(fileName));
        } catch (IOException e) {
            System.err.println("Missing config file : "+fileName);
            System.exit(1);
        }
        return prop;
    }

}
