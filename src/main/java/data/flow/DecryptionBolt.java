package data.flow;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import data.util.RSAUtil;

public class DecryptionBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple input) {
		String line = input.getString(0);
		try {
			String log = RSAUtil.getLog(line);
			collector.emit(input, new Values(log));
		} catch (Exception e) {
			e.printStackTrace();
		}
		collector.ack(input);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}
}
