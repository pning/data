package data.flow;

import java.text.SimpleDateFormat;
import java.util.Date;

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

public class ConfigureHDFSBolt {
	public static HdfsBolt configureHDFSBolt(String path) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		RecordFormat format = new DelimitedRecordFormat().withFieldDelimiter("\t");
		SyncPolicy syncPolicy = new CountSyncPolicy(1000);
		FileRotationPolicy rotationPolicy = new TimedRotationPolicy(1.0f, TimeUnit.DAYS);
		// FileRotationPolicy rotationPolicy = new FileSizeRotationPolicy(10.0f,
		// Units.MB);
		FileNameFormat fileNameFormat = new DefaultFileNameFormat().withPath(path)
				.withPrefix(sdf.format(new Date()) + "_").withExtension(".log");

		HdfsBolt bolt = new HdfsBolt().withFsUrl(Constants.HDFS_PATH).withFileNameFormat(fileNameFormat)
				.withRecordFormat(format).withRotationPolicy(rotationPolicy).withSyncPolicy(syncPolicy);
		return bolt;
	}
}
