package com.csc540.phi;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Main {

	public static Patient patient = new Patient();
	public static HealthSupporter hs = new HealthSupporter();
	public static UserInformation user_info = new UserInformation();//Stores the logged in user's information.
	public static ArrayList<Diagnosis> diagnosis = new ArrayList<>();//Stores the list of diagnosis defined in the system.
	public static ArrayList<Diagnosis> pat_diagnosis = new ArrayList<>();//Stores the list of diagnosis a patient is diagnosed with.
	public static ArrayList<HealthSupporter> pat_hs;//List of health supporters for a patient.
	public static ArrayList<Patient> hs_pat = new ArrayList<>();//List of patients associated to a health supporter.
	public static ArrayList<ObservationType> all_observation_types = new ArrayList<>();
	public static Scanner sc = new Scanner(System.in);
	public static Connection conn;

	public static void main(String[] args) {
		Connection connection = null;
		Statement statement;
		ResultSet result;
		String query = "";
		int option = 0;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL JDBC Driver missing!!!");
			e.printStackTrace();
			closeResources(sc);
			return;
		}

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/phi?useSSL=true", "root", "root");
			conn = connection;
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

		int type;
		try {
			System.out.println("****************Welcome to PHI application***************");
			//So lag as user does not select correct entry keep asking him choose correct option.
			do{
				System.out.println("\n1 - Login as Patient\n2 - Login as Health Supporter \n3 - Sign Up as new user\nEnter your choice: ");
				type = sc.nextInt();
			} while(type < 1 && type > 3);
			
			
			if(type == 3){
				manageSignUp(connection);				
			}
			//Once sign up is done, ask user to login as patient or HS.
			if(type == 3){
				do{
					System.out.println("\n1 - Login as Patient\n2 - Login as Health Supporter\nEnter your choice: ");
					type = sc.nextInt();
				}while(type < 1 && type > 2);				
			}
			manageLogin(type, connection);
			
			displaySystemDiagnosis(connection);
			
			if(type == 1){
	        	//Indicates a patient has logged in.
		        patient = patient.displayAndGetPatientInfo(connection,user_info.id);
		        
		        pat_diagnosis = patient.getPatientDiagnosis(connection,user_info.id);

				updatePatientHealthSupporters(connection, patient.id);
		        loadObservationType(connection);
		        patient.setPatientHealthSupporters(connection);
		        
		        while(option <= 9){
		        	System.out.println("\n1.Edit personal Information\n2.Add diagnosis\n3.Add observation\n4.View alerts\n5.Remove authorized health supporter\n6.Add authorized health supporter\n7.Remove diagnosis\n8.View Health Indicators\n9.View Observations\n10.Exit\nEnter your choice: ");
		        	option = sc.nextInt();
		        	sc.nextLine();
		        	if(option == 1){
		        		int chosen_menu;
		        		System.out.println("\nChoose the option from menu you would like to update.\n1.Name\n2.Address\n3.Phone Number\nEnter your choice: ");
		        		chosen_menu = sc.nextInt();
		        		sc.nextLine();
		        		switch(chosen_menu){
			        		case 1:
				        		System.out.println("Enter new name: ");
				        		String name = sc.nextLine();
				        		patient.setName(name);
				        		break;
			        		case 2:
			        			System.out.println("Enter new address: ");
				        		String address = sc.nextLine();
				        		patient.setAddress(address);
				        		break;
			        		case 3:
				        		System.out.println("Enter new phone number: ");
				        		String phone = sc.nextLine();
				        		patient.setPhone_num(phone);
				        		break;
			        		default:
				        		System.out.println("Invalid Choice");
		        		}
		        		
		        		statement = connection.createStatement();
		        		query = "update patient p set p.name='" + patient.getName() + "', p.address='" + patient.getAddress() + "', p.phone_num='" + patient.getPhone_num()+ "' where p.id=" + patient.getId() + "";
		        		System.out.println(query);
		        		int rows = statement.executeUpdate(query);
		        		if(rows > 0){
		        			System.out.println("Update successful");
		        		} else{
		        			System.out.println("Update unsuccessful");
		        		}
		        		//Update corresponding entry in Health Supporter table as well.
		        		statement = connection.createStatement();
		        		query = "update patient p,health_supporter hs,user_info u set hs.name='" + patient.getName() + "', hs.address='" + patient.getAddress() + "', hs.phone_num='" + patient.getPhone_num() + "' where p.id=" + patient.getId() + " and p.user_id=u.id and hs.user_id=u.id";
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
		        		addPatientObservation(connection,patient.getId());
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
		        				if(hsp.compareToIgnoreCase(h.name) == 0){
		        					found = true;
		        					hs_id = h.id;
		        					pat_hs.remove(h);
		        					break;
		        				}
		        			}
		        			if(!found){
		        				System.out.println("No such health supporter found.");
		        			} else{
		        				try{
		        					statement = connection.createStatement();
			        				query = "delete from hs_manages_patient where p_id=" + patient.id + " and hs_id=" + hs_id + "";
			        				System.out.println(query);
			        				int rows = statement.executeUpdate(query);
			        				if(rows > 0){
			        					System.out.println("Health Supporter removed successfully");
			        				} else {
			        					System.out.println("Removal failed.");
			        				}
		        				}catch(Exception e){
		        					System.err.println(e.getMessage());
		        				}
		        			}
		        		}
		        		updatePatientHealthSupporters(connection, patient.id);
		        	} else if(option == 6){
	        			if(pat_hs.size() == 2){
	        				System.out.println("You already have maximum number of health supporters");	        				
	        			} else{
	        				String name = "";
	        				do{
	        					System.out.println("Enter name of health supporter to add: ");
		        				name = sc.nextLine();
	        				}while(pat_hs.size() == 1 && pat_hs.get(0).name.compareTo(name)==0);        				
	        				System.out.println("Enter primary indicator: ");
	        				int primary_ind = Integer.parseInt(sc.nextLine());
	        				System.out.println("Enter relation to patient: ");
	        				String rel = sc.nextLine();
	        				if(pat_hs.size() == 1){
	        					//If there is already a primary health supporter, then overwrite it to make the new one primary.
	        					if(primary_ind == 1 && pat_hs.get(0).primary_ind == 1){
	        						pat_hs.get(0).primary_ind = 0;
	        						int hs_id = 0;
	        						statement = connection.createStatement();
	        						String subquery = "select h.id from health_supporter h where h.name='" + name + "'";
	        						System.out.println(subquery);
	        						result = statement.executeQuery(subquery);
	        						if(!result.next()){
	        							System.out.println("No such health supporter found.");
	        						}else{
	        							hs_id = result.getInt(1);
	        							
	        							query = "insert into hs_manages_patient values(" + patient.id + ", " + hs_id + ", " + primary_ind + ", '" + rel + "', '" + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTimeInMillis()) + "')";
		        						
	        							statement = connection.createStatement();
		        						System.out.println(query);
		        						int rows = statement.executeUpdate(query);
		        						if(rows > 0){
		        							System.out.println("Inserted successfully");
		        						}else{
		        							System.out.println("Insertion failed.");
		        						}
		        						query = "update hs_manages_patient hs set hs.primary_ind = 0 where hs.p_id = " + patient.id + " and hs.hs_id = " + pat_hs.get(0).id + "";
		        						statement = connection.createStatement();
		        						rows = statement.executeUpdate(query);
		        						if(rows > 0){
		        							System.out.println("Updated successfully");
		        						} else{
		        							System.out.println("Updation unsuccessful");
		        						}
	        						}	        						
	        					}else{
	        						statement = connection.createStatement();
	        						String subquery = "select h.id from health_supporter h where h.name='" + name + "'";
	        						result = statement.executeQuery(subquery);
	        						if(!result.next()){
	        							System.out.println("No such health supporter found.");
	        						}else{
	        							int hs_id = result.getInt(1);
	        							//This prevents two health supporters being inserted without each being a primary one.
	        							if(pat_hs.get(0).primary_ind == 0 && primary_ind == 0){
	        								primary_ind = 1;
	        							}
	        							query = "insert into hs_manages_patient values(" + patient.id + ", " + hs_id + ", " + primary_ind + ", '" + rel + "', '" + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTimeInMillis()) + "')";
		        						System.out.println(query);
		        						statement = connection.createStatement();
		        						int rows = statement.executeUpdate(query);
		        						if(rows > 0){
		        							System.out.println("Inserted successfully");
		        						}else{
		        							System.out.println("Insertion failed.");
		        						}
	        						}	        						
	        					}
	        				}else if(pat_hs.size() == 0){
	        					statement = connection.createStatement();
        						String subquery = "select h.id from health_supporter h where h.name='" + name + "'";
        						result = statement.executeQuery(subquery);
        						if(!result.next()){
        							System.out.println("No such health supporter found.");
        						}else{
        							int hs_id = result.getInt(1);
        							query = "insert into hs_manages_patient values(" + patient.id + ", " + hs_id + ", " + primary_ind + ", '" + rel + "', '" + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTimeInMillis()) + "')";
	        						System.out.println(query);
	        						statement = connection.createStatement();
	        						int rows = statement.executeUpdate(query);
	        						if(rows > 0){
	        							System.out.println("Inserted successfully");
	        						}else{
	        							System.out.println("Insertion failed.");
	        						}
        						}	        	        					
	        				}
	        			}
	        			updatePatientHealthSupporters(connection, patient.id);
	        		} else if(option == 7){
	        			removeDiagnosis(connection, patient.id);
	        		}
	        		else if(option == 8){
	        			patient.displayRequiredObservations(connection);
	        			patient.displayRecommendedObservations(connection);
	        		}
	        		else if(option == 9){
	        			viewPatientObservations(connection, patient.id);
	        		} else{
	    	        	System.out.println("Exiting...bye bye!!");
	    	        	System.exit(0);
	        		}
		        }	        	
		    } else if(type == 2){
	        	//Indicates a health supporter has logged in.
		    	Statement stmt = connection.createStatement();
		        query = "select hs.* from health_supporter hs, user_info u where u.id=" + user_info.getId() + " and u.id = hs.user_id";
		        result = stmt.executeQuery(query);
		        if(result.next()){
		        	hs.setId(result.getInt(1));
		        	hs.setName(result.getString(2));
		        	hs.setAddress(result.getString(3));
		        	hs.setPhone_num(result.getString(4));
		        	hs.setUser_id(result.getInt(5));
		        	System.out.println("\nHealth Supporter Information");
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
		        
		        while(option < 7){
		        	System.out.println("What do you want to do?\n1.Edit personal Information\n2.Add patient diagnosis\n3.Edit patient information\n4.Add patient observation\n5.View alerts\n6.View Patient Observations\n7.Exit");
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
		        		//Update corresponding entry in patient table as well.
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
				        			System.out.println("No such patients found. Please try again!!!");
				        		} else{
				        			break;
				        		}
			        		}
			        		addDiagnosis(connection,patID);
		        		}
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
			        		System.out.println("Enter new DOB(yyyy-MM-dd format): ");
			        		String dob = sc.nextLine();
			        		java.util.Date dt = new java.util.Date();
			        		try {
								dt = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
			        		
			        		System.out.println(dt.getTime());
			        		java.sql.Date sqlDate = new java.sql.Date(dt.getTime());
			        		
			        		query = "update patient p set p.dob = ? ,p.name=? ,p.address = ? ,p.phone_num=? where p.id=?";
			        		PreparedStatement pst = connection.prepareStatement(query);
			        		
			        		pst.setDate(1, sqlDate);
			        		pst.setString(2, name);
			        		pst.setString(3, address);
			        		pst.setString(4, phone);
			        		pst.setInt(5, patID);
			        		
			        		System.out.println(query);
			        		int rows = pst.executeUpdate();
			        		if(rows > 0){
			        			System.out.println("Update patient successful");
			        		} else{
			        			System.out.println("Update ptaient unsuccessful");
			        		}
			        		
			        		//Update entry in HS table as well.
			        		statement = connection.createStatement();
			        		query = "update health_supporter hs,patient p,user_info u set hs.name='" + name + "', hs.address='" + address + "', hs.phone_num='" + phone + "' where hs.user_id= u.id and p.user_id=u.id and p.id=" + patID + "";
			        		System.out.println(query);
			        		rows = statement.executeUpdate(query);
			        		if(rows > 0){
			        			System.out.println("Update successful");
			        		} else{
			        			System.out.println("Update unsuccessful");
			        		}
		        		}		        		
		        	} else if(option == 4){
		        		if(hs_pat.size() == 0){
		        			System.out.println("You have no authorized patients.");
		        		} else{
		        			int patID = 0;
			        		while(patID == 0){
				        		System.out.println("Enter the authorized patient name whose observations you want to view: ");
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
			        		addPatientObservation(connection, patID);
		        		}		        		
		        	} else if(option == 5){
		        		viewHSAlerts();
		        	} else if(option == 6){
		        		if(hs_pat.size() == 0){
		        			System.out.println("You have no authorized patients.");
		        		} else{
		        			int patID = 0;
			        		while(patID == 0){
				        		System.out.println("Enter the authorized patient name whose observations you want to view: ");
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
			        		viewPatientObservations(connection, patID);
		        		}
		        	} else{
		        		System.out.println("Exiting...bye bye!!");
		        		System.exit(0);
		        	}		        
		        }
		        connection.close();		    
	        } else {
	        	System.out.println("Invalid choice. Exiting!!!");
	        	System.exit(0);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeResources(sc);
	}
	
	private static void viewPatientObservations(Connection connection, int id) {
		Statement statement;
		ResultSet result;
		String query;
		
		Map<String,Integer> categories = new HashMap<>();
		categories.put("Weight",1);
		categories.put("Blood_Pressure",2);
		categories.put("Oxygen_Saturation",3);
		categories.put("Pain_Intensity",4);
		categories.put("Mood",5);
		categories.put("Temperature",6);
		
		Map<Integer,String> mood = new HashMap<>();
		mood.put(1, "Happy");
		mood.put(2, "Neutral");
		mood.put(3, "Sad");
		
		Iterator<Entry<String, Integer>> entries = categories.entrySet().iterator();
		while(entries.hasNext()){
			Map.Entry<String, Integer> entry = entries.next();
			String table = entry.getKey();
			Integer value = entry.getValue();
			if(table.compareTo("Blood_Pressure")!=0){
				query = "select ot.name,ot.metric,p.name,concat(w.value,''),o.observation_date,o.recorded_date from observation o,observation_type ot,patient p," + table + " w where o.pid=" + id + " and o.observation_type_id=" + value + " and ot.observation_type_id=o.observation_type_id and o.pid=p.id and w.observation_id=o.id";
			} else{
				query = "select ot.name,ot.metric,p.name,concat(w.systolic_value,'/',w.diastolic_value),o.observation_date,o.recorded_date from observation o,observation_type ot,patient p," + table + " w where o.pid=" + id + " and o.observation_type_id=" + value + " and ot.observation_type_id=o.observation_type_id and o.pid=p.id and w.observation_id=o.id";
			}
			try{
				statement = connection.createStatement();
				result = statement.executeQuery(query);
				if(!result.isBeforeFirst()){
					System.out.println("\nNo observations found for " + table);
				}else{
					System.out.println("\nObservations for " + table);
					while(result.next()){
						String str = (table == "Mood") ? mood.get(Integer.parseInt(result.getString(4))) : result.getString(4);
						System.out.println(str + result.getString(2) + " observed on " + result.getDate(5));
					}
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}		
	}

	private static void displaySystemDiagnosis(Connection connection) {
		try{
			Statement statement = connection.createStatement();
			String query = "select * from diagnosis";
			ResultSet result = statement.executeQuery(query);
			if(!result.isBeforeFirst()){
				System.out.println("\nSystem does not have any diagnosis defined.");
			} else{
				System.out.println("\nList of system defined diagnosis.");
				while(result.next()){
					Diagnosis d = new Diagnosis();
					d.setId(result.getInt(1));
					d.setName(result.getString(2));
					d.setDesc(result.getString(3));
					if(d.id != 0){
						//Only add and display diagnosis whose ID is not zero.
						diagnosis.add(d);
						System.out.println(d.desc);
					}					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}				
	}

	/*
	 * Function to handle the login functionality
	 * @params {Integer} type - Specify to login as a patient or a health supporter
	 * @params {Connection} connection - SQL Connection object used to query to the database 
	 */
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
				//While querying check if the user exists as a patient or a health supporter too as someone should not login as a patient if they are only a health supporter.
				if(type == 1){
					result = statement.executeQuery("select * from user_info u,patient p where p.user_id = u.id and u.username='" + username + "' and u.password='" + password + "'");
				}else{
					result = statement.executeQuery("select * from user_info u,health_supporter hs where hs.user_id = u.id and u.username='" + username + "' and u.password='" + password + "'");
				}
				
				if(!result.next()){
					System.out.println("Username and password combination is incorrect.\nPlease try again....");									
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
				System.out.println("Error encountered. Exiting!!!");
				e.printStackTrace();
				System.exit(0);
			}
		}		
	}

	private static void manageSignUp(Connection connection) {
		int option = 0;
		boolean validUsername = false;
		String username = "";
		String password = "";
		String query = "select max(p.id),max(hs.id),max(u.id) from patient p,health_supporter hs,user_info u";
		Statement st;
		ResultSet result;
		int rows, maxPID = 0, maxHSID = 0, maxUID = 0;
		
		try{
			st = connection.createStatement();
			result = st.executeQuery(query);
			if(result.next()){
				maxPID = result.getInt(1);
				maxHSID = result.getInt(2);
				maxUID = result.getInt(3);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
				
		try{
			do{
				System.out.println("\nEnter preferred username: ");
				username = sc.next();
				st = connection.createStatement();
				query = "select * from user_info u where u.username = '" + username + "'";
				result = st.executeQuery(query);
				if(result.next()){
					System.out.println("\nUsername already exists. Please enter new username.");					
				} else {
					validUsername = true;
				}
			} while(validUsername == false);
			
			System.out.println("Enter preferred password: ");
			password = sc.next();
			
			query = "insert into user_info values(" + (maxUID + 1) + ", '" + username + "', '" + password + "', NULL, NULL)";
			st = connection.createStatement();
			rows = st.executeUpdate(query);
			if(rows > 0){
				System.out.println("Insertion successful.");
				maxUID = maxUID + 1;
			} else{
				System.out.println("Insertion failed.");
			}
		} catch(Exception e){
			e.printStackTrace();
		}			
		
		do{
			System.out.println("Enter your option to sign up as:\n1. Patient \n2. Health Supporter \n3. Patient and Health Supporter");
			option = Integer.parseInt(sc.next(), 10);
			if(option < 1 && option > 3){
				System.out.println("Invalid option. Please try again.");
			}
		} while(option < 1 && option > 3);
		sc.nextLine();
		System.out.println("Enter you name: ");
		String name = sc.nextLine();
		System.out.println("Enter your gender: ");
		String gender = sc.nextLine();
		System.out.println("Enter your address: ");
		String address = sc.nextLine();
		System.out.println("Enter your phone number: ");
		String phone = sc.nextLine();
		System.out.println("Enter you DOB: ");
		String dob = sc.nextLine();
		
		switch(option){
			case 1:
				try{
					query = "insert into patient values(" + (maxPID + 1) + ", '" + name + "', '" + dob + "', '" + address + "', '" + gender + "', '" + phone + "', " + maxUID + ")";
					System.out.println(query);
					st = connection.createStatement();
					rows = st.executeUpdate(query);
					if(rows > 0){
						System.out.println("Insertion into Patient successful.");
					} else{
						System.out.println("Insertion into Patient failed.");
					}
				} catch(Exception e){
					e.printStackTrace();
				}				
				break;
			case 2:
				try{
					query = "insert into health_supporter values(" + (maxHSID + 1) + ", '" + name + "', '" + address + "', '" + phone + "', " + maxUID + ")";
					System.out.println(query);
					st = connection.createStatement();
					rows = st.executeUpdate(query);
					if(rows > 0){
						System.out.println("Insertion into HS successful.");
					} else{
						System.out.println("Insertion into HS failed.");
					}
				} catch(Exception e){
					e.printStackTrace();
				}
				
				break;
			case 3:
				try{
					query = "insert into patient values(" + (maxPID + 1) + ", '" + name + "', '" + dob + "', '" + address + "', '" + gender + "', '" + phone + "', " + maxUID + ")";
					System.out.println(query);
					st = connection.createStatement();
					rows = st.executeUpdate(query);
					if(rows > 0){
						System.out.println("Insertion into Patient successful.");
					} else{
						System.out.println("Insertion into Patient failed.");
					}
					query = "insert into health_supporter values(" + (maxHSID + 1) + ", '" + name + "', '" + address + "', '" + phone + "', " + maxUID + ")";
					System.out.println(query);
					st = connection.createStatement();
					rows = st.executeUpdate(query);
					if(rows > 0){
						System.out.println("Insertion into HS successful.");
					} else{
						System.out.println("Insertion into HS failed.");
					}
				} catch(Exception e){
					e.printStackTrace();
				}
				
				break;
			default:
				System.out.println("Incorrect option!!!");
				break;
		}		
	}

	private static void removeDiagnosis(Connection connection, int id) {
		if(pat_diagnosis.size() == 0){
			System.out.println("No diagnosis to remove.");
		} else{
			try {
				Statement stmt = connection.createStatement();
				int diagID = 0;
				System.out.println("Enter diagnosis name to remove: ");
				String name = sc.nextLine();
				for(Diagnosis d : diagnosis){
					if(name.compareToIgnoreCase(d.name) == 0){
						diagID = d.id;
						break;
					}
				}

				String query = "delete from patient_diagnosis where p_id = " + id + " and d_id = " + diagID;
				System.out.println(query);
				int rows = stmt.executeUpdate(query);
				if(rows > 0){
					System.out.println("Diagnosis removed successfully.");
				} else{
					System.out.println("Error removing diagnosis.");
				}
				pat_diagnosis = patient.getPatientDiagnosis(connection, user_info.id);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}				
	}	

	private static void updatePatientHealthSupporters(Connection connection, int id) {
		pat_hs = new ArrayList<>();
		try{
			Statement statement = connection.createStatement();
			String query = "select hs.*,hsp.primary_ind,hsp.auth_date from patient p, health_supporter hs, hs_manages_patient hsp where p.id=" + patient.getId() + " and hsp.p_id=p.id and hsp.hs_id=hs.id";
			ResultSet result = statement.executeQuery(query);

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
		} catch(Exception e){
			e.printStackTrace();
		}
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
			if(diagID == 0)
			{
				System.out.println("Sorry.No such Diagnosis exists");
			}
			else
			{
				String query = "insert into patient_diagnosis values(" + patId + ", " + diagID + ", '" + new java.sql.Date(Calendar.getInstance().getTimeInMillis()) + "')";
				System.out.println(query);
				int rows = stmt.executeUpdate(query);
				if(rows > 0){
					System.out.println("New diagnosis added.");
				} else{
					System.out.println("Error adding diagnosis.");
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	private static void viewPatientAlerts() throws SQLException {
		
		System.out.println("Alerts: \n");
		Statement statement = conn.createStatement();
		int i=1;
		System.out.println("   Alert ID \tAlert Date \t Observation Type \t Alert Type");
		ResultSet rs = statement.executeQuery("select * from alerts inner join observation_type using (observation_type_id) where patient_id = '" + patient.getId() + "' order by alert_date");
		while(rs.next())
		{
			Integer alertId = rs.getInt("id");
			Date alertDate = rs.getDate("alerts.alert_date");
			String observationName = rs.getString("observation_type.name");
			String alertType = rs.getString("type");
			
			System.out.println(i++ + ". " + alertId + "\t\t" + alertDate + " \t " + observationName + " \t \t " + alertType);		
		}
		System.out.println();
		System.out.println("Do you want to clear any alert? (y/n): ");
		String input = sc.nextLine();
		if(input.equals("y") || input.equals("Y")){
			Statement clearStatement = conn.createStatement();
			int count = 0;
			rs = clearStatement.executeQuery("select * from alerts inner join observation_type using (observation_type_id) where patient_id = '" + patient.getId() + "' and alerts.type != 'Low Activity' order by alert_date");
			while(rs.next())
			{
				if(count==0){
					System.out.print("Choose the Alert ID(or multiple comma seperated) of the alert(s) that you want cleared: ");
					System.out.println("\n   Alert ID \tAlert Date \t Observation Type \t Alert Type");
				}
				
				Integer alertId = rs.getInt("id");
				Date alertDate = rs.getDate("alerts.alert_date");
				String observationName = rs.getString("observation_type.name");
				String alertType = rs.getString("type");
				
				System.out.println(i++ + ". " + alertId + "\t\t" + alertDate + " \t " + observationName + " \t \t " + alertType);
				count++;
			}
			if(count==0){
				System.out.println("No alerts are available for you to clear.");
				return;
			}
				
			String clearAlert = sc.nextLine();
			String[] alertNumbers = clearAlert.split(",");
			StringBuilder sb = new StringBuilder();
			for(String number : alertNumbers){
				sb.append(number).append(',');
			}
			sb.deleteCharAt(sb.length()-1);
			Statement deleteStatement = conn.createStatement();
			String query = "delete from alerts where type <> 'Low Activity' and id in (" + sb.toString() + " )";
			deleteStatement.executeUpdate(query);
			
			System.out.println("Alerts cleared. \n");
			
		}
		statement.close();
	}

	private static void viewHSAlerts() throws SQLException{

		System.out.println("Alerts: \n");
		Statement statement = conn.createStatement();
		int i=1,count=0;
		System.out.println("   Alert ID \tAlert Date \t Observation Type \t Alert Type");
		ResultSet rs = statement.executeQuery("select * from alerts inner join observation_type using (observation_type_id) inner join hs_manages_patient on(alerts.patient_id = hs_manages_patient.p_id) where hs_manages_patient.hs_id = '" + patient.getId() + "' order by alert_date");
		while(rs.next())
		{
			Integer alertId = rs.getInt("id");
			Date alertDate = rs.getDate("alerts.alert_date");
			String observationName = rs.getString("observation_type.name");
			String alertType = rs.getString("type");
			
			System.out.println(i++ + ". " + alertId + "\t\t" + alertDate + " \t " + observationName + " \t \t " + alertType);
			count++;
		}
		System.out.println();
		System.out.println("Do you want to clear any alert? (y/n): ");
		String input = sc.nextLine();
		if(input.equals("y") || input.equals("Y")){
			if(count==0){
				System.out.println("No Alerts to delete. Returning to Main Menu.");
				return;
			}
			System.out.print("Choose the Alert ID(or multiple comma seperated) of the alert(s) that you want cleared: ");
			String clearAlert = sc.nextLine();
			String[] alertNumbers = clearAlert.split(",");
			StringBuilder sb = new StringBuilder();
			for(String number : alertNumbers){
				sb.append(number).append(',');
			}
			sb.deleteCharAt(sb.length()-1);
			Statement deleteStatement = conn.createStatement();
			String query = "delete from alerts where id in (" + sb.toString() + " )";
			deleteStatement.executeUpdate(query);
			
			System.out.println("Alerts cleared. \n");
		}
		statement.close();
	}
	
	private static void loadObservationType(Connection c){
		try{
			Statement statement = c.createStatement();
			ResultSet observation_type_result,observation_sub_type_result; 
			String query = "select * from observation_sub_type";
			observation_sub_type_result = statement.executeQuery(query);
			while(observation_sub_type_result.next()){
				ObservationType ot = new ObservationType();
				ot.setId(observation_sub_type_result.getInt(1));
				ot.setName(observation_sub_type_result.getString(2));
				ot.setSubTypeId(observation_sub_type_result.getInt(3));
				
				all_observation_types.add(ot);
				
			}
			query = "select * from observation_type";
			Statement statement2 = c.createStatement();
			observation_type_result = statement2.executeQuery(query);
			while(observation_type_result.next()){
				for(ObservationType ot: all_observation_types){
					if(ot.getId() == observation_type_result.getInt(1)){
						ot.setDesc(observation_type_result.getString(3));
						ot.setMetric(observation_type_result.getString(4));
					}
				}
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}

	private static void addPatientObservation(Connection c, int patID) {
		try
		{
			Statement stmt = c.createStatement();
			int choice = 0;
			HashMap<Integer, String> categories = new HashMap<>();
			categories.put(1,"Weight");
			categories.put(2,"Blood_Pressure");
			categories.put(3,"Oxygen_Saturation");
			categories.put(4,"Pain_Intensity");
			categories.put(5,"Mood");
			categories.put(6,"Temperature");

			do{
				System.out.println("Enter which observation you want to add:\n1.Weight\n2.Blood Pressure\n3.Oxygen Saturation\n4.Pain\n5.Mood\n6.Temperature\n");
				choice = Integer.parseInt(sc.next());
			}while(choice < 0 && choice > 6);

			int value = 0;
			if(choice == 2){
				System.out.println("Enter the patient Blood Pressure Systolic and Diastolic values: \n Systolic: ");
				int systolic = Integer.parseInt(sc.next(), 10);
				System.out.println("\nDiastolic: ");
				int diastolic = Integer.parseInt(sc.next(), 10);
			}
			if(choice == 5){
				do{
					System.out.println("Enter the patient mood\n1 - Happy\n2 - Sad \n3 - Neutral: \n");
					value = Integer.parseInt(sc.next(), 10);
				} while(value < 0 && value > 3);
			} else if(choice == 4){
				do{
					System.out.println("Enter the pain intensity on scale of 1-10\n");
					value = Integer.parseInt(sc.next(), 10);
				} while(value < 0 && value > 3);
			} else{
				System.out.println("Enter the value for patient " + categories.get(choice) + ": \n");
				value = Integer.parseInt(sc.next());;
			}
			System.out.println("Enter the date of observation in the format (yyyy-mm-dd) : \n");
			String date = sc.next();
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-mm-dd");
			java.sql.Date obs_Date = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
			try {
				obs_Date.setTime(fmt.parse(date).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			System.out.println("obsDate entered : "+obs_Date);
			String query = "Insert into Observation (id, observation_type_id, pid, observation_date, recorded_date) values (" + null + "," + choice + "," +patID+",'"+obs_Date+"','" + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTimeInMillis()) + "')";
			System.out.println(query);
			int rows = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			if(rows > 0){
				System.out.println("New observation added.");
			} else{
				System.out.println("Error adding observation.");
			}
			ResultSet keys = stmt.getGeneratedKeys();    
			keys.next();  
			int obsID = keys.getInt(1);
			String subquery = "Insert into " + categories.get(choice) + " values (" + value + "," + obsID +")";
			System.out.println(categories.get(choice));
			rows = stmt.executeUpdate(subquery);
			if(rows > 0){
				System.out.println("New observation added.");
			} else{
				System.out.println("Error adding observation.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}	
	
	public static void closeResources(Scanner sc){
		sc.close();
	}
	
}


