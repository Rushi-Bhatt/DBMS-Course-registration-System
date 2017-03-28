package Login;

import java.sql.*;
import java.util.Scanner;
import Admin.*;
import Connection.*;


public class Login {
	public static void main(String[] args) throws Exception
	{
		loginmenu();
	}
	
	public static void loginmenu() throws Exception {
		
		Connection conn = DBConnection.ConnectDB();
		Scanner scan = new Scanner(System.in);
		while(true)
		{
			System.out.println("---------Student Registration---------");
			System.out.println("1. Login");
			System.out.println("2. Exit");
	        System.out.println("Enter your choice :-> ");
			int select = scan.nextInt();
			if(select == 1)
			{
				userCheck(conn);
			}
			else
			{
				if(select !=2){
					System.out.println("Enter a valid number");
					loginmenu();
				}
				System.exit(0);
			}
		}
	}
	
	public static boolean userCheck(Connection conn)throws Exception
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter Username");
		String user = scan.nextLine();
		System.out.println("Enter Password");
		String pwd = scan.nextLine();
		PreparedStatement stmt = conn.prepareStatement("SELECT USERNAME,PASSWORD,ROLE,PERSON_ID FROM USER_LOGIN WHERE USERNAME=?");
		stmt.setString(1, user);
		ResultSet rs = stmt.executeQuery();
		if (!rs.next()) {
			System.out.printf("Login Incorrect.\n");
			return false;
		}
		String data_pwd = rs.getString("PASSWORD");
        String role = rs.getString("ROLE");
        System.out.println("Value of role is"+role);
        String username = rs.getString("USERNAME");
        int personid = rs.getInt("PERSON_ID");
        if (!pwd.equals(data_pwd)) 
        {
            System.out.println("Login Incorrect.");
            return false;
        }
        if(role.equals("A"))
        {
        	System.out.println("Welcome! Here "+username);
        	admin_home.adminHome(conn,personid);
        }
        else
        {
        	System.out.println("Welcome! "+username);
        }
        return true;
	}
}