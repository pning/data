package data.util;

import java.util.HashMap;
import java.util.Map;

public class DateFormatUtil {
	public static String dateformat(String s) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Jan", "01");
		map.put("Feb", "02");
		map.put("Mar", "03");
		map.put("Apr", "04");
		map.put("May", "05");
		map.put("Jun", "06");
		map.put("Jul", "07");
		map.put("Aug", "08");
		map.put("Sep", "09");
		map.put("Oct", "10");
		map.put("Nov", "11");
		map.put("Dec", "12");
		String d = s.split("/")[0];
		String m = null;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey().equals(s.split("/")[1])) {
				m = entry.getValue();
			}
		}
		String y = s.split("/")[2].split(":")[0];
		String hh = s.split("/")[2].split(":")[1];
		String mi = s.split("/")[2].split(":")[2];
		String ss = s.split("/")[2].split(":")[3];

		return y + "-" + m + "-" + d + " " + hh + ":" + mi + ":" + ss;
	}
}
