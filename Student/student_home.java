package Student;

import java.sql.Connection;
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

	static Scanner sc=new Scanner(System.in).useDelimiter("\\n");
	
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
			//View all available courses
			viewAllCourses(conn,personid);
			break;
		case 4:
			//Enroll courses
			enrollCourse(conn,personid);
			break;
		case 5:
			//Drop course
		
			//View
			break;
		case 6:
			//View all my courses
		
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
		
	}
	
	public static void editOwnProfile(Connection conn, int personid) {
		// TODO Auto-generated method stub
		
		
	}
	public static void viewAllCourses(Connection conn, int personid) {
		// Show all courses for current semester.
		
	}

}
