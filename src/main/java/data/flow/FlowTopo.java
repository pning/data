package data.flow;

import java.util.Arrays;

import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.format.FileNameFormat;
import org.apache.storm.hdfs.bolt.format.RecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.TimedRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.TimedRotationPolicy.TimeUnit;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.hdfs.bolt.sync.SyncPolicy;

import data.util.Constants;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;

public class FlowTopo {

	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException, InterruptedException {
		// storm-kafka
		BrokerHosts zk = new ZkHosts(Constants.ZkHosts);
		SpoutConfig kafkaConf = new SpoutConfig(zk, Constants.KAFKA_TOPIC,
				Constants.KAFKA_ZKROOT, Constants.KAFKA_TOPIC_ID);
		kafkaConf.scheme = new SchemeAsMultiScheme(new StringScheme());
		kafkaConf.forceFromStart = false;
		kafkaConf.zkServers = Arrays.asList(new String[] { "master", "node1",
				"node2" });
		kafkaConf.zkPort = 2181;
		KafkaSpout kafkaSpout = new KafkaSpout(kafkaConf);

		// storm-hdfs
		RecordFormat format = new DelimitedRecordFormat()
				.withFieldDelimiter("\t");
		SyncPolicy syncPolicy = new CountSyncPolicy(1000);
		FileRotationPolicy rotationPolicy = new TimedRotationPolicy(1.0f,
				TimeUnit.HOURS);
		// FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(10.0f,
		// Units.MB);
		FileNameFormat fileNameFormat = new DefaultFileNameFormat().withPath(
				"/foo/").withExtension(".log");
		HdfsBolt bolt = new HdfsBolt().withFsUrl(Constants.HDFS_PATH)
				.withFileNameFormat(fileNameFormat).withRecordFormat(format)
				.withRotationPolicy(rotationPolicy).withSyncPolicy(syncPolicy);

		// storm-hbase

		// config
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("hdfs-kafka", kafkaSpout, 5);
		builder.setBolt("hdfs-dec", new DecryptionBolt(), 3).shuffleGrouping(
				"hdfs-kafka");
		builder.setBolt("hdfs", bolt, 2).shuffleGrouping("hdfs-dec");
		builder.setBolt("hbase", new HbaseFlowBolt(), 2).shuffleGrouping(
				"hdfs-dec");

		Config conf = new Config();
		if (args != null && args.length > 0) {
			conf.setNumWorkers(3);
			StormSubmitter.submitTopology(args[0], conf,
					builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(3);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("hdfs", conf, builder.createTopology());
			Thread.sleep(10000);
			cluster.shutdown();
		}
	}
}
