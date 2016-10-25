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
	public ArrayList<Diagnosis> diagnosis = new ArrayList<>();
	
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
			
	        String query = "select o.* from observation_requirement o where o.hs_id is NULL and o.pid is NULL and o.diagnosis_id IN (select d_id from patient_diagnosis where p_id = "+ this.getId() +")";
	        result = statement.executeQuery(query);
	        
	        this.setPatientDiagnosis(connection);
	        System.out.println("Required Observations: \n");
	        if(this.diagnosis.isEmpty()){
	        	System.out.println("No diagnosis exists");
	        }
	        else{
		        while(result.next()){
		        	for(ObservationType ot: Main.all_observation_types){
		        		if(ot.getId() == result.getInt(1) && ot.getSubTypeId() == result.getInt(2)){
		        			String diagnosis_name = "";
		        			for(Diagnosis d: this.diagnosis){
		        				if(d.getId() == result.getInt(5)){
		        					diagnosis_name = d.getName();
		        				}
		        			}
		        			String tail_output = "";
		        			if(result.getString(6) != null)
		        				tail_output += "\n Lower limit: "+ result.getString(6);
		        			if(result.getString(7) != null)
		        				tail_output += "\n Upper limit: "+ result.getString(7);
		        			if(result.getBoolean(9))
		        				tail_output += "\n Status: Mandatory";
		        			else
		        				tail_output += "\n Status: Optional";
		        			
		        			System.out.println("Name:  "+ ot.getName()+ "\nDescription:  " + ot.getDesc()+
		        					"\nDiagnosis: "+ diagnosis_name + "\nFrequency:  " + result.getInt(10) + 
		        					" days" + tail_output);
		        		}
		        	}
		        	
		        }
	        }
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void displayRecommendedObservations(Connection connection){
		
	}
}
