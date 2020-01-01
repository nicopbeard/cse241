/*
 * Nico Beard
 * CSE 241
 * Main Interface
*/

import java.util.Scanner;
import java.sql.*;
import java.io.*;

public class Regork
{
	public static void main(String[] args) throws SQLException, IOException, java.lang.ClassNotFoundException
	{
		try
		{
		//establish connection and make user enter username and password
		Connection connection = null;
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		boolean test = false;
		Scanner input = new Scanner(System.in);
		while(!test)
		{
			System.out.println("Enter Username: ");
			String username = input.nextLine();
			System.out.println("Enter Password: ");
			String password = input.nextLine();

			test = true;
			try
			{
				connection = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username, password);
			}
			catch(Exception e)
			{
				System.out.println("Incorrect username or password");
				test = false;
			}
		}

		System.out.println("Welcome to Regork Enterprise");
		System.out.println("-----------------------------");

		//determine whther user is manager or customer
		test = false;
		input = new Scanner(System.in);
		String profile = "";
		do
		{
				System.out.println("Are you a customer, manager, or executive? (Enter \"quit\" to quit)");
				profile = input.nextLine();
				profile = profile.toLowerCase();

				//load respective interface based on user input
				if(profile.equals("customer"))
				{
					Customer.customerInterface(connection);
				}
				else if(profile.equals("manager"))
				{
					Manager.managerInterface(connection);
				}
				else if(profile.equals("executive"))
				{
					Executive.executiveInterface(connection);
				}
				else if(profile.equals("quit"))
				{
					test = true;
				}
				else
				{
					System.out.println("Incorrect input, please try again");
				}
    } while(!test);
		connection.close();
	}
	catch(Throwable throwable)
	{
		System.out.println("Incorrect input");
	}
	}
}