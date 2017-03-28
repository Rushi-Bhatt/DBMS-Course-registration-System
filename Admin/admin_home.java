package Admin;

import java.util.Scanner;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import java.sql.*;

public class admin_home {
	
	static Scanner sc=new Scanner(System.in).useDelimiter("\\n");;
	
		
	public static void adminHome(Connection conn, int personid) throws ParseException, SQLException{
		System.out.println("-----Welcome Admin------");
		System.out.println("1. View own profile");
		System.out.println("2. Enroll new student");
		System.out.println("3. View Student Details");
		System.out.println("4. View/Add courses");
		System.out.println("5. View/Add course offering");
		System.out.println("6. View/Approve Special Enrollment Requests");
		System.out.println("7. Enforce Add/Drop Deadline");
		System.out.println("8. Logout");
		System.out.println("Enter your choice :-> ");
		int admin_choice = sc.nextInt();
		switch(admin_choice){
		case 1:
			//View admin's own profile
			viewOwnProfile(conn,personid); //option1: to view own profile
			break;
		case 2:
			//enroll a new student
			enrollNewStudent(conn,personid);
			break;
		case 3:
			//Admin enters student ID to view Student details
			viewStudentDetails(conn,personid);
			break;
		case 4:
			//main menu for View/Add course
//			menuViewAddCourse();
			break;
		case 5:
//			menuViewAddClass();
			//View
			break;
		case 6:
			System.out.println("in choice 6");
			
			break;
		case 7:
			System.out.println("in choice 7");
			
			break;
		case 8:
			break;
		
		default:
			
			break;
			
		}
		
		
	}
	
//	public static void menuViewAddClass() {
//		System.out.println("Select appropriate option");
//		System.out.println("0. Go back to previous menu");
//		System.out.println("1. View Class");
//		System.out.println("2. Add Class");
//		int class_choice=sc.nextInt();
//		if(class_choice==0)adminHome();
//		else if(class_choice==1)adminViewClass();
//		else if(class_choice==2)adminAddClass();
//		else{
//			System.out.println("Incorrect option. Going back to Admin's home page");
//			adminHome();
//		}
//		
//	}

//	public static void adminAddClass() {
//		// TODO Auto-generated method stub
//		System.out.println("----Admin Enter Class Page----");
//		//we need class_id entry while creating the class. It will be created in the background 
//		//using sequence and qutomatically placed in the column "class_id" of table class.
//		//Check with Zankruti for more details.
//		System.out.println("Enter course ID (e.g. CSC111) :-> ");
//		String class_id=sc.nextLine();
//		System.out.println("Enter semester (e.g. Fall2016):-> ");
//		System.out.println("Enter faculty name:-> ");
//		System.out.println("Enter Days of the week (e.g. MW) :-> ");
//		System.out.println("Enter Class start time:-> ");
//		System.out.println("Enter Class end time:-> ");
//		System.out.println("Enter Class size:-> ");
//		System.out.println("Enter wait list size:-> ");
//		
//		//if everything successful, press 0 to go back to previous menu, else show the error message
//		//and go back to previous menu.
//		
//		
//	}

//	public static void adminViewClass() {
//		// TODO Auto-generated method stub
//		
//	}

//	public static void menuViewAddCourse() {
//		// main menu for view/add ccourse
//		System.out.println("Select appropriate option");
//		System.out.println("0. Go back to previous menu");
//		System.out.println("1. View course");
//		System.out.println("2. Add course");
//		int course_choice=sc.nextInt();
//		if(course_choice==0)adminHome();
//		else if(course_choice==1)adminViewCourse();
//		else if(course_choice==2)adminAddCoure();
//		else{
//			System.out.println("Incorrect option. Going back to Admin's home page");
//			adminHome();
//		}
//		
//	}

//	public static void adminAddCoure() {
//		// Admin enters the course details.
//		System.out.println("1. Enter Course ID:-> ");
//		//dount here for the course ID. Entering as CSC111 or just 111? confirm once
//		String course_id=sc.next();
//		System.out.println("2. Enter Course name:-> ");
//		String course_title=sc.next();
//		System.out.println("3. Enter Department name:-> ");
//		String dept_name=sc.next();
//		System.out.println("4. Enter Course Level:-> ");
//		String course_level=sc.next();
//		System.out.println("5. Enter GPA requirement:-> ");
//		float gpa_req=sc.nextFloat();
//		System.out.println("6. Enter Pre-req courses:-> ");
//		
//		//Prerequisite courses may take multiple inputs. If values are seperated by commas(,) break
//		//the string into values and update those in the database accordingly. if anyone is trying to
//		//create a SQL statement for this will have to discuss this before creating.
//		
//		String pre_req_courses=sc.next();
//		System.out.println("7. Enter if spacial approval required:->(0/1) ");
//		int approval_required=sc.nextInt();
//		System.out.println("8. Are credits as a Range of single credit(Y/N):-> ");
//		String range=sc.next();
//		if(range.equals("Y")){
//			System.out.println("Enter min_credit for the course:-> ");
//			float min_credit=sc.nextFloat();
//			System.out.println("Enter max_credit for the course:-> ");
//			float max_credit=sc.nextFloat();
//		}else{
//			System.out.println("Enter credits for the course:-> ");
//			float course_credit=sc.nextFloat();
//			//this is the single credit value for the course, while updating in the database please
//			//enter this value in both the min_credit and max_credit in the course table database.
//		}
//		//create a query here to add these values in the database table.
//		//if query is successful, display success message and go back to previous menu.
//		//if query is unsuccessful, display specific error message and go back to previous menu.
//	}
//	
//	public static void adminViewCourse() {
//		// Admin enters the courseID and system shows all course details
//		System.out.println("Enter the course ID:--> ");
//		String course_id = sc.nextLine();
//		
//		//create a SQL query to fetch the course related data from database
//		//and display all fields.Take care while printing credits of course. If course
//		//has credit range display in the form of range else as single credit.
//		System.out.println("Enter 0 to go back to previous menu:-> ");
//		int choice = sc.nextInt();
//		if (choice==0)menuViewAddCourse();
//
//	}

