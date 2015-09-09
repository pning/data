package data.flow;

import java.util.Arrays;

import backtype.storm.spout.SchemeAsMultiScheme;
import data.util.Constants;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

public class ConfigureKafkaSpout {
	public static KafkaSpout configureKafkaSpout(String KAFKA_TOPIC, String KAFKA_ZKROOT, String KAFKA_TOPIC_ID) {
		BrokerHosts zk = new ZkHosts(Constants.ZkHosts);
		SpoutConfig kafkaConf = new SpoutConfig(zk, KAFKA_TOPIC, KAFKA_ZKROOT, KAFKA_TOPIC_ID);
		kafkaConf.scheme = new SchemeAsMultiScheme(new StringScheme());
		kafkaConf.forceFromStart = false;
		kafkaConf.zkServers = Arrays.asList(new String[] { "master", "node1", "node2" });
		kafkaConf.zkPort = 2181;
		KafkaSpout kafkaSpout = new KafkaSpout(kafkaConf);
		return kafkaSpout;
	}
}
