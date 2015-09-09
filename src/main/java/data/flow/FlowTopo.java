package data.flow;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class FlowTopo {

	public static void main(String[] args)
			throws AlreadyAliveException, InvalidTopologyException, InterruptedException {
		// config
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("hdfs-app-kafka",
				ConfigureKafkaSpout.configureKafkaSpout("app_log", "/storm-hdfs/app_log", "app_log"), 5);
		builder.setSpout("hdfs-pc-kafka",
				ConfigureKafkaSpout.configureKafkaSpout("pc_log", "/storm-hdfs/pc_log", "pc_log"), 5);
		builder.setBolt("hdfs-dec", new DecryptionBolt(), 3).shuffleGrouping("hdfs-app-kafka");
		builder.setBolt("hdfs-app", ConfigureHDFSBolt.configureHDFSBolt("/logs/app"), 2).shuffleGrouping("hdfs-dec");
		builder.setBolt("hdfs-pc", ConfigureHDFSBolt.configureHDFSBolt("/logs/pc"), 2).shuffleGrouping("hdfs-pc-kafka");
		// builder.setBolt("hbase", new HbaseFlowBolt(),
		// 2).shuffleGrouping("hdfs-dec");

		Config conf = new Config();
		if (args != null && args.length > 0) {
			conf.setNumWorkers(3);
			StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(3);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("flow", conf, builder.createTopology());
			Thread.sleep(10000);
			cluster.shutdown();
		}
	}
}
