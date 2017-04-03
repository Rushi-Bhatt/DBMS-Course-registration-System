package Admin;

import java.util.Scanner;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import java.util.concurrent.TimeUnit;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import Connection.*;
import java.util.*;

public class admin_home {
	
	static Scanner sc = new Scanner(System.in).useDelimiter("\\n");

	public static void adminHome(Connection conn, int personid) {
		try {
			System.out.println("-----Welcome Admin------");
			System.out.println("1. View own profile");
			System.out.println("2. Enroll new student");
			System.out.println("3. View Student Details");
			System.out.println("4. View/Add courses");
			System.out.println("5. View/Add course offering");
			System.out.println("6. View/Approve Special Enrollment Requests");
			System.out.println("7. Enforce Add/Drop Deadline");
			System.out.println("8. Manage Waitlist");
			System.out.println("9. Logout");
			System.out.println("Enter your choice :-> ");
			int admin_choice = sc.nextInt();
			switch (admin_choice) {
			case 1:
				// View admin's own profile
				viewOwnProfile(conn, personid); // option1: to view own profile
				break;
			case 2:
				// enroll a new student
				enrollNewStudent(conn, personid);
				break;
			case 3:
				// Admin enters student ID to view Student details
				viewStudentDetails(conn, personid);
				break;
			case 4:
				// main menu for View/Add course
				menuViewAddCourse(conn,personid);
				break;
			case 5:
				menuViewAddClass(conn, personid);
				break;
			case 6:
				// Special permission requests
				specialEnrollmentReq(conn, personid);
				break;
			case 7:
				enforceDeadline(conn, personid);
				// dealing thing
				break;
			case 8:
				manageWaitlist(conn, personid);
				break;
			case 9:
				System.out.println("Loggin out..");
				TimeUnit.SECONDS.sleep(3);
				System.exit(0);
				break;
			default:
				// invalid option selected. Throw back to previous menu.
				break;

			}
		} catch(Exception ex) {
			System.out.print(ex);
		}
	}

