package data.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.bean.WebBean;
import data.bean.AppBean;
import net.sf.json.JSONArray;

public class LogSplit {
	// app log split if exception value = ""
	public static AppBean app(String log) {
		AppBean app = new AppBean();
		Pattern p = Pattern
				.compile("^\\[.*\\].*\\|\\|.*\\|\\|.*\\|\\|.*\\|\\|.*\\|\\|.*\\|\\|.*");
		Matcher m = p.matcher(log);
		if (m.matches()) {
			String time = null;
			try {
				time = DateFormat.dateformat(log.split(" ")[0].substring(1));// time
			} catch (Exception e) {
				time = "";
			}
			String shuju = log.substring(log.indexOf("]") + 2);
			String sys_type = null;
			try {
				sys_type = shuju.split("\\|\\|")[0];
			} catch (Exception e) {
				sys_type = "";
			}// sys_type
			String url = null;
			try {
				url = shuju.split("\\|\\|")[1];
			} catch (Exception e) {
				url = "";
			}// url
			String version = null;
			try {
				version = shuju.split("\\|\\|")[5];
			} catch (Exception e) {
				version = "";
			}// version
			String parameter = null;
			try {
				parameter = shuju.split("\\|\\|")[2];
			} catch (Exception e) {
				parameter = "";
			}// parameter
			String channel = null;
			try {
				channel = shuju.split("\\|\\|")[3];
			} catch (Exception e) {
				channel = "";
			}// channel
			String IMEI = null;
			try {
				IMEI = shuju.split("\\|\\|")[4];
			} catch (Exception e) {
				IMEI = "";
			}// IMEI
			String ip = null;
			try {
				ip = shuju.split("\\|\\|")[6];
			} catch (Exception e) {
				ip = "";
			}// ip

			// set app bean
			app.setChannel(channel);
			app.setIMEI(IMEI);
			app.setIp(ip);
			app.setParameter(parameter);
			app.setSys_type(sys_type);
			app.setTime(time);
			app.setUrl(url);
			app.setVersion(version);
			return app;
		} else {
			return null;
		}
	}

