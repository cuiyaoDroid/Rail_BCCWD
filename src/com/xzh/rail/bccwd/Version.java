package com.xzh.rail.bccwd;

public class Version {
	private int id;
	private String updateTime;
	private String version;
	private int forceUpdate;
	private String name;
	private String url;
	private int type;
	private String lowestVersion;
	private int productId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getForceUpdate() {
		return forceUpdate;
	}
	public void setForceUpdate(int forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getLowestVersion() {
		return lowestVersion;
	}
	public void setLowestVersion(String lowestVersion) {
		this.lowestVersion = lowestVersion;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
}
