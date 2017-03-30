package Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	
	public static void studentHome(Connection conn, int personid) throws InterruptedException, SQLException {
		PreparedStatement globl_stmt = conn.prepareStatement(
				"SELECT DEADLINE_ENFORCED FROM GLOBAL_VAR");
		ResultSet rs = globl_stmt.executeQuery();
		int deadline_enforced=0;
		while(rs.next()){
			System.out.println("Deadline is enforced or not:->"+rs.getInt("DEADLINE_ENFORCED"));
			deadline_enforced=rs.getInt("DEADLINE_ENFORCED");
		}

		System.out.println("-----Welcome Student------");
		System.out.println("1. View profile");
		System.out.println("2. Edit Profile");
		System.out.println("3. View All Courses");
		if(deadline_enforced==0)System.out.println("4. Enroll Courses");
		if(deadline_enforced==0)System.out.println("5. Drop course");
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
			if(deadline_enforced==0)enrollCourse(conn,personid);
			break;
		case 5:
			//Drop course
			if(deadline_enforced==0)
				dropCourse(conn,personid);
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
			handleBilling(conn,personid);
			break;
		case 9:
			//Logout
			System.exit(0);
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
					stmt = conn.prepareStatement("UPDATE STUDENT SET LNAME = ? WHERE SID=?");
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
					System.out.print("|  Course ID :-> " + course_id);
					
					//Get course title from CID
					PreparedStatement stmt2 = conn.prepareStatement(
					"SELECT  TITLE FROM COURSE WHERE CID = ?");
					stmt2.setString(1, course_id);
					ResultSet rs2 = stmt2.executeQuery();
					while(rs2.next()){
						System.out.print("|  Course Title :-> " + rs2.getString("TITLE"));
					}
					
					System.out.print("|  Faculty name :-> " + rs.getString("FAC_NAME"));
					System.out.print("|  Days :-> " + rs.getString("DAYS"));
					System.out.println("|  Time :-> " + rs.getString("START_TIME")+" -- "+rs.getString("END_TIME"));
					System.out.println("------------------------------------------------------------------------------------------------");
				}
			}
			System.out.println("Press 0 to go back to Previous Menu");
			int choice = sc.nextInt();
			if (choice == 0) {
				studentHome(conn, personid);
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
public static void enrollCourse(Connection conn, int personid) throws SQLException, InterruptedException {
		
		//get current semester
		PreparedStatement globl_stmt = conn.prepareStatement(
				"SELECT * FROM GLOBAL_VAR");
		ResultSet rs1 = globl_stmt.executeQuery();
		String sem="";
		while(rs1.next()){
			System.out.println("SEMESTER->"+rs1.getString("SEMESTER"));
			sem=rs1.getString("SEMESTER");
		}
		// Enroll for the course.
		//First, check if th dead_line is enforced or not from global_var table
		//Second, take input from student about course_id
		System.out.println("Enter Course ID:-->");
		String course_id=sc.next();
		PreparedStatement stmt = conn.prepareStatement(
				"SELECT CLASS_ID,CID,FAC_NAME,LOCATION,DAYS,START_TIME,END_TIME FROM CLASS WHERE CID=?");
		stmt.setString(1, course_id);
		ResultSet rs = stmt.executeQuery();
		System.out.println("Classes for this course");
		while(rs.next()){
			System.out.print("CLASS_ID :-> " + rs.getString("CLASS_ID")+"|");
			System.out.print("Course ID :-> " + rs.getString("CID")+"|");
			System.out.print("Fac_Name :-> " + rs.getString("FAC_NAME")+"|");
			System.out.print("Location :-> " + rs.getString("LOCATION")+"|");
			System.out.print("Days :-> " + rs.getString("DAYS")+"|");
			System.out.print("Start_time :-> " + rs.getString("START_TIME")+"|");
			System.out.println("End_time :-> " + rs.getString("END_TIME")+"|");
			System.out.println("------------------------------------------------------------------------------------------------");
		}
		System.out.println("Select ClASS_ID to enroll:->");
		int class_id=sc.nextInt();
		
		//check for credit requirement. Go to Enrollment table and find all classes from table (for current sem) 
		//and status (all except rejected) for current semester only. Compare this with his max_credit limit from sud_special table
		
		//Code to fetch max_limit_credit
		PreparedStatement specil_id_stmt = conn.prepareStatement(
				"SELECT * FROM STUDENT WHERE SID = ?");
		specil_id_stmt.setInt(1, personid);
		ResultSet rs_spcl_id = specil_id_stmt.executeQuery();
		int st_spcl_id=0;
		while(rs_spcl_id.next()){
			System.out.println("Student special ID is:->"+rs_spcl_id.getInt("STUDENT_SPECIAL_ID"));
			st_spcl_id=rs_spcl_id.getInt("STUDENT_SPECIAL_ID");
		}
		PreparedStatement max_credit_stmt = conn.prepareStatement(
				"SELECT * FROM STUDENT_SPECIAL WHERE STUDENT_SPECIAL_ID = ?");
		max_credit_stmt.setInt(1, st_spcl_id);
		ResultSet rs_max_credit = max_credit_stmt.executeQuery();
		int max_credit_limit=0;
		while(rs_max_credit.next()){
			System.out.println("Max credits allowed for this students are"+rs_max_credit.getInt("MAX_CREDIT"));
			max_credit_limit=rs_max_credit.getInt("MAX_CREDIT");
		}
		//We have now max_credit_limit for this student
		
		//Code to get all credits currently student has enrolled for
		//Got to enrollment table : Current sem+SID+Status(except rejected)
		PreparedStatement credit_stmt = conn.prepareStatement("SELECT SUM(MAX_CREDIT) AS TOTAL_CREDIT FROM COURSE,CLASS,ENROLLMENT"
    			+ "  WHERE COURSE.CID = CLASS.CID AND CLASS.CLASS_ID=ENROLLMENT.CLASS_ID"
    			+ "  AND STATUS NOT IN ('REJECTED') AND ENROLLMENT.SID=? GROUP BY ENROLLMENT.SID");
		credit_stmt.setInt(1, personid);
		ResultSet max_credit_enroll = credit_stmt.executeQuery();
		
		int max_credit_limit1=0;
		while(max_credit_enroll.next()){
			System.out.println("Answer is "+max_credit_enroll.getInt("TOTAL_CREDIT"));
			max_credit_limit1=max_credit_enroll.getInt("TOTAL_CREDIT");
		}
//		if(max_credit_limit1>max_credit_limit){
//			System.out.println("You have already enrolled for maximum credits. Can't add this cout");
//			System.out.println("Redirecting back to home page...");
//			TimeUnit.SECONDS.sleep(3);
//			studentHome(conn, personid);
//		}
		//End of condition checking : max_credit_enrolled>max_credit_allowed
		
		//Start of condition checking : Class capacity is full or not
		PreparedStatement capacity_stmt = conn.prepareStatement("SELECT CAPACITY FROM CLASS WHERE CLASS_ID =?");
		capacity_stmt.setInt(1,class_id);
		ResultSet max_class_capacity = capacity_stmt.executeQuery();
		int max_capacity=0;
		while(max_class_capacity.next()){
			max_capacity=max_class_capacity.getInt("CAPACITY");
		}
		System.out.println("Maximum class capacity is :->"+max_capacity);
		if(max_capacity<=0){
			System.out.println("Class is already full. Would you like to be placed on waitlist (Y/N)?:->");
		}
		String wait_list_choice=sc.next();
		if(wait_list_choice.equals("Y")){
			System.out.println("Logic to place on waitlist");
		}else{
			System.out.println("waitlist not needed. going back to previus menu");
			studentHome(conn, personid);
		}
		//end of condition checking : Class capacity is full or not
		
		
		//Start of pre-req checking : pre-reqs met or not
		//1. course ID from class
		//2. person_id
		PreparedStatement pre_req_stmt = conn.prepareStatement("SELECT "
    			+ "(SELECT COUNT(*) FROM PRE_REQ WHERE CID=?) - "
    			+ "(SELECT COUNT(*) FROM ENROLLMENT,CLASS,COURSE WHERE COURSE.CID=CLASS.CID AND CLASS.CLASS_ID = ENROLLMENT.CLASS_ID  AND ENROLLMENT.SID=?"
    			+ "AND ENROLLMENT.STATUS IN 'Enrolled' AND SEMESTER NOT IN (?) AND COURSE.CID IN "
    			+ "(SELECT PRE_REQ_COURSES FROM PRE_REQ WHERE CID=?)) AS TOTAL_COUNT FROM dual;");
		pre_req_stmt.setString(1,course_id);
		pre_req_stmt.setInt(2,personid);
		pre_req_stmt.setString(3,sem);
		pre_req_stmt.setString(4,sem);
		pre_req_stmt.setString(1,course_id);
		ResultSet pre_req_rs = pre_req_stmt.executeQuery();
		boolean pre_req_met=pre_req_rs.getInt("TOTAL_COUNT")>0?false:true;
		if(pre_req_met)System.out.println(" Met");
		else System.out.println("Not met");
		//end of pre-req checking : pre-reqs met or not
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
						System.out.print("|  Course ID :-> " + course_id);
						
						//Get course title from CID
						PreparedStatement stmt3 = conn.prepareStatement(
						"SELECT  TITLE FROM COURSE WHERE CID = ?");
						stmt3.setString(1, course_id);
						ResultSet rs3 = stmt3.executeQuery();
						while(rs3.next()){
							System.out.print("|  Course Title :-> " + rs3.getString("TITLE"));
						} ///closing for rs3
	
						System.out.print("|  Faculty name :-> " + rs2.getString("FAC_NAME"));
						System.out.print("|  Days :-> " + rs2.getString("DAYS"));
						System.out.println("|  Time :-> " + rs2.getString("START_TIME")+" -- "+rs2.getString("END_TIME"));
						System.out.println("------------------------------------------------------------------------------------------------");
					} //closing for rs2	
				} //closing for rs1
			}//closing for rs
			System.out.println("Press 0 to go back to Previous Menu");
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
		try {
			//Get current semester from global_var table
			PreparedStatement stmt = conn.prepareStatement("SELECT SEMESTER FROM GLOBAL_VAR");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){ //for each semester
				String semester = rs.getString("SEMESTER");
				System.out.println("My courses for "+semester);
				
				//Get all the details from enrollment table
				PreparedStatement stmt1 = conn.prepareStatement(
				"SELECT  CLASS_ID,STATUS FROM ENROLLMENT WHERE SID = ? AND SEMESTER = ? and STATUS NOT LIKE 'Rejected'");
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
			System.out.println("Choose the ClassId you want to drop: ");
			System.out.println("Enter 0 to go back to the previous menu: ");
			//go back to previous menu	
			int cid = sc.nextInt();
			if (cid == 0) {
				studentHome(conn, personid);
			}
			else{
				//FIND THE STATUS
				PreparedStatement stmt3 = conn.prepareStatement("select STATUS from enrollment where class_id = ?");
				stmt3.setInt(1, cid);
				ResultSet rs3 = stmt3.executeQuery();
				
				//DROP THE ENTRY FROM ENROLLMENT
				PreparedStatement stmt4 = conn.prepareStatement("DELETE from enrollment where class_id = ? and sid=?");
				stmt4.setInt(1, cid);
				stmt4.setInt(2, personid);
				stmt4.executeQuery();

				if(rs3.next()){
					String status = rs3.getString("STATUS");
					if(status.toLowerCase().equals("waitlisted")){
						dropWaitlistedCourse(conn, personid, cid);
					}
					if(status.toLowerCase().equals("enrolled")){
						dropEnrolledCourse(conn, personid, cid);
					}
					
					if(status.toLowerCase().equals("pending")){
						dropPendingCourse(conn, personid, cid);
					}
				}//closing first if
			}//closing else
			System.out.println("Dropped Successfully.");
			dropCourse(conn, personid);
			
		} //closing for try
		catch (Exception ex) {
			System.out.println(ex);
		}//closing catch
	}//closing dropCourse()
	
	
	public static void dropWaitlistedCourse(Connection conn, int personid, int cid) throws SQLException{
		try{
		//increase waitlist capacity from class
		PreparedStatement stmt1 = conn.prepareStatement("UPDATE class set WAITLIST_CAPACITY = WAITLIST_CAPACITY +1 "
				+ "WHERE CLASS_ID = ?");
		stmt1.setInt(1, cid);
		stmt1.executeQuery();

		//remove waitlist entry from wait_list table
		PreparedStatement stmt2 = conn.prepareStatement("Delete from waitlist where sid = ? and class_id = ?");
		stmt2.setInt(1, personid);
		stmt2.setInt(2, cid);
		stmt2.executeQuery();
				
		}
		catch(Exception ex){
			System.out.println("Couldn't drop waitlisted course. Error : "+ ex);
		}
		
	}

	public static void dropEnrolledCourse(Connection conn, int personid, int cid){
		try{
			//check if there's a waitlist
			PreparedStatement stmt1 = conn.prepareStatement("select count(*) as waitlist from wait_list where CLASS_ID = ?");
			stmt1.setInt(1, cid);
			ResultSet rs = stmt1.executeQuery();
			int waitlist = 0;
			if(rs.next()){
				waitlist = rs.getInt("waitlist");
			}
			//if there's a waitlist: 
			if(waitlist>0){
				//1. increase the waitlist capacity 
				PreparedStatement stmt2 = conn.prepareStatement("UPDATE class set waitlist_CAPACITY = waitlist_CAPACITY +1 "
						+ "WHERE CLASS_ID = ?");
				stmt2.setInt(1, cid);
				stmt2.executeQuery();
	
			//find the sid of the top student AND DELETE HIM
				int newpersonid = 0;
				PreparedStatement stmt3 = conn.prepareStatement("select sid from wait_list where class_id = ? and rownum=1");
				stmt3.setInt(1, cid);
				ResultSet rs1 = stmt3.executeQuery();
				if(rs1.next()){
					newpersonid = rs1.getInt("SID");
				}
				PreparedStatement stmt4 = conn.prepareStatement("DELETE from wait_list where class_id = ? and rownum=1");
				stmt4.setInt(1, cid);
				stmt4.executeQuery();			
				//enroll him				
				PreparedStatement stmt5 = conn.prepareStatement("UPDATE ENROLLMENT SET STATUS = 'Enrolled' where "
						+ "class_id = ? and sid= ?");
				stmt5.setInt(1, cid);
				stmt5.setInt(2, newpersonid);
				stmt5.executeQuery();

			
			}
			else{		
			//increase class capacity
			PreparedStatement stmt6 = conn.prepareStatement("UPDATE class set CAPACITY = CAPACITY +1 "
					+ "WHERE CLASS_ID = ?");
			stmt6.setInt(1, cid);
			stmt6.executeQuery();
			}
		}
		catch(Exception ex){
			System.out.println("Couldn't drop waitlisted course. Error : "+ ex);			
		}
		
	}

	public static void dropPendingCourse(Connection conn, int personid, int cid){
		try{
			//remove entry from special_enrollment
			PreparedStatement stmt1 = conn.prepareStatement("Delete from special_permission where sid = ? "
					+ "and class_id = ?");
			stmt1.setInt(1, personid);
			stmt1.setInt(2, cid);
			stmt1.executeQuery();
			
		}
		catch(Exception ex){
			System.out.println("Couldn't drop waitlisted course. Error : "+ ex);
		}
		
	}
	
	public static void handleBilling(Connection conn, int personid) {
		// show options for view bill and pay bill 
		try {
				//Get current semester from global_var table
				PreparedStatement stmt = conn.prepareStatement("SELECT BILL_ID,BILLAMOUNT FROM ACCOUNT WHERE SID=?");
				stmt.setInt(1, personid);
				ResultSet rs = stmt.executeQuery();
				int billamount = 0;
				while(rs.next()){ //for each bill of student
					//show the total bill by adding the individual bills of student
					billamount += rs.getInt("BILLAMOUNT");  
				}//closing for rs
				System.out.println("Your pending bill amount is:"+billamount);
				if(billamount>0){
					System.out.println("Press 1 to Pay the bill,  Press 0 to go back to Previous Menu");
				}else{
					System.out.println("Press 0 to go back to Previous Menu");
				}
				int choice = sc.nextInt();
				if (choice == 0) 
				{
					studentHome(conn, personid);
				}
				else if(choice == 1)
				{
					if(billamount>0){
						//pay the bill 
						//by creating new entry with the negative amount, should not be greater than the 
						//pending amount though, else show validation there.
						System.out.println("Enter the amount you would like to pay: ");
						float amount = sc.nextFloat();
						if(amount>billamount){
							System.out.println("Your due amount is "+billamount+"So enter any value less than that");
							handleBilling(conn,personid);
						}
						stmt = conn.prepareStatement("INSERT INTO ACCOUNT(BILL_ID,BILLAMOUNT,SID) VALUES(ACCOUNT_SEQ.NEXTVAL,?,?)");
						stmt.setFloat(1, (amount* -1));
						stmt.setInt(2, personid);
						stmt.executeUpdate();
						System.out.println("Bill Paid Successfully...!");
						handleBilling(conn,personid);
					}
					else{
						System.out.println("Invalid Choice");
						studentHome(conn, personid);
					}
				}	
			} //closing for try
		catch (Exception ex) {
			System.out.println(ex);
		}
	}

	

}
