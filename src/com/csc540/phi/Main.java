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
	
	public static Patient patient = new Patient();
	public static HealthSupporter hs = new HealthSupporter();
	public static UserInformation user_info = new UserInformation();//Stores the logged in user's information.
	public static ArrayList<Diagnosis> diagnosis = new ArrayList<>();//Stores the list of diagnosis defined in the system.
	public static ArrayList<Diagnosis> pat_diagnosis = new ArrayList<>();//Stores the list of diagnosis a patient is diagnosed with.
	public static ArrayList<HealthSupporter> pat_hs;//List of health supporters for a patient.
	public static ArrayList<Patient> hs_pat = new ArrayList<>();//List of patients associated to a health supporter. 
	public static Scanner sc = new Scanner(System.in);
	
	public static void closeResources(Scanner sc){
		sc.close();
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
				result = statement.executeQuery("select * from user_info where username='" + username + "' and password='" + password + "'");
				
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
		        System.out.println("\nPatient Information:");
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
		        
		        getPatientDiagnosis(connection);

		        updatePatientHealthSupporters(connection, patient.id);
		        
		        while(option <= 8){
		        	System.out.println("\n1.Edit personal Information\n2.Add diagnosis\n3.Add observation\n4.View alerts\n5.Remove authorized health supporter\n6.Add authorized health supporter\n7.Remove diagnosis\n8.Exit\nEnter your choice: ");
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
		        				statement = connection.createStatement();
		        				query = "delete from hs_manages_patient where p_id=" + patient.id + " and hs_id=" + hs_id + "";
		        				System.out.println(query);
		        				int rows = statement.executeUpdate(query);
		        				if(rows > 0){
		        					System.out.println("Health Supporter removed successfully");
		        				} else {
		        					System.out.println("Removal failed.");
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
	        		else{
	    	        	System.out.println("Exiting...bye bye!!");
	    	        	System.exit(0);
	        		}
		        }
	        	System.out.println("Invalid choice. Exiting!!!");
	        	System.exit(0);
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
		        	System.out.println("What do you want to do?\n1.Edit personal Information\n2.Add patient diagnosis\n3.Edit patient information\n4.Add patient observation\n5.View alerts\n6.Exit");
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
		        	else{
		        		System.out.println("Exiting...bye bye!!");
		        		System.exit(0);
		        	}
		        	System.out.println("Invalid choice. Exiting!!!");
		        	System.exit(0);
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
				getPatientDiagnosis(connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}				
	}

	private static void getPatientDiagnosis(Connection connection) {
		pat_diagnosis = new ArrayList<>();
		try{
			Statement statement = connection.createStatement();
	        String query = "select d.id,d.name,d.description from user_info u,patient p,patient_diagnosis pd, diagnosis d where u.id = " + user_info.getId() + " and p.user_id = u.id and pd.p_id = p.id and pd.d_id = d.id";
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
		        	pat_diagnosis.add(d);
		        	
		        	System.out.println("Diagnosis Name: " + d.getName() + " , Diagnosis Description: " + d.getDesc());
		        }
	        }	
		} catch(Exception e){
			e.printStackTrace();
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

	private static void viewPatientAlerts() {
		
		
	}

	private static void viewHSAlerts() {
		
		
	}

	private static void addPatientObservation() {
		
		
	}	
}


