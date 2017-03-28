package Admin;

import java.util.Scanner;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import java.util.concurrent.TimeUnit;
import java.sql.*;

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
			System.out.println("8. Logout");
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
				// menuViewAddCourse(conn,personid);
				break;
			case 5:
				menuViewAddClass(conn, personid);
				break;
			case 6:
				// Special permission requests
				specialEnrollmentReq();
				break;
			case 7:
				System.out.println("in choice 7");
				// dealing thing
				break;
			case 8:
				System.out.println("Loggin out...");
				TimeUnit.SECONDS.sleep(3);
				break;
			default:
				// invalid option selected. Throw back to previous menu.
				break;

			}
		} catch (Exception ex) {
			System.out.println(ex);
		}

	}

	public static void specialEnrollmentReq() {
		// No input is required here. Directly write the SQL query here and show
		// all the special enrollment request with only status
		// as "pending". Once all pending requests are displayed on screen.
		// Admin will select which one he wants to approve/Reject
		// we will increment the counter and then mark the appropriate request
		// with that status.
		// Please, write the query to display the requests first then we can
		// think of approving/rejecting it.

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
			System.out.println("Enter Student's D.O.B (dd-MON-yy) :--> ");
			String dob = sc.next();
			System.out.println("Enter Student's Email :--> ");
			String email = sc.next();
			System.out.println("Enter Student's Address :--> ");
			String address = sc.next();
			System.out.println("Enter Student's Level(Graduate,Undergraduate) :--> ");
			String stud_level = sc.next();
			System.out.println("Enter Student's Residency Status(In State,Out Of State,International) :--> ");
			String stud_resi_status = sc.next();
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
					"INSERT INTO STUDENT(SID,FNAME,LNAME,DOB,EMAIL,ADDRESS,STUDENT_SPECIAL_ID) VALUES(?,?,?,TO_DATE(?,'dd-MON-yy'),?,?,?)");
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
					System.out.println("Student’s Level :-> " + rs.getString("LVL"));
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
					addGrades(conn, personid);
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

	public static void addGrades(Connection conn, int personid) {
		System.out.println("Press course number to add grades");
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
}
