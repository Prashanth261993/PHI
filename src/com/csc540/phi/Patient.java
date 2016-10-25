package com.csc540.phi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Patient {
	
	public int id;
	public String name;
	public String DOB;
	public String address;
	public String gender;
	public String phone_num;
	public int user_id;
	public ArrayList<Diagnosis> diagnosis;
	
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
	public String getDOB() {
		return DOB;
	}
	public void setDOB(String dOB) {
		DOB = dOB;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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
	public void setPatientDiagnosis(Connection connection) {
		try{
			Statement statement = connection.createStatement();
	        String query = "select d.id,d.name,d.description from patient p,patient_diagnosis pd, diagnosis d where  pd.p_id =" + this.getId() +" and pd.d_id = d.id";
	        ResultSet result = statement.executeQuery(query);
	        
	        if(result.isBeforeFirst()){
	        	while(result.next()){
		        	Diagnosis d = new Diagnosis();
		        	d.setId(result.getInt(1));
		        	d.setName(result.getString(2));
		        	d.setDesc(result.getString(3));
		        	this.diagnosis.add(d);
		        }
	        }
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public void displayRequiredObservations(Connection connection){
		try{
			Statement statement;
			ResultSet result;
			statement = connection.createStatement();
			ArrayList<ObservationType> patient_observations = new ArrayList<ObservationType>();
			ArrayList<Integer> observation_type_ids = new ArrayList<Integer>();  
			ArrayList<Integer> observation_sub_type_ids = new ArrayList<Integer>();  
			
	        String query = "select or.* from observation_requirement or, patient_diagnosis pd where pd.p_id =" + this.getId()+ "and or.diagnosis_id = pd.d_id";
	        result = statement.executeQuery(query);
	        
	        this.setPatientDiagnosis(connection);
	        System.out.println("Required Observations: ");
	        if(this.diagnosis.isEmpty()){
	        	System.out.println("No diagnosis exists");
	        }
	        else{
		        while(result.next()){
		        	observation_type_ids.add(result.getInt(1));
		        	observation_sub_type_ids.add(result.getInt(1));
		        	String sql = "select ot.* from observation_type ot where ot.observation_type_id IN [" + String.join(",", observation_type_ids.toArray(new String[1]))+ "]";
		        	statement.executeQuery(sql);
		        }
		        for(ObservationType o:patient_observations){
	        		
	        	}
	        }
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void displayRecommendedObservations(Connection connection){
		
	}
}
