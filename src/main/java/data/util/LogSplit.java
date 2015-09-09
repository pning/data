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
		Pattern p = Pattern.compile("^\\[.*\\].*\\|\\|.*\\|\\|.*\\|\\|.*\\|\\|.*\\|\\|.*\\|\\|.*");
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
			} // sys_type
			String url = null;
			try {
				url = shuju.split("\\|\\|")[1];
			} catch (Exception e) {
				url = "";
			} // url
			String version = null;
			try {
				version = shuju.split("\\|\\|")[5];
			} catch (Exception e) {
				version = "";
			} // version
			String parameter = null;
			try {
				parameter = shuju.split("\\|\\|")[2];
			} catch (Exception e) {
				parameter = "";
			} // parameter
			String channel = null;
			try {
				channel = shuju.split("\\|\\|")[3];
			} catch (Exception e) {
				channel = "";
			} // channel
			String IMEI = null;
			try {
				IMEI = shuju.split("\\|\\|")[4];
			} catch (Exception e) {
				IMEI = "";
			} // IMEI
			String ip = null;
			try {
				ip = shuju.split("\\|\\|")[6];
			} catch (Exception e) {
				ip = "";
			} // ip

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
		WebBean web = new WebBean();
		Pattern p = Pattern.compile(
				"^(\\d+\\.\\d+\\.\\d+\\.\\d+)\\stj.*.com\\s\\d+.\\d+\\s\\[.*\\]\\s\".*\"\\s200\\s\\d+\\s\"http.*\"\\s\".*\"\\s\".*\"");
		Matcher m = p.matcher(log);
		if (m.matches()) {
			String shuju[] = log.split("\\s\"");
			// ip
			String ip = shuju[0].split("\\s")[0];
			// time
			String time = DateFormat.dateformat(shuju[0].split("\\s")[3].substring(1));
			// url
			String url = URLDecoder.decode(shuju[2].replace("\"", ""), "UTF-8");
			// code
			String code_ = URLDecoder.decode(shuju[3].replace("\"", ""), "UTF-8");
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
			String code = JSONArray.fromObject(map).toString().replace("[", "").replace("]", "");
			// parameter problem url contain &
			// b_c.gif || _.gif two format
			String parameter = "";
			String tourl = "";
			String referrer = "";
			String type = "";
			if (shuju[1].contains("GET /_.gif?")) {
				String parameter_ = URLDecoder
						.decode(shuju[1].replace("\"", "").replace("GET /_.gif?", "").split("\\s")[0], "utf-8");
				// referrer tourl value
				String t1[] = parameter_.split("&referrer")[0].split("&");
				String r2 = parameter_.split("&referrer")[1].split("&tourl")[0].substring(1);
				String t2 = parameter_.split("&tourl")[1].split("&miyaid")[0].substring(1);
				String t3[] = ("miyaid" + parameter_.split("&tourl")[1].split("&miyaid")[1]).split("&");
				Map<String, String> map1 = new HashMap<String, String>();
				for (String kv1 : t1) {
					String k = kv1.split("=")[0];
					String v = null;
					try {
						v = kv1.split("=")[1];
					} catch (Exception e) {
						v = "";
					}
					map1.put(k, v);
				}
				map1.put("tourl", t2);
				map1.put("referrer", r2);
				// not contain tourl & referrer value
				for (String kv3 : t3) {
					String k = kv3.split("=")[0];
					String v = null;
					try {
						v = kv3.split("=")[1];
					} catch (Exception e) {
						v = "";
					}
					map1.put(k, v);
				}
				parameter = JSONArray.fromObject(map1).toString().replace("[", "").replace("]", "");
				// tourl
				tourl = map1.get("tourl");
				// referrer
				referrer = map1.get("referrer");
				// type
				type = "_.gif";
			} else if (shuju[1].contains("GET /b_c.gif?")) {
				String parameter_ = URLDecoder
						.decode(shuju[1].replace("\"", "").replace("GET /b_c.gif?", "").split("\\s")[0], "utf-8");
				// tourl(source_url) referrer(target_url) value
				String t1[] = parameter_.split("&source_url")[0].split("&");
				String t2 = parameter_.split("&source_url")[1].split("&target_url")[0].substring(1);
				String t3 = parameter_.split("&source_url")[1].split("&target_url")[1].substring(1);
				System.out.println(t3);
				Map<String, String> map1 = new HashMap<String, String>();
				for (String kv1 : t1) {
					String k = kv1.split("=")[0];
					String v = null;
					try {
						v = kv1.split("=")[1];
					} catch (Exception e) {
						v = "";
					}
					map1.put(k, v);
				}
				map1.put("source_url", t2);
				map1.put("target_url", t3);
				parameter = JSONArray.fromObject(map1).toString().replace("[", "").replace("]", "");
				// tourl (source_url)
				tourl = map1.get("source_url");
				// referrer (target_url)
				referrer = map1.get("target_url");
				// type
				type = "b_c.gif";
			}
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
				uidInfo = new String(new sun.misc.BASE64Decoder().decodeBuffer(map.get("uidInfo"))).split("-")[1];
			} catch (Exception e) {
				uidInfo = "";
			}
			// set web bean
			web.setIp(ip);
			web.setUrl(url);
			web.setTime(time);
			web.setCode(code);
			web.setParameter(parameter);
			web.setMiyauuid(miyauuid);
			web.setUidInfo(uidInfo);
			web.setUseragent(useragent);
			web.setTourl(tourl);
			web.setReferrer(referrer);
			web.setSitefrom(sitefrom);
			web.setType(type);
			return web;
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		// http://m.mia.com/download_h/2c001256000408003f99552cc97bec38e/?utm_source=bdwap&utm_medium=cpc&utm_campaign=baidu&src=Baidu&medium=PPC&Network=1&kw=19495608879&ad=7039380118&mt=2&ap=mt1&ag_kwid=2906-16-febdb78d040f4920.d7f58bff9ab63cb0_m
		//String log1 = "125.84.79.222 tj.mia.com 0.001 [14/Aug/2015:00:00:01 +0800] \"GET /_.gif?version=1.1&browser=other&browser_version=600.1.4&operation_system=mac&flash_version=false&java_enabled=false&language=zh-cn&screen_colors=32&screen_resolution=320*548&referrer=&tourl=&miyaid=&cid=&rnd=1715840961 HTTP/1.1\" 200 43 \"http://m.miyabaobei.com/bigpic-1004406.html\" \"_adwb=151852645; _adwc=151852645; _adwp=151852645.7974216298.1439481602.1439481602.1439481602.1; _adwr=151852645%230; uidInfo=NTEyMjI2LTE4NDU4ODE%3D; miyauuid=92c45e50-4178-4867-afd7-946fb6f00bb9\" \"Mozilla/5.0 (iPhone; CPU iPhone OS 8_0 like Mac OSX) AppleWebKit/600.1.4 (KHTML, like Gecko) Mobile/12A365_miyabaobei_io_3.4.3\"";
		String log2 = "183.58.204.132 tj.mia.com 0.000 [08/Sep/2015:16:57:50 +0800] \"GET /_.gif?version=1.1&browser=other&browser_version=600.1.4&operation_system=mac&flash_version=fase&java_enabled=false&language=zh-cn&screen_colors=32&screen_resolution=1024*748&referrer=http%3A%2F%2Fm.mia.com%2Fdownload_h%2F2c00235500048c00401d55c9ed401d0eb&title=%E8%9C%9C%E8%8A%BD%E8%A7%A6%E5%B1%8F%E7%89%88&tese=1111111111111&tourl=http%3A%2F%2Fm.mia.com%2Fdownload_h%2F2c00235500048c00401d55c9ed401d0eb&title=%E8%9C%9C%E8%8A%BD%E8%A7%A6%E5%B1%8F%E7%89%88&miyaid=vqb7pmneueck64dk35m3iejan4&cid=&rnd=1190970122 HTTP/1.1\" 200 43 \"http://m.mia.com/download_h/2c00235500048c00401d55c9ed401d0eb\" \"miyauuid=51c6f1b7-bea8-4d55-99c6-28b3f5b02980; sitefrom=2c00235500048c00401d55c9ed401d0eb\" \"Mozilla/5.0 (iPad; CPU OS 8_1_3 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Mobile/12B466\"";
		//String log3 = "221.198.248.214 tj.mia.com 0.000 [08/Sep/2015:16:58:31 +0800] \"GET /b_c.gif?miyaid=pq6o7i96loocp8qqtjtsu18kj5&cid=9999&source_url=http%3A%2F%2Fm.mia.com%2F%3Futm_source%3Dfanli%26utm_medium%3Dcps%26utm_campaign%3Dcps%26wu_cid%3D9999%26wu_sorce%3D51fanli%26wu_channel%3Dcps%26wu_wi%3D127101%7C88524235&target_url=http%3A%2F%2Fm.mia.com%2Fhome_promise.html&atitle=%3Cimg%20src%3D%22http%3A%2F%2Fmfile01.miyabaobei.com%2Fresources%2Fimages%2Fm%2Fdomain%2FpromiseImg.jpg%22%20width%3D%22100%25%22%20class%3D%22promiseImg%22%3E&x=133&y=200 HTTP/1.1\" 200 43 \"http://m.mia.com/?utm_source=fanli&utm_medium=cps&utm_campaign=cps&wu_cid=9999&wu_source=51fanli&wu_channel=cps&wu_wi=127101|88524235\" \"miyauuid=7a366c5d-0499-4469-bd2d-c65b3f0c8497; __ag_cm_=1441449784832; pgv_pvi=2786016256; pgv_si=s2934777856; fanli_channel_id=51fanli; fanli_u_id=88524235; fanli_tracking_code=127101; uidInfo=NjA2MTg0LTM5NTgyODQ%3D; username=MjI2Njg2LXM6MTE6IjE4NzAyMjAxNzU3Ijs%3D; usernames=18702201757; sitefrom=1c00220000034200131054f19455ceb0a; _gat=1; web_union_source=51fanli; web_union_channel=cps; web_union_cid=9999; web_union_wi=127101|88524235; _jzqco=%7C%7C%7C%7C%7C1.1087632459.1441449784515.1441702684614.1441702694134.1441702684614.1441702694134.0.0.0.21.21; _ga=GA1.2.546344058.1441449785; ag_fid=QhZmsLIH4dk5qwwF; bfd_s=108528798.51782021.1441702678600; tmc=3.108528798.15024107.1441702678629.1441702685679.1441702694676; tma=108528798.40373480.1441449785069.1441628112995.1441702678665.3; tmd=21.108528798.40373480.1441449785069.; bfd_g=b56c782bcb75035d0000509a007a5ee355eac734\" \"Mozilla/5.0 (Linux; Android 5.0; SM-N9008 Build/LRX21V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.121 Mobile Safari/537.36\"";
		System.out.println(log2);
		if (web(RSAUtil.getLog(log2)) != null) {
			System.out.println(web(RSAUtil.getLog(log2)).toString());

		}
	}
}
