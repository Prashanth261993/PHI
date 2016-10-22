package com.csc540.phi;

public class HealthSupporter {
	
	public int id;
	public String name;
	public String address;
	public String phone_num;
	public int user_id;
	public int primary_ind;
	public String auth_date;
	
	public int getPrimary_ind() {
		return primary_ind;
	}
	public void setPrimary_ind(int primary_ind) {
		this.primary_ind = primary_ind;
	}
	public String getAuth_date() {
		return auth_date;
	}
	public void setAuth_date(String auth_date) {
		this.auth_date = auth_date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone_num() {
		return phone_num;
	}
	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}	
}
