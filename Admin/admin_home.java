package Admin;

import java.util.Scanner;

public class admin_home {
	
	static Scanner sc=new Scanner(System.in);
	
	public static void main(String[] args) {
		// This is the first method to be called after logged in as Admin.
		adminHome(); //call the Admin's home page
	}
	
	public static void adminHome(){
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
			viewOwnProfile(); //option1: to view own profile
			break;
		case 2:
			//enroll a new student
			enrollNewStudent();
			break;
		case 3:
			//Admin enters student ID to view Student details
			viewStudentDetails();
			break;
		case 4:
			System.out.println("in choice 4");
			//main menu for View/Add course
			menuViewAddCourse();
			break;
		case 5:
			System.out.println("in choice 5");
			
			break;
		case 6:
			System.out.println("in choice 6");
			
			break;
		case 7:
			System.out.println("in choice 7");
			
			break;
		case 8:
			System.out.println("in choice 8");
			
			break;
		
		default:
			
			break;
			
		}
		
		
	}
	
	public static void viewOwnProfile(){
		//method to View Admin's own profile.
		System.out.println("View your own profile");
		System.out.println("Press 0 to go back");
		//create a procedure here to fetch admin details from database
		
		System.out.println("First Name :-> ");
		System.out.println("Last Name :-> ");
		System.out.println("D.O.B. :-> ");
		System.out.println("Employee ID :-> ");
		int choice=sc.nextInt();
		if (choice==0) {
			
		}
		
	}

	public static void menuViewAddCourse() {
		// main menu for view/add ccourse
		System.out.println("Select appropriate option");
		System.out.println("0. Go back to previous menu");
		System.out.println("1. View course");
		System.out.println("2. Add course");
		int course_choice=sc.nextInt();
		if(course_choice==0)adminHome();
		else if(course_choice==1)adminViewCourse();
		else if(course_choice==2)adminAddCoure();
		else{
			System.out.println("Incorrect option. Going back to Admin's home page");
			adminHome();
		}
		
	}

	public static void adminAddCoure() {
		// TODO Auto-generated method stub
		
	}

	public static void adminViewCourse() {
		// TODO Auto-generated method stub
		
	}

	public static void viewStudentDetails() {
		// Admin can check student details
		System.out.println("Enter Student ID :-> ");
		int stud_id=sc.nextInt();
		
		//Run SQL query to validate if the particular Student ID exists in the databse or not.
		//IF no then re-prompt to enter the student ID
		//Else successful-> Show all student details
		//After showing all student detail's user can press 0-go back or 1-enter grades.
	}

	public static void enrollNewStudent() {
		// Enroll New Student function
		System.out.println("----Enrolling New Student----");
		System.out.println("Enter Student ID :--> ");
		int stud_id=sc.nextInt();
		System.out.println("Enter Student Name :--> ");
		String stud_name=sc.nextLine();
		System.out.println("Enter Student's First Name :--> ");
		String stud_fname=sc.nextLine();
		System.out.println("Enter Student's Last Name :--> ");
		String stud_lname=sc.nextLine();
		System.out.println("Enter Student's D.O.B (MM-DD-YYYY) :--> ");
		String dob=sc.nextLine();
		System.out.println("Enter Student's Level :--> ");
		String stud_level=sc.nextLine();
		System.out.println("Enter Student's Residency Status :--> ");
		String stud_resi_status=sc.nextLine();
		System.out.println("Enter amound owned (if any) :--> ");
		float stud_amount=sc.nextFloat();
		
		//create a SQL query to enter all these data into the table and get the status if
		//data was successfully inserted into the table or not. if successful-> display success message
		//and go back to previous menu, else show error message and go back to previous menu
		
	}

}
