package Admin;

import java.util.Scanner;

public class admin_home {
	
	static Scanner sc=new Scanner(System.in);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		adminHome(); //call the Admin's home page
		
		
	}
	
	public static void viewOwnProfile(){
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
			viewOwnProfile(); //option1: to view own profile
			break;
		case 2:
			System.out.println("in choice 2");
			enrollNewStudent();
			//enroll a new student
			break;
		case 3:
			System.out.println("in choice 3");
			
			break;
		case 4:
			System.out.println("in choice 4");
			
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

	public static void enrollNewStudent() {
		// Enroll New Student function
		
		
	}

}