	public static void viewOwnProfile(Connection conn,int personid) throws SQLException, ParseException{
		System.out.println("View your own profile");
		System.out.println("Press 0 to go back");
		PreparedStatement stmt = conn.prepareStatement("SELECT FNAME,LNAME,TO_CHAR(DOB,'dd-MON-yyyy') as BIRTH,EMP_ID FROM ADMIN WHERE EMP_ID=?");
		stmt.setInt(1, personid);
		ResultSet rs = stmt.executeQuery();
		while(rs.next())
		{
			System.out.println("First Name :-> "+rs.getString("FNAME"));
			System.out.println("Last Name :-> "+rs.getString("LNAME"));
			System.out.println("D.O.B. :-> "+rs.getString("BIRTH"));
			System.out.println("Employee ID :-> "+rs.getString("EMP_ID"));
		}
		int choice=sc.nextInt();
		if (choice==0) {
			adminHome(conn,personid);
		}
	}
	
	public static void enrollNewStudent(Connection conn,int personid) throws ParseException, SQLException{
		try
		{
			System.out.println("----Enrolling New Student----");
			System.out.println("Enter Student ID :--> ");
			int stud_id=sc.nextInt();
			System.out.println("Enter Student's First Name :--> ");
			String stud_fname=sc.next();
			System.out.println("Enter Student's Last Name :--> ");
			String stud_lname=sc.next();
			System.out.println("Enter Student's D.O.B (dd-MON-yy) :--> ");
			String dob=sc.next();
			System.out.println("Enter Student's Email :--> ");
			String email=sc.next();
			System.out.println("Enter Student's Address :--> ");
			String address=sc.next();
			System.out.println("Enter Student's Level(Graduate,Undergraduate) :--> ");
			String stud_level=sc.next();
			System.out.println("Enter Student's Residency Status(In State,Out Of State,International) :--> ");
			String stud_resi_status=sc.next();
			System.out.println("Enter amound owned (if any) :--> ");
			float stud_amount=sc.nextFloat();
			PreparedStatement stmt = conn.prepareStatement("SELECT STUDENT_SPECIAL_ID FROM STUDENT_SPECIAL WHERE LVL=? AND RESIDENCY=?");
			stmt.setString(1, stud_level);
			stmt.setString(2, stud_resi_status);
			ResultSet rs = stmt.executeQuery();
			int special_id=0;
			while(rs.next())
			{
				special_id = rs.getInt("STUDENT_SPECIAL_ID");
			}
			stmt = conn.prepareStatement("INSERT INTO STUDENT(SID,FNAME,LNAME,DOB,EMAIL,ADDRESS,STUDENT_SPECIAL_ID) VALUES(?,?,?,TO_DATE(?,'dd-MON-yy'),?,?,?)");
			stmt.setInt(1, stud_id);
			stmt.setString(2,stud_fname);
			stmt.setString(3,stud_lname);
			stmt.setString(4,dob);
			stmt.setString(5,email);
			stmt.setString(6,address);
			stmt.setInt(7, special_id);
			stmt.executeUpdate();
			
			stmt = conn.prepareStatement("INSERT INTO ACCOUNT(BILL_ID,BILLAMOUNT,SID) VALUES(ACCOUNT_SEQ.NEXTVAL,?,?)");
			stmt.setInt(2, stud_id);
			stmt.setFloat(1,stud_amount);
			stmt.executeUpdate();
			
			System.out.println("Student enrolled successfully");
			adminHome(conn,personid);
		}catch(Exception ex)
		{
			System.out.println("Student Not enrolled: "+ex);
			adminHome(conn,personid);
		}
	}
	public static void viewStudentDetails(Connection conn,int personid) throws SQLException {
		// Admin can check student details
		System.out.println("Enter Student ID :-> ");
		int stud_id=sc.nextInt();
		
		PreparedStatement stmt = conn.prepareStatement("SELECT S.FNAME FNAME,S.LNAME LNAME,TO_CHAR(S.DOB,'dd-MON-yyyy') as BIRTH,SS.LVL LVL,SS.RESIDENCY RESIDENCY,A.BILLAMOUNT BILLAMOUNT,S.GPA GPA  "
														+ "FROM STUDENT S,ACCOUNT A,STUDENT_SPECIAL SS "
														+ "WHERE S.SID=A.SID AND S.STUDENT_SPECIAL_ID = SS.STUDENT_SPECIAL_ID "
														+ "AND S.SID=?");
		stmt.setInt(1, stud_id);
		ResultSet rs = stmt.executeQuery();
		if (!rs.next()) {
			viewStudentDetails(conn,personid);
		}
		else
		{
			rs = stmt.executeQuery();
			while(rs.next())
			{
				System.out.println("First Name :-> "+rs.getString("FNAME"));
				System.out.println("Last Name :-> "+rs.getString("LNAME"));
				System.out.println("D.O.B. :-> "+rs.getString("BIRTH"));
				System.out.println("Student’s Level :-> "+rs.getString("LVL"));
				System.out.println("Student's Residency :-> "+rs.getString("RESIDENCY"));
				System.out.println("Amount Owed :-> "+rs.getString("BILLAMOUNT"));
				System.out.println("GPA :-> "+rs.getString("GPA"));
			}
			System.out.println("Press 0 to go back.");
			System.out.println("Press 1 to enter grades");
			int choice=sc.nextInt();
			if (choice==0) {
				adminHome(conn,personid);
			}
			else if(choice==1)
			{
				addGrades(conn,personid);
			}
		}
		//Run SQL query to validate if the particular Student ID exists in the databse or not.
		//IF no then re-prompt to enter the student ID
		//Else successful-> Show all student details
		//After showing all student detail's user can press 0-go back or 1-enter grades.
	}
	
	public static void addGrades(Connection conn,int personid)
	{
		System.out.println("Press course number to add grades");
	}
}
