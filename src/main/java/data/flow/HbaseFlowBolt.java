package data.flow;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class HbaseFlowBolt extends BaseRichBolt {

	/**
	 * storm to hbase bolt
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;

	public static class Config {
		public static Configuration configuration;
		public static Connection con;
		static {
			configuration = HBaseConfiguration.create();
			//configuration.set("hbase.zookeeper.property.clientPort", "2181");
			//configuration.set("hbase.zookeeper.quorum", "master,node1,node2");
			configuration.addResource("hbase-site.xml");
			try {
				con = ConnectionFactory.createConnection(configuration);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;

	}

	public void execute(Tuple input) {
		try {
			String url = input.getString(0);
			if (url != null) {
				Table table = Config.con.getTable(TableName
						.valueOf("storm-hbase"));
				Put put = new Put("1".getBytes());
				put.addColumn("url".getBytes(), url.getBytes(), "1".getBytes());
				table.put(put);
			}
		} catch (Exception e) {
			e.printStackTrace();
			collector.fail(input);
		}
		collector.ack(input);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

}