	public static void viewOwnProfile(Connection conn, int personid) {
		try {
			System.out.println("View your own profile");
			System.out.println("Press 0 to go back");
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT FNAME,LNAME,TO_CHAR(DOB,'dd-MON-yyyy') as BIRTH,EMP_ID FROM ADMIN WHERE EMP_ID=?");
			stmt.setInt(1, personid);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				System.out.println("First Name :-> " + rs.getString("FNAME"));
				System.out.println("Last Name :-> " + rs.getString("LNAME"));
				System.out.println("D.O.B. :-> " + rs.getString("BIRTH"));
				System.out.println("Employee ID :-> " + rs.getString("EMP_ID"));
			}
			int choice = sc.nextInt();
			if (choice == 0) {
				adminHome(conn, personid);
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public static void enrollNewStudent(Connection conn, int personid)
			throws ParseException, SQLException, InterruptedException {
		try {
			System.out.println("----Enrolling New Student----");
			System.out.println("Enter Student ID :--> ");
			int stud_id = sc.nextInt();
			System.out.println("Enter Student's First Name :--> ");
			String stud_fname = sc.next();
			System.out.println("Enter Student's Last Name :--> ");
			String stud_lname = sc.next();
			System.out.println("Enter Student's D.O.B (dd-MON-yyyy) :--> ");
			String dob = sc.next();
			System.out.println("Enter Student's Email :--> ");
			String email = sc.next();
			System.out.println("Enter Student's Address :--> ");
			sc.nextLine();
			String address = sc.nextLine();
			System.out.println("Enter Student's Level(Graduate,Undergraduate) :--> ");
			String stud_level = sc.next();
			System.out.println("Enter Student's Residency Status(In State,Out Of State,International) :--> ");
			sc.nextLine();
			String stud_resi_status = sc.nextLine();
			System.out.println("Enter amound owned (if any) :--> ");
			float stud_amount = sc.nextFloat();
			PreparedStatement stmt = conn
					.prepareStatement("SELECT STUDENT_SPECIAL_ID FROM STUDENT_SPECIAL WHERE LVL=? AND RESIDENCY=?");
			stmt.setString(1, stud_level);
			stmt.setString(2, stud_resi_status);
			ResultSet rs = stmt.executeQuery();
			int special_id = 0;
			while (rs.next()) {
				special_id = rs.getInt("STUDENT_SPECIAL_ID");
			}
			stmt = conn.prepareStatement(
					"INSERT INTO STUDENT(SID,FNAME,LNAME,DOB,EMAIL,ADDRESS,STUDENT_SPECIAL_ID) VALUES(?,?,?,TO_DATE(?,'dd-MON-yyyy'),?,?,?)");
			stmt.setInt(1, stud_id);
			stmt.setString(2, stud_fname);
			stmt.setString(3, stud_lname);
			stmt.setString(4, dob);
			stmt.setString(5, email);
			stmt.setString(6, address);
			stmt.setInt(7, special_id);
			stmt.executeUpdate();

			stmt = conn.prepareStatement("INSERT INTO ACCOUNT(BILL_ID,BILLAMOUNT,SID) VALUES(ACCOUNT_SEQ.NEXTVAL,?,?)");
			stmt.setInt(2, stud_id);
			stmt.setFloat(1, stud_amount);
			stmt.executeUpdate();

			System.out.println("Student enrolled successfully");
			adminHome(conn, personid);
		} catch (Exception ex) {
			System.out.println("Student Not enrolled: " + ex);
			adminHome(conn, personid);
		}
	}

	public static void viewStudentDetails(Connection conn, int personid) {
		try {
			System.out.println("Enter Student ID :-> ");
			int stud_id = sc.nextInt();

			PreparedStatement stmt = conn.prepareStatement(
					"SELECT S.FNAME FNAME,S.LNAME LNAME,TO_CHAR(S.DOB,'dd-MON-yyyy') as BIRTH,SS.LVL LVL,SS.RESIDENCY RESIDENCY,A.BILLAMOUNT BILLAMOUNT,S.GPA GPA  "
							+ "FROM STUDENT S,ACCOUNT A,STUDENT_SPECIAL SS "
							+ "WHERE S.SID=A.SID AND S.STUDENT_SPECIAL_ID = SS.STUDENT_SPECIAL_ID " + "AND S.SID=?");
			stmt.setInt(1, stud_id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				viewStudentDetails(conn, personid);
			} else {
				rs = stmt.executeQuery();
				while (rs.next()) {
					System.out.println("First Name :-> " + rs.getString("FNAME"));
					System.out.println("Last Name :-> " + rs.getString("LNAME"));
					System.out.println("D.O.B. :-> " + rs.getString("BIRTH"));
					System.out.println("Student's Level :-> " + rs.getString("LVL"));
					System.out.println("Student's Residency :-> " + rs.getString("RESIDENCY"));
					System.out.println("Amount Owed :-> " + rs.getString("BILLAMOUNT"));
					System.out.println("GPA :-> " + rs.getString("GPA"));
				}
				System.out.println("Press 0 to go back.");
				System.out.println("Press 1 to enter grades");
				int choice = sc.nextInt();
				if (choice == 0) {
					adminHome(conn, personid);
				} else if (choice == 1) {
					addGrades(conn, personid, stud_id);
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
		// Run SQL query to validate if the particular Student ID exists in the
		// databse or not.
		// IF no then re-prompt to enter the student ID
		// Else successful-> Show all student details
		// After showing all student detail's user can press 0-go back or
		// 1-enter grades.
	}

	
	public static void menuViewAddCourse(Connection conn, int personid) throws ParseException, SQLException, InterruptedException {
		// main menu for view/add ccourse
		System.out.println("Select appropriate option");
		System.out.println("0. Go back to previous menu");
		System.out.println("1. View course");
		System.out.println("2. Add course");
		int course_choice=sc.nextInt();
		if(course_choice==0)adminHome(conn, personid);
		else if(course_choice==1)adminViewCourse(conn, personid);
		else if(course_choice==2)adminAddCourse(conn, personid);
		else{
			System.out.println("Incorrect option. Going back to Admin's home page");
			adminHome(conn, personid);
		}
	}
	
	public static void adminAddCourse(Connection conn, int personid) throws ParseException, SQLException, InterruptedException {
	// Admin enters the course details.
	System.out.println("1. Enter Course ID:-> ");
	//dount here for the course ID. Entering as CSC111 or just 111? confirm once
	String course_id=sc.next();
	System.out.println("2. Enter Course name:-> ");
	String course_title=sc.next();
	System.out.println("3. Enter Department name:-> ");
	String dept_name=sc.next();
	System.out.println("4. Enter Course Level (e.g Graduate,Undergraduate):-> ");
	String course_level=sc.next();
	System.out.println("5. Enter GPA requirement:->(e.g. 3.0)");
	float gpa_req=sc.nextFloat();
	System.out.println("6. Enter Pre-req courses separated by comma(If any):-> (e.g.CSC501,CSC502) ");
	
	//Prerequisite courses may take multiple inputs. If values are separated by commas(,) break
	//the string into values and update those in the database accordingly. if anyone is trying to
	//create a SQL statement for this will have to discuss this before creating.
	int pre_req = 0;
	sc.nextLine();
	String pre_req_courses=sc.nextLine();
	if(!pre_req_courses.trim().isEmpty()){
		pre_req = 1;
	}

	System.out.println("7. Enter if special approval required:->(0/1) ");
	int approval_required=sc.nextInt();
	int min_credit, max_credit;
	if(approval_required==1){
		//then only ask for min & max credits.
		System.out.println("8. Enter min_credit for the course:-> ");
		min_credit=sc.nextInt();
		System.out.println("9. Enter max_credit for the course:-> ");
		max_credit=sc.nextInt();
	}else{
		//enter the number of credits for that course.
		System.out.println("8. Enter credits for the course:-> ");
		max_credit=sc.nextInt();
		min_credit = max_credit;
	}

	try{
	PreparedStatement stmt1 = conn.prepareStatement("SELECT DID from department where DEPT_NAME=?");
	stmt1.setString(1, dept_name);
	ResultSet rs = stmt1.executeQuery();
	int DID=0;
	if(rs.next()){
		DID = (rs.getInt("DID"));		
	}
	PreparedStatement stmt = conn.prepareStatement("INSERT INTO COURSE(CID, TITLE, DID, SP_PERMISSION, PRE_REQ, LVL, "
			+ "MIN_CREDIT, MAX_CREDIT, GPA_REQ) VALUES(?,?,?,?,?,?,?,?,?)");
	stmt.setString(1, course_id);
	stmt.setString(2, course_title);
	stmt.setInt(3, DID);
	stmt.setInt(4, approval_required);
	stmt.setInt(5, pre_req);
	stmt.setString(6, course_level);
	stmt.setInt(7, min_credit);
	stmt.setInt(8, max_credit);
	stmt.setFloat(9, gpa_req);
	stmt.executeUpdate();
	if(pre_req==1){
		if(pre_req_courses.contains(","))
		{
			String[] prereq = pre_req_courses.split(",");
			for(int i=0;i<prereq.length;i++)
			{
				PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO PRE_REQ VALUES(?,?)");
				stmt2.setString(1,course_id);
				stmt2.setString(2, prereq[i]);
				stmt2.executeQuery();
			}
		}
		else
		{
			PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO PRE_REQ VALUES(?,?)");
			stmt2.setString(1,course_id);
			stmt2.setString(2, pre_req_courses);
			stmt2.executeQuery();
		}
	}


	System.out.println("Course added successfully");
	menuViewAddCourse(conn, personid);
	}
	catch(Exception ex)
	{
		System.out.println("course not added successfully. Error: "+ex);
		menuViewAddCourse(conn, personid);
	}
	//create a query here to add these values in the database table.
	//if query is successful, display success message and go back to previous menu.
	//if query is unsuccessful, display specific error message and go back to previous menu.
}
public static void adminViewCourse(Connection conn, int personid) throws SQLException, InterruptedException {
	// Admin enters the courseID and system shows all course details
	try{
		System.out.println("Enter the course ID:--> ");
		String course_id = sc.next();
		PreparedStatement stmt = conn.prepareStatement("SELECT LISTAGG(PRE_REQ_COURSES,',') WITHIN GROUP (ORDER BY CID) AS PREREQ FROM PRE_REQ WHERE CID=?");
		stmt.setString(1, course_id);
		ResultSet rs = stmt.executeQuery();
		String prereq="";
		if (rs.next()) {
			do{
				prereq = rs.getString("PREREQ");
			}while(rs.next());
		}
		
		stmt = conn.prepareStatement("SELECT CID,TITLE,DID,SP_PERMISSION,LVL,MIN_CREDIT,MAX_CREDIT,GPA_REQ FROM COURSE WHERE CID=?");
		stmt.setString(1, course_id);
		rs = stmt.executeQuery();
	//create a SQL query to fetch the course related data from database
	//and display all fields.Take care while printing credits of course. If course
	//has credit range display in the form of range else as single credit.
	while(rs.next()){
		System.out.println("CID: " +  rs.getString("CID"));
		System.out.println("TITLE: " +  rs.getString("TITLE"));
		System.out.println("DID: " +  rs.getInt("DID"));
		System.out.println("SP_PERMISSION: " +  rs.getInt("SP_PERMISSION"));
		System.out.println("PRE_REQ: " +  prereq);
		System.out.println("LVL: " +  rs.getString("LVL"));
		System.out.println("MIN_CREDIT: " +  rs.getInt("MIN_CREDIT"));
		System.out.println("MAX_CREDIT: " +  rs.getInt("MAX_CREDIT"));
		System.out.println("GPA_REQ: " +  rs.getFloat("GPA_REQ"));		
	}
	}
	catch(Exception ex){
		System.out.println("Course view is not available."+ex);
	}
	System.out.println("Enter 0 to go back to previous menu:-> ");
	int choice = sc.nextInt();
	if (choice==0)menuViewAddCourse(conn, personid);

}



	public static void menuViewAddClass(Connection conn, int personid) {
		try {
			System.out.println("Select appropriate option");
			System.out.println("0. Go back to previous menu");
			System.out.println("1. View Class");
			System.out.println("2. Add Class");
			int class_choice = sc.nextInt();
			if (class_choice == 0)
				adminHome(conn, personid);
			else if (class_choice == 1)
				adminViewClass(conn, personid);
			else if (class_choice == 2)
				adminAddClass(conn, personid);
			else {
				System.out.println("Incorrect option. Going back to Admin's home page");
				adminHome(conn, personid);
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public static void adminViewClass(Connection conn, int personid) {

		try {
			System.out.println("Enter the course ID");
			;
			String course_id = sc.next();
			System.out.println("Class ID (can be multiple for single course) :-> ");
			System.out.println("Semester ");
			System.out.println("faculty name:-> ");
			System.out.println("Days of the week (e.g. MW) :-> ");
			System.out.println("Class start time:-> ");
			System.out.println("Class end time:-> ");
			System.out.println("Class size:-> ");
			System.out.println("Wait list size:-> ");

			PreparedStatement stmt = conn.prepareStatement(
					"SELECT CLASS_ID,SEMESTER,FAC_NAME,DAYS,START_TIME,END_TIME,CAPACITY,WAITLIST_CAPACITY FROM"
							+ " CLASS WHERE CID=?");

			stmt.setString(1, course_id);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				System.out.println("Enter a valid course id");
				adminViewClass(conn, personid);
			} else {
				rs = stmt.executeQuery();
				while (rs.next()) {
					System.out.println("Class Id :-> " + rs.getString("CLASS_ID"));
					System.out.println("Semester :-> " + rs.getString("SEMESTER"));
					System.out.println("Faculty Name :-> " + rs.getString("FAC_NAME"));
					System.out.println("Days of the Week :-> " + rs.getString("DAYS"));
					System.out.println("Class Start Time :-> " + rs.getString("START_TIME"));
					System.out.println("Class End Time :-> " + rs.getString("END_TIME"));
					System.out.println("Class Size :-> " + rs.getString("CAPACITY"));
					System.out.println("Wait list size :-> " + rs.getString("WAITLIST_CAPACITY"));
				}
				System.out.println("Press 0 to go back.");
				int choice = sc.nextInt();
				if (choice == 0) {
					adminHome(conn, personid);
				}

			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public static void adminAddClass(Connection conn, int personid) {

		try {
			System.out.println("----Admin Enter Class Page----");

			System.out.println("Enter course ID (e.g. CSC111) :-> ");
			String course_id = sc.next();
			System.out.println("Enter semester (e.g. Fall2016):-> ");
			String sem = sc.next();
			System.out.println("Enter faculty name:-> ");
			String fac_name = sc.next();
			System.out.println("Enter Days of the week (e.g. MW) :-> ");
			String schedule = sc.next();
			System.out.println("Enter Class start time:-> ");
			String s_time = sc.next();
			System.out.println("Enter Class end time:-> ");
			String e_time = sc.next();
			System.out.println("Enter Class size:-> ");
			int class_size = sc.nextInt();
			System.out.println("Enter wait list size:-> ");
			int wait_size = sc.nextInt();

			PreparedStatement stmt = conn.prepareStatement("INSERT INTO CLASS(CLASS_ID,CID,SEMESTER,FAC_NAME,DAYS,"
					+ "START_TIME,END_TIME,CAPACITY,WAITLIST_CAPACITY) VALUES(CLASS_SEQ.NEXTVAL,?,?,?,?,?,?,?,?)");
			stmt.setString(1, course_id);
			stmt.setString(2, sem);
			stmt.setString(3, fac_name);
			stmt.setString(4, schedule);
			stmt.setString(5, s_time);
			stmt.setString(6, e_time);
			stmt.setInt(7, class_size);
			stmt.setInt(8, wait_size);
			stmt.executeUpdate();
			System.out.println("Class successfully added");
			System.out.println("Enter 0 to go back to previous menu:-> ");
			int choice = sc.nextInt();
			if (choice == 0)
				menuViewAddClass(conn, personid);
		} catch (Exception ex) {
			System.out.println("Cannot add class: " + ex);
			adminAddClass(conn, personid);
		}
	}
	
	public static void specialEnrollmentReq(Connection conn, int personid) throws SQLException {
		// No input is required here. Directly write the SQL query here and show
		// all the special enrollment request with only status
		// as "pending". Once all pending requests are displayed on screen.
		// Admin will select which one he wants to approve/Reject
		// we will increment the counter and then mark the appropriate request
		// with that status.
		// Please, write the query to display the requests first then we can
		// think of approving/rejecting it.
		PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM SPECIAL_PERMISSION WHERE STATUS='PENDING'");
		ResultSet rs=stmt1.executeQuery();
		System.out.println("Press 0 to go back to previous menu");
		System.out.println("Enter the SPCL_ID that you would like to approve/reject:->");
		while(rs.next()){
			System.out.print("Special permisison ID:->"+rs.getInt("SPCL_PERM_ID"));
			System.out.print("| SID:->"+rs.getInt("SID"));
			System.out.println("| Class_ID:->"+rs.getInt("CLASS_ID"));
		}
		System.out.println("Enter the SPCL_ID that you would like to approve/reject:->");
		int spcl_id=sc.nextInt();
		
		PreparedStatement globl_stmt = conn.prepareStatement(
				"SELECT * FROM GLOBAL_VAR");
		ResultSet rs1 = globl_stmt.executeQuery();
		String sem="";
		while(rs1.next()){
			System.out.println("SEMESTER->"+rs1.getString("SEMESTER"));
			sem=rs1.getString("SEMESTER");
		}
		//fetch the values of that special permission
		PreparedStatement stmt4 = conn.prepareStatement("SELECT * FROM SPECIAL_PERMISSION WHERE SPCL_PERM_ID =?");
		stmt4.setInt(1, spcl_id);
		ResultSet rs4=stmt4.executeQuery();
		int stud_id=0;
		int class_id=0;
		while(rs4.next()){
			stud_id=rs4.getInt("SID");
			class_id=rs4.getInt("CLASS_ID");
		}//end of values of that stmt
		System.out.println("Student id"+stud_id);
		System.out.println("Class ID"+class_id);
		System.out.println("Would like to reject/approve(0/1) special permission "+spcl_id+"? (e.g. 0,1)");
		int decision=sc.nextInt();
		
		if(decision==1){
			//request is approved:1
			//for that SID and class_ID change the status from pending to approved in enrollment
			//decrease the class size by 1
			System.out.println("Accept");
			PreparedStatement stmt7 = conn.prepareStatement("UPDATE ENROLLMENT SET STATUS='Enrolled' WHERE SID=? AND CLASS_ID=?"
					+ " AND STATUS='Pending' AND SEMESTER=?");
			stmt7.setInt(1, stud_id);
			stmt7.setInt(2,class_id );
			stmt7.setString(3, sem);
			stmt7.executeUpdate();
			System.out.println("Done..");
			DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Calendar calobj = Calendar.getInstance();
		       System.out.println(df.format(calobj.getTime()));
			PreparedStatement stmt8 = conn.prepareStatement("Update special_permission set status = 'APPROVED', APPROVED_BY=?"
					+ ", APPROVED_ON = ? "
					+ "WHERE SID=? AND CLASS_ID=? "
					+ " AND STATUS='PENDING'");
			stmt8.setInt(1, personid);
			stmt8.setString(2, String.valueOf(df.format(calobj.getTime())));
			stmt8.setInt(3, stud_id);
			stmt8.setInt(4,class_id );
			stmt8.executeUpdate();
			System.out.println("Status is approved");
		}else{
			//request is rejected:0
			//Start: for that SID and class_ID change the status from pending to approved in enrollment
			System.out.println("Reject");
			PreparedStatement stmt2 = conn.prepareStatement("UPDATE ENROLLMENT SET STATUS='Rejected' WHERE SID=? AND CLASS_ID=?"
					+ " AND STATUS='Pending' AND SEMESTER=?");
			stmt2.setInt(1, stud_id);
			stmt2.setInt(2,class_id );
			stmt2.setString(3, sem);
			stmt2.executeUpdate();
			System.out.println("Done..");
			//End: for that SID and class_ID change the status from pending to approved in enrollment
			//Start:delete that entry from special_permission table.
			PreparedStatement stmt5 = conn.prepareStatement("DELETE special_permission WHERE SID=? AND CLASS_ID=? "
					+ " AND STATUS='PENDING'");
			stmt5.setInt(1, stud_id);
			stmt5.setInt(2,class_id );
			stmt5.executeUpdate();
			//End::delete that entry from special_permission table.
		}
		System.out.println("Request status changed");
		System.out.println("Enter 0 to go back to previous menu");
		System.out.println("Enter 1 to approve/reject additional requests");
		int next_step=sc.nextInt();
		if(next_step==0)adminHome(conn,personid);
		else specialEnrollmentReq(conn,personid);
		
	}
	

	public static void addGrades(Connection conn, int personid, int studentid) {
		System.out.println("Entering grades for Student id:"+studentid);
		//view courses from enrollment for current student
		try {
			//Get current semester from global_var table
			PreparedStatement stmt = conn.prepareStatement("SELECT CLASS_ID,GRADE,SEMESTER FROM ENROLLMENT WHERE STATUS = ? AND SID = ?");
			stmt.setString(1,"Enrolled");
			stmt.setInt(2,studentid);
			
			ResultSet rs = stmt.executeQuery();
			boolean rsEmpty = true;
			while(rs.next()){ //for each enrolled class of that student
				rsEmpty = false;
				System.out.print("Class ID :-> " + rs.getInt("CLASS_ID"));	
				//get all the details from class id using the class table
					PreparedStatement stmt2 = conn.prepareStatement(
					"SELECT  CID FROM CLASS WHERE CLASS_ID=?");
					stmt2.setInt(1, rs.getInt("CLASS_ID"));
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
	
					} //closing for rs2	
				System.out.print("|  Grade :-> " + rs.getString("GRADE"));
				System.out.println("|  Semester :-> " + rs.getString("SEMESTER"));	
				System.out.println("-------------------------------------------------------------------------------------------------------");
			}//closing for rs
			if(rsEmpty){
				System.out.println("Student is not enrolled in any class.. Press 0 to go back to previous menu");
			}else{
				System.out.println("Select class id to enter/modify grade.. Press 0 to go back to previous menu");
			}
			
			int choice = sc.nextInt();
			if (choice == 0) {
				adminHome(conn, personid);
			}
			else{
				//for that classid, change the grade to the mentioned grade by admin
				System.out.println("Enter new grade: ");
				String newGrade = sc.next();
				stmt = conn.prepareStatement(
				"UPDATE ENROLLMENT SET GRADE = ? WHERE SID=? AND CLASS_ID=?");
				stmt.setString(1, newGrade);  //here give validation in database to limit the value
				stmt.setInt(2, studentid); //studentid passed as a parameter
				stmt.setInt(3, choice); //class id is entered as a choice 
				stmt.executeUpdate();
				System.out.println("Grades updated successfully");
				System.out.println("----------------------------------------------------------");
				//Procedure update_gpa will be called to update the GPA of student.
				CallableStatement cStmt = conn.prepareCall("{call update_gpa(?)}");
				cStmt.setInt(1, studentid);
				cStmt.registerOutParameter(1, java.sql.Types.NUMERIC);
				boolean he = cStmt.execute();
				addGrades(conn,personid,studentid);
			}
		} //closing for try
		catch (Exception ex) {
			System.out.println(ex);
		}

	}

	public static void enforceDeadline(Connection conn, int personid) throws SQLException{
		System.out.println("Do you want to enforce deadline?(Y/N): ");
		try{
			String input = sc.next();
			if(input.toLowerCase().equals("y")){
			PreparedStatement st = conn.prepareStatement("UPDATE global_Var SET deadline_enforced = ?");
			st.setInt(1, 1);
			st.executeUpdate();
			System.out.println("Successfully enforced the deadline.");
			System.out.println("Enter 0 to go back to previous menu:-> ");
			int choice = sc.nextInt();
			if (choice == 0);
				adminHome(conn, personid);
		}
		else{
			PreparedStatement st = conn.prepareStatement("UPDATE global_Var SET deadline_enforced = 0");
			st.executeUpdate();		
			System.out.println("Removed the deadline.");
			System.out.println("Enter 0 to go back to previous menu:-> ");
			int choice = sc.nextInt();
			if (choice == 0)
				adminHome(conn, personid);
		}
	}
	catch(Exception ex){
		System.out.println("Couldn't perform the action. Error: " + ex);
	}
	}


	public static void manageWaitlist(Connection conn, int personid) throws SQLException{
		try{
			PreparedStatement st = conn.prepareStatement("SELECT * FROM WAIT_LIST");
			ResultSet rs = st.executeQuery();
			int sid, class_id, approve;
			if(!rs.next()){
				System.out.println("No entries in the waitlist");
				adminHome(conn,personid);
			}
			else{
				while(rs.next()){
				System.out.println("SID: -->  " + rs.getInt("SID"));
				System.out.println("CLASS_ID: -->  " + rs.getInt("CLASS_ID"));
				}
				}
			System.out.println("Enter SID you want to approve/reject:");
			sid = sc.nextInt();
			System.out.println("Enter CLASS_ID you want to approve/reject:");
			class_id = sc.nextInt();
			System.out.println("Do you want to reject or approve(0/1)?:");
			approve = sc.nextInt();
			if(approve==0){
				PreparedStatement pst = conn.prepareStatement("update enrollment set status='Rejected' where "
						+ "sid = ? and class_id = ?");
				pst.setInt(1, sid);
				pst.setInt(2, class_id);
				pst.executeUpdate();
				System.out.println("Successfully rejected");
			}
			else{
				PreparedStatement pst1 = conn.prepareStatement("update enrollment set status='Enrolled' where "
						+ "sid = ? and class_id = ?");
				pst1.setInt(1, sid);
				pst1.setInt(2, class_id);
				int rs1 = pst1.executeUpdate();
//				System.out.println("number of rows affected: "+rs1);
				System.out.println("Successfully enrolled");
			}
			System.out.println("Enter 0 to go back, 1 to go to home page");
			int choice = sc.nextInt();
			if (choice == 1)
				adminHome(conn, personid);
			else
				manageWaitlist(conn,personid);
		}
	catch(Exception ex){
		System.out.println("Couldn't perform the action. Error: " + ex);
	}
	}



}
