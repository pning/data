package data.bean;

public class WebBean {
	private String ip;
	private String time;
	private String url;
	private String code;
	private String parameter;
	private String useragent;
	private String sitefrom;
	private String miyauuid;
	private String uidInfo;
	private String tourl;
	private String referrer;

	public WebBean() {
		super();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getUseragent() {
		return useragent;
	}

	public void setUseragent(String useragent) {
		this.useragent = useragent;
	}

	public String getSitefrom() {
		return sitefrom;
	}

	public void setSitefrom(String sitefrom) {
		this.sitefrom = sitefrom;
	}

	public String getMiyauuid() {
		return miyauuid;
	}

	public void setMiyauuid(String miyauuid) {
		this.miyauuid = miyauuid;
	}

	public String getUidInfo() {
		return uidInfo;
	}

	public void setUidInfo(String uidInfo) {
		this.uidInfo = uidInfo;
	}

	public String getTourl() {
		return tourl;
	}

	public void setTourl(String tourl) {
		this.tourl = tourl;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	@Override
	public String toString() {
		return "WebBean [ip=" + ip + ", time=" + time + ", url=" + url
				+ ", code=" + code + ", parameter=" + parameter
				+ ", useragent=" + useragent + ", sitefrom=" + sitefrom
				+ ", miyauuid=" + miyauuid + ", uidInfo=" + uidInfo
				+ ", tourl=" + tourl + ", referrer=" + referrer + "]";
	}
}
