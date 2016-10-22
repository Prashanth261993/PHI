package com.csc540.phi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class Main {
	
	public static UserInformation user_info = new UserInformation();
	public static ArrayList<Diagnosis> diagnosis = new ArrayList<>();
	public static ArrayList<Diagnosis> pat_diagnosis = new ArrayList<>();
	public static Patient patient = new Patient();
	public static HealthSupporter hs = new HealthSupporter();
	public static ArrayList<HealthSupporter> pat_hs = new ArrayList<>();//List of health supporters for a patient.
	public static ArrayList<Patient> hs_pat = new ArrayList<>();//List of patients associated to a health supporter. 
	public static Scanner sc = new Scanner(System.in);
	
	public static void closeResources(Scanner sc){
		sc.close();
	}
	
	public static void manageLogin(int type, Connection connection){
		Statement statement;
		ResultSet result;
		boolean loginValid = false;
		
		while(loginValid == false){
			try{
				System.out.println("Please enter your login credentials: ");
				System.out.println("Username: ");
				String username = sc.next();
				
				System.out.println("Password: ");
				String password = sc.next();
				
				statement = connection.createStatement();
				result = statement.executeQuery("select * from user_info where username='" + username + "' and password='" + password + "'");
				
				if(!result.next()){
					System.out.println("Username is incorrect. Please try again....");									
				} else{
					loginValid = true;
			        user_info.setId(result.getInt(1));
			        user_info.setUsername(result.getString(2));
			        user_info.setPassword(result.getString(3));
			        user_info.setSecQuestion(result.getString(4));
			        user_info.setSecAnswer(result.getString(5));	
			        System.out.println("User information stored");
				}				
			} catch(Exception e){
				System.out.println("Error encountered. Exitting!!!");
				e.printStackTrace();
				System.exit(0);
			}
		}		
	}
	
	public static void main(String[] args) {
		String query = "";
		Statement statement;
		ResultSet result;
		int option = 2;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC Driver missing!!!");
			e.printStackTrace();
			closeResources(sc);
			return;
		}

		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/phi?useSSL=true", "root", "root");
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			closeResources(sc);
			return;
		}
		
		if (connection != null) {
			System.out.println("Connection with database established!");
		} else {
			System.out.println("Failed to connect to the database!");
		}
		
		try {
			System.out.println("Enter 1 to login as Patient or 2 as Health Supporter: ");
			int type = sc.nextInt();
			
			manageLogin(type, connection);
			
			statement = connection.createStatement();
			query = "select * from diagnosis";
			result = statement.executeQuery(query);
			if(!result.isBeforeFirst()){
				System.out.println("System does not have any diagnosis defined.");
			} else{
				System.out.println("List of system defined diagnosis.");
				while(result.next()){
					Diagnosis d = new Diagnosis();
					d.setId(result.getInt(1));
					d.setName(result.getString(2));
					d.setDesc(result.getString(3));
					diagnosis.add(d);
					System.out.println(d.desc);
				}
			}
			
			if(type == 1){
	        	//Indicates a patient has logged in.
		        statement = connection.createStatement();
		        query = "select p.* from patient p, user_info u where u.id=" + user_info.getId() + " and u.id = p.user_id";
		        result = statement.executeQuery(query);
		        System.out.println("\nPatient details:");
		        if(result.next()){
		        	patient.setId(result.getInt(1));
		        	patient.setName(result.getString(2));
		        	patient.setDOB(result.getString(3));
		        	patient.setAddress(result.getString(4));
		        	patient.setGender(result.getString(5));
		        	patient.setPhone_num(result.getString(6));
		        	
		        	System.out.println("Name: " + patient.name);
		        	System.out.println("DOB: " + patient.DOB);
		        	System.out.println("Gender: " + patient.gender);
		        	System.out.println("Address: " + patient.address);
		        }
		        
		        statement = connection.createStatement();
		        query = "select d.id,d.name,d.description from user_info u,patient p,patient_diagnosis pd, diagnosis d where u.id = " + user_info.getId() + " and p.user_id = u.id and pd.p_id = p.id and pd.d_id = d.id";
		        result = statement.executeQuery(query);
		        
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
			        	pat_diagnosis.add(d);
			        	
			        	System.out.println(d.getDesc());
			        }
		        }

		        statement = connection.createStatement();
		        query = "select hs.*,hsp.primary_ind,hsp.auth_date from patient p, health_supporter hs, hs_manages_patient hsp where p.id=" + patient.getId() + " and hsp.p_id=p.id and hsp.hs_id=hs.id";
		        result = statement.executeQuery(query);
		        
		        if(!result.isBeforeFirst()){
		        	System.out.println("\nNo authorized health supporters found!");
		        } else{
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
			        	pat_hs.add(h);
			        	if(h.primary_ind != 0){
			        		System.out.println("Primary HS: " + h.name);
			        	} else {
			        		System.out.println("Secondary HS: " + h.name);
			        	}
			        }
		        }
		        
		        while(option < 6){
		        	System.out.println("\n1.Edit personal Information\n2.Add diagnosis\n3.Add observation\n4.View alerts\n5..Remove authorized health supporter\n6.Add authorized health supporter\nEnter your choice: ");
		        	option = sc.nextInt();
		        	sc.nextLine();
		        	if(option == 1){
		        		System.out.println("Enter new address: ");
		        		String address = sc.nextLine();
		        		System.out.println("Enter new name: ");
		        		String name = sc.nextLine();
		        		System.out.println("Enter new phone number: ");
		        		String phone = sc.nextLine();
		        		statement = connection.createStatement();
		        		query = "update patient p set p.name='" + name + "', p.address='" + address + "', p.phone_num='" + phone + "' where p.id=" + patient.id + "";
		        		System.out.println(query);
		        		int rows = statement.executeUpdate(query);
		        		if(rows > 0){
		        			System.out.println("Update successful");
		        		} else{
		        			System.out.println("Update unsuccessful");
		        		}
		        		//Update corresponding entry in Health Supporter table as well.
		        		statement = connection.createStatement();
		        		query = "update patient p,health_supporter hs,user_info u set hs.name='" + name + "', hs.address='" + address + "', hs.phone_num='" + phone + "' where p.id=" + patient.id + " and p.user_id=u.id and hs.user_id=u.id";
		        		System.out.println(query);
		        		rows = statement.executeUpdate(query);
		        		if(rows > 0){
		        			System.out.println("Update successful");
		        		} else{
		        			System.out.println("Update unsuccessful");
		        		}
		        	} else if(option == 2){
		        		addDiagnosis(connection, patient.id);
		        	} else if(option == 3){
		        		addPatientObservation();
		        	} else if(option == 4){
		        		viewPatientAlerts();
		        	} else if(option == 5){
		        		if(pat_hs.size() == 0){
		        			System.out.println("You have no authorized health supporters to remove.");
		        		} else{
		        			System.out.println("Enter authorized health supporter to remove");
		        			String hsp = sc.nextLine();
		        			boolean found = false;
		        			int hs_id = 0;
		        			for(HealthSupporter h: pat_hs){
		        				if(h.name == hsp){
		        					found = true;
		        					hs_id = h.id;
		        					break;
		        				}
		        			}
		        			if(!found){
		        				System.out.println("No such health supporter found.");
		        			} else{
		        				statement = connection.createStatement();
		        				query = "delete from hs_manages_patient hsp where hsp.p_id=" + patient.id + " and hsp.hs_id=" + hs_id + "";
		        				int rows = statement.executeUpdate(query);
		        				if(rows > 0){
		        					System.out.println("Update successful");
		        				} else {
		        					System.out.println("Update unsuccessful");
		        				}
		        			}
		        		}
		        	}		        
		        }		        
		    } else if(type == 2){
	        	//Indicates a health supporter has logged in.
		    	result.close();
	        	Statement stmt = connection.createStatement();
		        query = "select hs.* from health_supporter hs, user_info u where u.id=" + user_info.getId() + " and u.id = hs.user_id";
		        result = stmt.executeQuery(query);
		        if(result.next()){
		        	hs.setId(result.getInt(1));
		        	hs.setName(result.getString(2));
		        	hs.setAddress(result.getString(3));
		        	hs.setPhone_num(result.getString(4));
		        	hs.setUser_id(result.getInt(5));
		        	System.out.println("H_S Name: " + hs.name);
		        	System.out.println("H_S Address: " + hs.address);
		        	System.out.println("H_S Phone: " + hs.phone_num);
		        }
		        
		        stmt = connection.createStatement();
		        query = "select p.* from hs_manages_patient hsp, patient p, health_supporter hs where hs.id=" + hs.getId() + " and hsp.hs_id = hs.id and hsp.p_id=p.id";
		        result = stmt.executeQuery(query);
		        
		        if(!result.isBeforeFirst()){
		        	System.out.println("\nNo authorized patients found!!!");
		        }else{
		        	System.out.println("\nList of patients associated: ");
		        	while(result.next()){
			        	Patient p = new Patient();
			        	p.setId(result.getInt(1));
			        	p.setName(result.getString(2));
			        	p.setDOB(result.getString(3));
			        	p.setAddress(result.getString(4));
			        	p.setGender(result.getString(5));
			        	p.setPhone_num(result.getString(6));
			        	hs_pat.add(p);
			        	System.out.println(p.name);
			        }
		        }
		        
		        while(option < 6){
		        	System.out.println("What do you want to do?\n1.Edit personal Information\n2.Add patient diagnosis\n3.Edit patient information\n4.Add patient observation\n5.View alerts");
		        	option = sc.nextInt();
		        	sc.nextLine();
		        	if(option == 1){
		        		System.out.println("Enter new address: ");
		        		String address = sc.nextLine();
		        		System.out.println("Enter new name: ");
		        		String name = sc.nextLine();
		        		System.out.println("Enter new phone number: ");
		        		String phone = sc.nextLine();
		        		statement = connection.createStatement();
		        		query = "update health_supporter hs set hs.name='" + name + "', hs.address='" + address + "', hs.phone_num='" + phone + "' where hs.id=" + hs.id + "";
		        		System.out.println(query);
		        		int rows = statement.executeUpdate(query);
		        		if(rows > 0){
		        			System.out.println("Update successful");
		        		} else{
		        			System.out.println("Update unsuccessful");
		        		}
		        		//Update corresponding entry in Health Supporter table as well.
		        		statement = connection.createStatement();
		        		query = "update patient p,health_supporter hs,user_info u set p.name='" + name + "', p.address='" + address + "', p.phone_num='" + phone + "' where hs.id=" + hs.id + " and hs.user_id=u.id and p.user_id=u.id";
		        		System.out.println(query);
		        		rows = statement.executeUpdate(query);
		        		if(rows > 0){
		        			System.out.println("Update successful");
		        		} else{
		        			System.out.println("Update unsuccessful");
		        		}
		        	} else if(option == 2){
		        		int patID = 0;
		        		while(patID == 0){
			        		System.out.println("Enter the authorized patient name you want to modify: ");
			        		String name = sc.nextLine();
			        		for(Patient p : hs_pat){
			        			if(p.name.compareTo(name) == 0){
			        				patID = p.id;
			        				break;
			        			}
			        		}
			        		if(patID == 0){
			        			System.out.println("No such patients found. Please try again!!!");
			        		} else{
			        			break;
			        		}
		        		}
		        		addDiagnosis(connection,patID);
		        	} else if(option == 3){
		        		if(hs_pat.size() == 0){
		        			System.out.println("You have no authorized patients.");
		        		} else{
		        			int patID = 0;
			        		while(patID == 0){
				        		System.out.println("Enter the authorized patient name you want to modify: ");
				        		String name = sc.nextLine();
				        		for(Patient p : hs_pat){
				        			if(p.name.compareTo(name) == 0){
				        				patID = p.id;
				        				break;
				        			}
				        		}
				        		if(patID == 0){
				        			System.out.println("No such authorized patients found. Please try again!!!");
				        		} else{
				        			break;
				        		}
			        		}
			        		System.out.println("Enter new address: ");
			        		String address = sc.nextLine();
			        		System.out.println("Enter new name: ");
			        		String name = sc.nextLine();
			        		System.out.println("Enter new phone number: ");
			        		String phone = sc.nextLine();
			        		System.out.println("Enter new DOB: ");
			        		String dob = sc.next();
			        		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			        		java.sql.Date bday = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
			        		try {
								bday.setTime(fmt.parse(dob).getTime());
							} catch (ParseException e) {
								e.printStackTrace();
							}
			        		statement = connection.createStatement();
			        		query = "update patient p set p.dob = " + bday + " and p.name='" + name + "', p.address='" + address + "', p.phone_num='" + phone + "' where p.id=" + patID + "";
			        		System.out.println(query);
			        		int rows = statement.executeUpdate(query);
			        		if(rows > 0){
			        			System.out.println("Update successful");
			        		} else{
			        			System.out.println("Update unsuccessful");
			        		}
		        		}		        		
		        	} else if(option == 4){
		        		addPatientObservation();
		        	} else if(option == 5){
		        		viewHSAlerts();
		        	}
		        }
		        connection.close();		    
	        } else {
	        	System.out.println("Invalid choice. Exitting!!!");
	        	System.exit(0);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeResources(sc);
	}

	private static void viewPatientAlerts() {
		
		
	}

	private static void viewHSAlerts() {
		
		
	}

	private static void addPatientObservation() {
		
		
	}

	private static void addDiagnosis(Connection c,int patId) {
		try {
			Statement stmt = c.createStatement();
			int diagID = 0;
			System.out.println("Enter diagnosis name: ");
			String name = sc.nextLine();
			for(Diagnosis d : diagnosis){
				if(name.compareToIgnoreCase(d.name) == 0){
					diagID = d.id;
					break;
				}
			}
			
			String query = "insert into patient_diagnosis values(" + patId + ", " + diagID + ", '" + new java.sql.Date(Calendar.getInstance().getTimeInMillis()) + "')";
			System.out.println(query);
			int rows = stmt.executeUpdate(query);
			if(rows > 0){
				System.out.println("New diagnosis added.");
			} else{
				System.out.println("Error adding diagnosis.");
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}