	// pc & wap log split if exception value = ""
	@SuppressWarnings("restriction")
	public static WebBean web(String log) throws IOException {
		WebBean app = new WebBean();
		Pattern p = Pattern
				.compile("^(\\d+\\.\\d+\\.\\d+\\.\\d+)\\stj.*.com\\s\\d+.\\d+\\s\\[.*\\]\\s\".*\"\\s200\\s\\d+\\s\"http.*\"\\s\".*\"\\s\".*\"");
		Matcher m = p.matcher(log);
		if (m.matches()) {
			String shuju[] = log.split("\\s\"");
			// ip
			String ip = shuju[0].split("\\s")[0];
			// time
			String time = DateFormat.dateformat(shuju[0].split("\\s")[3]
					.substring(1));
			// url
			String url = URLDecoder.decode(shuju[2].replace("\"", ""), "UTF-8");
			// code
			String code_ = URLDecoder.decode(shuju[3].replace("\"", ""),
					"UTF-8");
			String dcode[] = code_.split(";");
			Map<String, String> map = new HashMap<String, String>();
			for (String kv : dcode) {
				String k = kv.split("=")[0].replace(" ", "");
				String v = null;
				try {
					v = kv.split("=")[1].replace(" ", "");
				} catch (Exception e) {
					v = "";
				}
				map.put(k, v);
			}
			String code = JSONArray.fromObject(map).toString().replace("[", "")
					.replace("]", "");
			// parameter problem url contain &
			String parameter_ = URLDecoder.decode(shuju[1].replace("\"", "")
					.replace("GET /_.gif?", "").split("\\s")[0], "utf-8");
			String p1[] = parameter_.split("&tourl")[0].split("&");
			String p2 = parameter_.split("&tourl")[1].split("&miyaid")[0]
					.substring(1);
			String p3[] = ("miyaid" + parameter_.split("&tourl")[1]
					.split("&miyaid")[1]).split("&");
			Map<String, String> map1 = new HashMap<String, String>();
			for (String kv1 : p1) {
				String k = kv1.split("=")[0];
				String v = null;
				try {
					v = kv1.split("=")[1];
				} catch (Exception e) {
					v = "";
				}
				map1.put(k, v);
			}
			map1.put("tourl", p2);
			for (String kv3 : p3) {
				String k = kv3.split("=")[0];
				String v = null;
				try {
					v = kv3.split("=")[1];
				} catch (Exception e) {
					v = "";
				}
				map1.put(k, v);
			}
			String parameter = JSONArray.fromObject(map1).toString()
					.replace("[", "").replace("]", "");
			// useragent
			String useragent = shuju[4].replace("\"", "");
			// sitefrom
			String sitefrom = map.get("sitefrom");
			if (sitefrom == null) {
				sitefrom = "";
			}
			// miyauuid
			String miyauuid = map.get("miyauuid");
			if (miyauuid == null) {
				miyauuid = "";
			}
			// uidInfo
			String uidInfo = null;
			try {
				uidInfo = new String(
						new sun.misc.BASE64Decoder().decodeBuffer(map
								.get("uidInfo"))).split("-")[1];
			} catch (Exception e) {
				uidInfo = "";
			}
			// tourl
			String tourl = map1.get("tourl");
			// referrer
			String referrer = map1.get("referrer");

			// set app bean
			app.setIp(ip);
			app.setUrl(url);
			app.setTime(time);
			app.setCode(code);
			app.setParameter(parameter);
			app.setMiyauuid(miyauuid);
			app.setUidInfo(uidInfo);
			app.setUseragent(useragent);
			app.setTourl(tourl);
			app.setReferrer(referrer);
			app.setSitefrom(sitefrom);
			return app;
		}
		return null;
	}

	public static void main(String[] args) throws IOException {
		// http://m.mia.com/download_h/2c001256000408003f99552cc97bec38e/?utm_source=bdwap&utm_medium=cpc&utm_campaign=baidu&src=Baidu&medium=PPC&Network=1&kw=19495608879&ad=7039380118&mt=2&ap=mt1&ag_kwid=2906-16-febdb78d040f4920.d7f58bff9ab63cb0_m
		String log = "[28/Jul/2015:00:00:02 +0800] ||/banner/listsExt/||111||222||8A3E0B7F-6F37-41B4-B649-ECD361FFA321||ios_3_4_2||";
		// String log =
		// "125.84.79.222 tj.miyabaobei.com 0.001 [14/Aug/2015:00:00:01 +0800] \"GET /_.gif?version=1.1&browser=other&browser_version=600.1.4&operation_system=mac&flash_version=false&java_enabled=false&language=zh-cn&screen_colors=32&screen_resolution=320*548&referrer=&tourl=http%3A%2F%2Fm.miyabaobei.com%2Fbigpic-1004406.html&title=%E8%9C%9C%E8%8A%BD%E8%A7%A6%E5%B1%8F%E7%89%88&siteform=11111111&miyaid=&cid=&rnd=1715840961 HTTP/1.1\" 200 43 \"http://m.miyabaobei.com/bigpic-1004406.html\" \"_adwb=151852645; _adwc=151852645; _adwp=151852645.7974216298.1439481602.1439481602.1439481602.1; _adwr=151852645%230; uidInfo=NTEyMjI2LTE4NDU4ODE%3D; miyauuid=92c45e50-4178-4867-afd7-946fb6f00bb9\" \"Mozilla/5.0 (iPhone; CPU iPhone OS 8_0 like Mac OSX) AppleWebKit/600.1.4 (KHTML, like Gecko) Mobile/12A365_miyabaobei_io_3.4.3\"";
		if (app(log) != null) {
			System.out.println(app(log).toString());

		}
	}
}
