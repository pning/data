package data.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogUtil {
	// app log split
	public static String[] app(String log) {
		String value[] = new String[8];
		Pattern p = Pattern
				.compile("^\\[.*\\].*\\|\\|.*\\|\\|.*\\|\\|.*\\|\\|.*\\|\\|.*\\|\\|.*");
		Matcher m = p.matcher(log);
		if (m.matches()) {
			try {
				value[0] = DateFormatUtil.dateformat(log.split(" ")[0]
						.substring(1));// time
			} catch (Exception e) {
				value[0] = "";
			}
			String shuju = log.substring(log.indexOf("]") + 2);
			try {
				value[1] = shuju.split("\\|\\|")[0];
			} catch (Exception e) {
				value[1] = "";
			}// sys_type
			try {
				value[2] = shuju.split("\\|\\|")[1];
			} catch (Exception e) {
				value[2] = "";
			}// url
			try {
				value[3] = shuju.split("\\|\\|")[5];
			} catch (Exception e) {
				value[3] = "";
			}// version
			try {
				value[4] = shuju.split("\\|\\|")[2];
			} catch (Exception e) {
				value[4] = "";
			}// parameter
			try {
				value[5] = shuju.split("\\|\\|")[3];
			} catch (Exception e) {
				value[5] = "";
			}// channel
			try {
				value[6] = shuju.split("\\|\\|")[4];
			} catch (Exception e) {
				value[6] = "";
			}// IMEI
			try {
				value[7] = shuju.split("\\|\\|")[6];
			} catch (Exception e) {
				value[7] = "";
			}
			// ip
			return value;
		} else {
			return null;
		}
	}

	// pc & wap log split
	public static String[] web(String log) {

		return null;
	}

	public static void main(String[] args) {
		String log = "[28/Jul/2015:00:00:02 +0800] ||/banner/listsExt/||111||222||8A3E0B7F-6F37-41B4-B649-ECD361FFA321||ios_3_4_2||";
		System.out.println(log);
		if (app(log) != null) {
			for (int i = 0; i <= 7; i++) {
				System.out.println(app(log)[i] + "-------");
			}

		}
	}
}
