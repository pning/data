package data.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropConfig {
	private Properties props = null;

	public PropConfig() {
		props = new Properties();
	}

	public PropConfig(String path) throws IOException {
		props = new Properties();
		loadResource(path);
	}

	public void loadResource(String path) throws IOException {
		if (!path.startsWith("/")) { // see relative path as system resource
			InputStream in = ClassLoader.getSystemResourceAsStream(path);
			props.load(in);
			in.close();
		} else {
			FileReader fr = new FileReader(path);
			props.load(fr);
			fr.close();
		}
	}

	public String getProperty(String key) {
		return props.getProperty(key);
	}
	
	// public static void main(String[] args) throws IOException {
	// String s=new
	// PropConfig(Constants.HBASE_PROP_CONF_FILE).getProperty("rsa.private_key");
	// System.out.println(s);
	// }
}
