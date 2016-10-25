package com.csc540.phi;

public class ObservationType {
	private int id;
	private String name;
	private String desc;
	private String metric;
	private int sub_type_id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSubTypeId() {
		return sub_type_id;
	}
	public void setSubTypeId(int id) {
		this.sub_type_id = id;
	}
	public String getName() {
		return this.name;
	}
	public void setName( String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getMetric() {
		return metric;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
}
