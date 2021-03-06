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
	public ArrayList<HealthSupporter> health_supporters = new ArrayList<>();
	
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
	
	public Patient displayAndGetPatientInfo(Connection connection, int userID){
		try{
			Statement statement = connection.createStatement();
	        String query = "select p.* from patient p, user_info u where u.id=" + userID + " and u.id = p.user_id";
	        ResultSet result = statement.executeQuery(query);
	        System.out.println("\nPatient Information:");
	        if(result.next()){
	        	this.setId(result.getInt(1));
	        	this.setName(result.getString(2));
	        	this.setDOB(result.getString(3));
	        	this.setAddress(result.getString(4));
	        	this.setGender(result.getString(5));
	        	this.setPhone_num(result.getString(6));
	        	this.setUser_id(userID);
	        	
	        	System.out.println("Name: " + this.name);
	        	System.out.println("DOB: " + this.DOB);
	        	System.out.println("Gender: " + this.gender);
	        	System.out.println("Address: " + this.address);
	        }
		} catch(Exception e){
			e.printStackTrace();
		}
		return this;
	}
	
	public ArrayList<Diagnosis> getPatientDiagnosis(Connection connection, int userID) {
		try{
			Statement statement = connection.createStatement();
	        String query = "select d.id,d.name,d.description from user_info u,patient p,patient_diagnosis pd, diagnosis d where u.id = " + userID + " and p.user_id = u.id and pd.p_id = p.id and pd.d_id = d.id";
	        ResultSet result = statement.executeQuery(query);
	        
	        if(!result.isBeforeFirst()){
	        	System.out.println("\nPatient category: Well");
	        	System.out.println("No diagnosis found.");
	        } else{
	        	System.out.println("\nPatient category: Sick");
	        	System.out.println("List of diagnosis: ");
	        	
	        	while(result.next()){
		        	Diagnosis d = new Diagnosis();
		        	d.setId(result.getInt(1));
		        	d.setName(result.getString(2));
		        	d.setDesc(result.getString(3));
		        	this.diagnosis.add(d);
		        	
		        	System.out.println("Diagnosis Name: " + d.getName() + " , Diagnosis Description: " + d.getDesc());
		        }
	        }	
		} catch(Exception e){
			e.printStackTrace();
		}
		return this.diagnosis;
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
		        	diagnosis.add(d);
		        }
	        }
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	public void setPatientHealthSupporters(Connection connection) {
		try{
			Statement statement = connection.createStatement();
			String query = "select hs.*,hsp.primary_ind,hsp.auth_date from patient p, health_supporter hs, hs_manages_patient hsp where p.id=" + this.getId() + " and hsp.p_id=p.id and hsp.hs_id=hs.id";
			ResultSet result = statement.executeQuery(query);

			
				System.out.println("\nList of authorized health supporters.");
				while(result.next()){
					HealthSupporter h = new HealthSupporter();
					h.setId(result.getInt(1));
					h.setName(result.getString(2));
					h.setAddress(result.getString(3));
					h.setPhone_num(result.getString(4));
					h.setUser_id(result.getInt(5));
					h.setPrimary_ind(result.getInt(6));
					h.setAuth_date(result.getString(7));
					this.health_supporters.add(h);
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
		
	        this.setPatientDiagnosis(connection);
	        System.out.println("Required Observations: \n");
	        if(this.diagnosis.isEmpty()){
	        	System.out.println("\nNo diagnosis exists");
	        	System.out.println("\nFor well patient  ");
	        	
	        	String q = "select o.* from observation_requirement o where o.diagnosis_id = 0";
	        	result = statement.executeQuery(q);
	        	
	        	while(result.next()){
	        		for(ObservationType ot: Main.all_observation_types){
		        		if(ot.getId() == result.getInt(1) && ot.getSubTypeId() == result.getInt(2)){
		        			
		        			String tail_output = "";
		        			if(result.getString(6) != null)
		        				tail_output += "\n Lower limit: "+ result.getString(6);
		        			if(result.getString(7) != null)
		        				tail_output += "\n Upper limit: "+ result.getString(7);
		        			if(result.getBoolean(9))
		        				tail_output += "\n Status: Mandatory";
		        			else
		        				tail_output += "\n Status: Optional";
		        			
		        			System.out.println("\nName:  "+ ot.getName()+ "\nDescription:  " + ot.getDesc()
		        			+ "\nFrequency:  " + result.getInt(10) + " days" + tail_output);
		        		}
		        	}
	        	}
	        }
	        else{
	        	String query = "select o.* from observation_requirement o where o.hs_id is NULL and o.pid is NULL and o.diagnosis_id IN (select d_id from patient_diagnosis where p_id = "+ this.getId() +")";
		        result = statement.executeQuery(query);
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
		        			
		        			System.out.println("\nName:  "+ ot.getName()+ "\nDescription:  " + ot.getDesc()+
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
	public void displayRecommendedObservations(Connection connection){
		try{
			Statement statement;
			ResultSet result;
			statement = connection.createStatement();
			String query = "select o.* from observation_requirement o where o.pid=" + this.getId();
			
			result = statement.executeQuery(query);
			System.out.println("\n\n\nRecommended Observations:");
			while(result.next()){
	        	for(ObservationType ot: Main.all_observation_types){
	        		if(ot.getId() == result.getInt(1) && ot.getSubTypeId() == result.getInt(2)){
	        			String diagnosis_name = "";
	        			String health_supporter_name = "";
	        			for(Diagnosis d: this.diagnosis){
	        				if(d.getId() == result.getInt(5)){
	        					diagnosis_name = d.getName();
	        				}
	        			}
	        			for(HealthSupporter hs:this.health_supporters){
	        				if(hs.getId() == result.getInt(3))
	        					health_supporter_name = hs.getName();
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
	        			
	        			System.out.println("\nName:  "+ ot.getName()+ "\nDescription:  " + ot.getDesc()+
	        					"\nDiagnosis: "+ diagnosis_name + "\nAssigned by:  "+ health_supporter_name + 
	        					"\nRequired from:  " + result.getDate(8).toString() + "\nFrequency:  " 
	        					+ result.getInt(10) + " days" + tail_output);
	        		}
	        	}
	        	
	        }
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
