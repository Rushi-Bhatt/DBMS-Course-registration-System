package Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

//Student functinoalities
	//1. View/Edit profile
		//1.1 View profile : Student+Student_Special table to fetch all details
		//1.2 Edit pforile : Fname,Lname,Email,Phone (only 4 fields he can edit)
	//2. View courses/Enroll/Drop/View my courses
		//To understand all these functionalities we need two global variables, for that create one global table
		//1)For deadline is enforced or not 2)Current sem.
		//2.1 View courses (only for current semester) : Fetch all data from course ID, fac_name, Day of week, Start time, End time
		//2.2 Enroll course : First check if the deadline is enforced or not (global variable), 
			//Enter the course ID then ask for faculty name (by fetching this fac-names from DB), and check if the 
			//1. check for credit requirement (if exceeding credit or not)
			//2. if pre-reqs met or not. 
			//3. creating conflict with existing schedule or not. 
			//4. capacity of the class is full or not, 
			//if capacity of the class is full, ask if student wants to be placed on waitlist or not.
			//then check if the wait_list capacity is full or not, then add entry in waitlist table and decrement waitlist count.
			// If none of this, enrolled successfully. After enrolling successfully, decrease the capacity of class size, and show success message
		//2.3 Drop course : show all courses for that student (including all status) for current semester. and ask which one would 
			//he/she like to drop. After dropping, check entry for that SID and Class_ID in enrollment table. If entry is saved
			//as "Enrolled" then 1. Increase class capacity 2.Remove entry. 3.check if we want to create trigger (take first waistlist
			//entry) to confirmed. If status if "wait-listed" then 1.increase waitlist capacity. 2. Remove entry from waitlist table
			//If status is "pending" remove entry from special permission.

public class student_home {

	static Scanner sc=new Scanner(System.in);
	
	public static void studentHome(Connection conn, int personid) throws InterruptedException {
		System.out.println("-----Welcome Student------");
		System.out.println("1. View profile");
		System.out.println("2. Edit Profile");
		System.out.println("3. View All Courses");
		System.out.println("4. Enroll Courses");
		System.out.println("5. Drop course");
		System.out.println("6. View all my Courses");
		System.out.println("7. View Grades");
		System.out.println("8. View/Pay Bill");
		System.out.println("9. Logout");
		System.out.println("Enter your choice :-> ");
		int student_choice = sc.nextInt();
		
		switch(student_choice){
		case 1:
			//View Student's own profile
			viewOwnProfile(conn,personid); //option1: to view own profile
			break;
		case 2:
			//Edit Student's profile
			editOwnProfile(conn,personid);
			break;
		case 3:
			//View all available courses, irrespective of whether they are full/not
			viewAllCourses(conn,personid);
			break;
		case 4:
			//Enroll courses
			//enrollCourse(conn,personid);
			break;
		case 5:
			//Drop course
		
			//View
			break;
		case 6:
			//View my courses from enrollment table
			viewMyCourses(conn,personid);
			break;
		case 7:
			//View grades
			
			break;
		case 8:
			//View/Pay bill
			System.out.println("Loggin out...");
			TimeUnit.SECONDS.sleep(3);
			break;
		case 9:
			//Logout
			
			break;
		default:
			//invalid option selected. Throw back to previous menu.
			break;
			
		}
	}

