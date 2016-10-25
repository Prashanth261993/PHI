package com.csc540.phi;

public class ObservationType {
	private int id;
	private String name;
	private String desc;
	private String metric;
	private int sub_type;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return this.getName();
	}
	public void setName() {
		String name = "";
		
		if(this.id == 1)
			name = "Weight";
		else if(this.id == 2 && this.sub_type == 1)
			name = "Systolic BP";
		else if(this.id == 2 && this.sub_type == 2)
			name = "Diastolic BP";
		else if(this.id == 3)
			name = "SpO2";
		else if(this.id == 4)
			name = "Pain";
		else if(this.id == 5)
			name = "Mood";
		else if(this.id == 6)
			name = "Temperature";
		
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
