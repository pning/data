package data.bean;

public class AppBean {
	private String time;
	private String sys_type;
	private String url;
	private String version;
	private String parameter;
	private String channel;
	private String IMEI;
	private String ip;

	public AppBean() {
		super();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSys_type() {
		return sys_type;
	}

	public void setSys_type(String sys_type) {
		this.sys_type = sys_type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return "AppBean [time=" + time + ", sys_type=" + sys_type + ", url="
				+ url + ", version=" + version + ", parameter=" + parameter
				+ ", channel=" + channel + ", IMEI=" + IMEI + ", ip=" + ip
				+ "]";
	}
}