	public static void viewOwnProfile(Connection conn, int personid) {
		// TODO Auto-generated method stub
		try {
			System.out.println("View your own profile");
			System.out.println("Press 0 to go back");
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT  SID,FNAME,LNAME,TO_CHAR(DOB,'dd-MON-yyyy') as BIRTH,EMAIL,PHONE,ADDRESS,STUDENT_SPECIAL_ID FROM STUDENT WHERE SID=?");
			stmt.setInt(1, personid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				System.out.println("Student ID :-> " + rs.getInt("SID"));
				System.out.println("First Name :-> " + rs.getString("FNAME"));
				System.out.println("Last Name :-> " + rs.getString("LNAME"));
				System.out.println("D.O.B. :-> " + rs.getString("BIRTH"));
				System.out.println("EMail id :-> " + rs.getString("EMAIL"));
				System.out.println("Phone :-> " + rs.getString("PHONE"));
				System.out.println("Address :-> " + rs.getString("ADDRESS"));
				
				//to print level and residency from student_special_id
				PreparedStatement stmt1 = conn.prepareStatement(
				"SELECT  LVL,RESIDENCY FROM STUDENT_SPECIAL WHERE STUDENT_SPECIAL_ID=?");
				stmt1.setInt(1, rs.getInt("STUDENT_SPECIAL_ID")); //get student special id from earlier query
				ResultSet rs1 = stmt1.executeQuery();
				while (rs1.next()) {
					System.out.println("Level:-> " + rs1.getString("LVL"));
					System.out.println("Residency :-> " + rs1.getString("RESIDENCY"));
				}
			}
			int choice = sc.nextInt();
			if (choice == 0) {
				studentHome(conn, personid);
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	public static void editOwnProfile(Connection conn, int personid) {
		// TODO Auto-generated method stub
		//
		try {
			System.out.println("Your profile Information");
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT  FNAME,LNAME,TO_CHAR(DOB,'dd-MON-yyyy') as BIRTH,EMAIL,PHONE,ADDRESS FROM STUDENT WHERE SID=?");
			stmt.setInt(1, personid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				System.out.println("1.First Name :-> " + rs.getString("FNAME"));
				System.out.println("2.Last Name :-> " + rs.getString("LNAME"));
				System.out.println("3.D.O.B. :-> " + rs.getString("BIRTH"));
				System.out.println("4.EMail id :-> " + rs.getString("EMAIL"));
				System.out.println("5.Phone :-> " + rs.getString("PHONE"));
				System.out.println("6.Address :-> " + rs.getString("ADDRESS"));	
			}
			System.out.println("Enter your selection to edit(Press 0 to go back): -->");
			int choice = sc.nextInt();
			switch(choice){
				case 0: 
					studentHome(conn, personid);
					break;
				
				case 1://edit first name
					System.out.println("Enter new First name: --> ");
					String fname = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE STUDENT SET FNAME = ? WHERE SID=?");
					stmt.setString(1, fname);
					stmt.setInt(2, personid);
					stmt.executeUpdate();
					System.out.println("First name edited successfully");
					editOwnProfile(conn, personid);
					break;
					
				case 2://edit Last name
					System.out.println("Enter new Last name: --> ");
					String lname = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE STUDENT SET LNAME = ? WHERE SID=?");
					stmt.setString(1, lname);
					stmt.setInt(2, personid);
					stmt.executeUpdate();
					System.out.println("Last name edited successfully");
					editOwnProfile(conn, personid);
					break;
					
				case 3://edit D.O.B
					System.out.println("Enter new D.O.B(dd-MON-yyyy): --> ");
					String dob = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE STUDENT SET DOB = ? WHERE SID=?");
					stmt.setString(1, dob);
					stmt.setInt(2, personid);
					stmt.executeUpdate();
					System.out.println("Date of birth edited successfully");
					editOwnProfile(conn, personid);
					break;
					
				case 4://edit Email
					System.out.println("Enter new email address: --> ");
					String email = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE STUDENT SET EMAIL = ? WHERE SID=?");
					stmt.setString(1, email);
					stmt.setInt(2, personid);
					stmt.executeUpdate();
					System.out.println("Email address edited successfully");
					editOwnProfile(conn, personid);
					break;
					
				case 5://edit Phone
					System.out.println("Enter new Phone number: --> ");
					long phone = sc.nextLong();
					stmt = conn.prepareStatement(
					"UPDATE STUDENT SET PHONE = ? WHERE SID=?");
					stmt.setLong(1, phone);
					stmt.setInt(2, personid);
					stmt.executeUpdate();
					System.out.println("Phone number edited successfully");
					editOwnProfile(conn, personid);
					break;
				
				case 6://edit Address
					System.out.println("Enter new Address: --> ");
					String address = sc.next();
					stmt = conn.prepareStatement(
					"UPDATE STUDENT SET ADDRESS = ? WHERE SID=?");
					stmt.setString(1, address);
					stmt.setInt(2, personid);
					stmt.executeUpdate();
					System.out.println("Address edited successfully");
					editOwnProfile(conn, personid);
					break;
				
				default:
					// invalid option selected. Throw back to previous menu.
					break;		
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	public static void viewAllCourses(Connection conn, int personid) {
		// Show all courses for current semester.
		try {
			System.out.println("All Available Courses for this semester:");
			//Get current semester from global_var table
			PreparedStatement stmt1 = conn.prepareStatement("SELECT SEMESTER FROM GLOBAL_VAR");
			ResultSet rs1 = stmt1.executeQuery();
			while(rs1.next()){
				String semester = rs1.getString("SEMESTER");
				System.out.println("Available courses for "+semester);
				//Get all the details from class table
				PreparedStatement stmt = conn.prepareStatement(
				"SELECT  CLASS_ID,CID,FAC_NAME,DAYS,START_TIME,END_TIME FROM CLASS WHERE SEMESTER=?");
				stmt.setString(1, semester);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					System.out.print("Class ID :-> " + rs.getInt("CLASS_ID"));
					String course_id = rs.getString("CID");
					System.out.print(",  Course ID :-> " + course_id);
					
					//Get course title from CID
					PreparedStatement stmt2 = conn.prepareStatement(
					"SELECT  TITLE FROM COURSE WHERE CID = ?");
					stmt2.setString(1, course_id);
					ResultSet rs2 = stmt2.executeQuery();
					while(rs2.next()){
						System.out.print(",  Course Title :-> " + rs2.getString("TITLE"));
					}
					
					System.out.print(",  Faculty name :-> " + rs.getString("FAC_NAME"));
					System.out.print(",  Days :-> " + rs.getString("DAYS"));
					System.out.print(",  Time :-> " + rs.getString("START_TIME")+" -- "+rs.getString("END_TIME"));
					
				}
			}
			int choice = sc.nextInt();
			if (choice == 0) {
				studentHome(conn, personid);
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	public static void viewMyCourses(Connection conn, int personid) {
		// TODO Auto-generated method stub
		//view courses from enrollment for current semester
		try {
			//Get current semester from global_var table
			PreparedStatement stmt = conn.prepareStatement("SELECT SEMESTER FROM GLOBAL_VAR");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){ //for each semester
				String semester = rs.getString("SEMESTER");
				System.out.println("My courses for "+semester);
				
				//Get all the details from enrollment table
				PreparedStatement stmt1 = conn.prepareStatement(
				"SELECT  CLASS_ID,STATUS FROM ENROLLMENT WHERE SID = ? AND SEMESTER = ?");
				stmt1.setInt(1, personid);
				stmt1.setString(2, semester);
				ResultSet rs1 = stmt1.executeQuery();
				while (rs1.next()) { //for each classid of enrollment
					System.out.print("Class ID :-> " + rs1.getInt("CLASS_ID"));
					System.out.print(",  Status :-> " + rs1.getString("STATUS"));
					
					//get all the details from class id using the class table
					PreparedStatement stmt2 = conn.prepareStatement(
					"SELECT  CID,FAC_NAME,DAYS,START_TIME,END_TIME FROM CLASS WHERE CLASS_ID=?");
					stmt2.setInt(1, rs1.getInt("CLASS_ID"));
					ResultSet rs2 = stmt2.executeQuery();
					
					while (rs2.next()) { //for each classid entry of class table
						//Get CID from classid
						String course_id = rs2.getString("CID");
						System.out.print(",  Course ID :-> " + course_id);
						
						//Get course title from CID
						PreparedStatement stmt3 = conn.prepareStatement(
						"SELECT  TITLE FROM COURSE WHERE CID = ?");
						stmt3.setString(1, course_id);
						ResultSet rs3 = stmt3.executeQuery();
						while(rs3.next()){
							System.out.print(",  Course Title :-> " + rs3.getString("TITLE"));
						} ///closing for rs3
	
						System.out.print(",  Faculty name :-> " + rs2.getString("FAC_NAME"));
						System.out.print(",  Days :-> " + rs2.getString("DAYS"));
						System.out.println(",  Time :-> " + rs2.getString("START_TIME")+" -- "+rs2.getString("END_TIME"));		
					} //closing for rs2	
				} //closing for rs1
			}//closing for rs
			int choice = sc.nextInt();
			if (choice == 0) {
				studentHome(conn, personid);
			}
		} //closing for try
		catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	
	public static void dropCourse(Connection conn, int personid){
		
	}


}
