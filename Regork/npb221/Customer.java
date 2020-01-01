/*
 * Nico Beard
 * CSE 241
 * Customer Interface
*/

import java.util.Scanner;
import java.sql.*;
import java.io.*;

public class Customer
{
	public static void customerInterface(Connection connection) throws SQLException, IOException, java.lang.ClassNotFoundException
	{
    try
    {
    Scanner input = new Scanner(System.in);
    System.out.println("Hello, please enter your supplier ID: ");
    String supplier = supID(input, connection);
    if(!(supplier.equals("quit")))
    {
      
      //give them 4 options
      try
      {
      boolean test = false;
      do
      {
          int option = 0;
          System.out.println("\nWhat would you like to do?");
          System.out.println("Enter 1 to buy products");
          System.out.println("Enter 2 to check inventory levels");
          System.out.println("Enter 3 to check shipments of items bought");
          System.out.println("Enter 4 to quit");
          while(!input.hasNextInt())
          {
              input.next();
              System.out.println("Incorrect input, try again");
          }
          option = input.nextInt();

          //go to their selected options
          switch(option)
          {
            case 1:
              product(connection, supplier);
              System.out.println("\nItem has been bought, you can see it in your inventory levels");
              break;
            case 2:
              inventory(connection, supplier);
              break;
            case 3:
              shipment(connection, supplier);
              break;
            case 4:
              test = true;
              break;
            default:
              System.out.println("Incorrect input, try again");
          }
        } while(!test);
      }
      catch(Exception e)
      {
        System.out.println("Incorrect input");
      }
    }
  }
  catch(Throwable throwable)
  {
    System.out.println("Incorrect input");
  }
	}
  
  //verify supplier ID
  public static String supID(Scanner input, Connection connection) throws SQLException, IOException, java.lang.ClassNotFoundException
  {
    String supplier = "";
    try
    {
    supplier = input.nextLine();
    //get all the ids in the database
    Statement statement = connection.createStatement();
    String query = "select * from supplier";
    ResultSet result = null;
		boolean correct = false;
		boolean results = false;
    String id = "";
    boolean match = false;
    
    try
    {
    do
    {
      try
      {
        result = statement.executeQuery(query);
        if(!result.next())
        {
          System.out.println("That entity doesn't exist");
        }
        else
        {
          System.out.println("\nHere is a list of the current ID's in the database: \n");
          System.out.println("Supply_ID Name                 Address");
          do
          {
            System.out.println(String.format("%-9s %-20s %-30s", result.getString("Supply_ID"), result.getString("Name"), result.getString("Address")));
            
            id = result.getString("supply_id");
            if(supplier.equals(id))//check if their input is a valid supplier in the database
            {
              correct = true;
              match = true;
            }
          }
          while(result.next());
        }
      }
      catch(Exception e)
      {
        System.out.println("Unable to output supplier table");
      }

      //check to see if the user is trying to mess with the database
      if(supplier.contains(";") || supplier.contains("\'"))
      {
        correct = false;
      }
      
      //check to see if the id they entered is valid, offering them a way out if they want
      if(!match)
      {
        Scanner userInput = new Scanner(System.in);
        System.out.println("\nHmm, the ID you entered doesn't match our records. You can enter a new ID to try again or enter \"quit\" to quit");
        supplier = input.nextLine();
      }
      
      //exit the method if they decide to quit
      if(supplier.equals("quit"))
      {
        return supplier;
      }
      
    } while(!correct);
  }
  catch(Throwable throwable)
  {
    System.out.println("Incorrect Input");
  }
    
    //if they inputted a valid supplier, allow them to move on
    if(match)
    {
      return supplier;
    }
    
  }
    catch(Throwable throwable)
    {
      System.out.println("Incorrect input");
    }
    return supplier;
  }
  
  //allow customer to buy products
  public static void product(Connection connection, String supplier) throws SQLException, IOException, java.lang.ClassNotFoundException
  {
    try
    {
    Scanner userInput = new Scanner(System.in);
    ResultSet result = null;
    String query = "select product.prod_id, supplier.supply_id, item.name, item.base_size, product.price from item inner join specifies on specifies.item_id = item.item_id inner join product on specifies.prod_id = product.prod_id inner join inventory on product.prod_id = inventory.prod_id inner join supplier on supplier.supply_id = inventory.supply_id";
    Statement statement = connection.createStatement();
    boolean test = false;
    String testID = "";
    String prod_id = "";
    String previousSupplier = "";
    
    System.out.println("Enter the product ID of the item that you want to purchase:");
    prod_id = userInput.nextLine();
    do
    {
      try
      {
        result = statement.executeQuery(query);
        if(!result.next())
        {
          System.out.println("There are no products");
        }
        else
        {
          System.out.println("\nHere is a list of the current products in the database: \n");
          System.out.println("Product_ID Suppier_ID Name    		             Base_size Price");
          do
          {
            if(testID.equals(prod_id))
            {
              test = true;
              previousSupplier = result.getString("supply_id");
            }
            System.out.println(String.format("%-10s %-10s %-30s %-9s %-8s", result.getString("prod_id"), result.getString("supply_id"), result.getString("Name"), result.getString("Base_size"), result.getString("Price")));
            testID = result.getString("prod_id");
          } while(result.next());
        }
      }
      catch(Exception e)
      {
        System.out.println("Incorrect entity inputted");
      }
      
      //check to see if the id they entered is valid, offering them a way out if they want
      if(!test)
      {
        System.out.println("\nHmm, the product you entered doesn't match our records. You can enter a new product ID to try again or enter \"quit\" to quit");
        prod_id = userInput.nextLine();
      }

      //check to see if the user is trying to mess with the database
      if(prod_id.contains(";") || prod_id.contains("\'"))
      {
        System.out.println("Hmm it seems you're trying to do something that could mess with the database");
        test = false;
      }
      
      //exit the method if they decide to quit
      if(prod_id.equals("quit"))
      {
        return;
      }
      
    } while(!test);
    
    //delete the product from the last supplier it was associated with
    query = "delete from inventory where prod_id = \'" + prod_id + "\'";
    query(connection, query, "Cannot remove last owner");
    
    //insert the new product into the supplier's inventory
    query = "insert into Inventory (supply_id, prod_id) values (\'" + supplier + "\', \'" + prod_id + "\')";
    query(connection, query, "Couldn't insert product");
    
    //update the shipments
    int month = (int)(Math.random() * ((12 - 0) + 1)) + 0;
    int day = (int)(Math.random() * ((31 - 0) + 1)) + 0;
    int year = (int)(Math.random() * ((2019 - 2000) + 1)) + 2000;
    int ship_id = (int)(Math.random() * ((99999999 - 0) + 1)) + 0;
    double amount = (Math.random() * ((10 - 0) + 1)) + 0;
    amount = Math.round(amount * 100.0) / 100.0;
    double price = (Math.random() * ((99999 - 0) + 1)) + 0;
    price = Math.round(amount * 100.0) / 100.0;

    query = "insert into Shipment (ship_id, amount, occurrence) values (\'" + ship_id + "\', \'$" + amount + "\', \'" + month + "/" + day + "/" + year + "\')";
    query(connection, query, "Couldn't add shipment");
    
    //update the supply_to table
    query = "insert into Supply_to (supply_id, ship_id) values (\'" + supplier + "\', \'" + ship_id + "\')";
    query(connection, query, "Couldn't add to supply_to table");
    
    //update the supply_from table
    query = "insert into Supply_from (supply_id, ship_id) values (\'" + previousSupplier + "\', \'" + ship_id + "\')";
    query(connection, query, "Couldn't add to supply_from table");

    query = "delete from contains where prod_id = \'" + prod_id + "\'";
    query(connection, query, "Cannot remove shipment");

    query = "insert into contains (ship_id, prod_id, price) values (\'" + ship_id + "\', \'" + prod_id + "\', \'" + price + "\')";
    query(connection, query, "Couldn't add to contains");
  }
  catch(Throwable throwable)
  {
    System.out.println("Incorrect input");
  }
  }
  //allow customer to check inventory levels
  public static void inventory(Connection connection, String supplier) throws SQLException, IOException, java.lang.ClassNotFoundException
  {
    ResultSet result = null;
    String query = "select supplier.supply_id, supplier.name as Supplier, item.name as Product, product.price, item.base_size from supplier inner join inventory on supplier.supply_id = inventory.supply_id inner join product on product.prod_id = inventory.prod_id inner join specifies on product.prod_id = specifies.prod_id inner join item on item.item_id = specifies.item_id where supplier.supply_id = \'" + supplier + "\'";
    boolean test = false;
    
    try
    {
      result = query(connection, query, "Couldn't load inventory");
      if(!result.next())
      {
        System.out.println("You have no inventory right now");
      }
      else
      {
        System.out.println("\nHere are your current inventory levels: \n");
        System.out.println("Supply_ID Supplier Product                        Price    Base_size");
        do
        {
          System.out.println(String.format("%-9s %-8s %-30s %-8s %-8s", result.getString("supply_id"), result.getString("supplier"), result.getString("product"), result.getString("price"), result.getString("base_size")));
        } while(result.next());
      }
    }
    catch(Exception e)
    {
      System.out.println("Unable to load inventory");
    }
  }
  
  //allow customer to check shipments of items they;ve bought
  public static void shipment(Connection connection, String supplier) throws SQLException, IOException, java.lang.ClassNotFoundException
  {
    Statement statement = connection.createStatement();
    ResultSet result = null;
    String query = "select shipment.amount, shipment.occurrence, item.name as Product, contains.price, item.base_size from supplier inner join supply_to on supply_to.supply_id = supplier.supply_id inner join shipment on supply_to.ship_id = shipment.ship_id inner join contains on shipment.ship_id = contains.ship_id inner join product on product.prod_id = contains.prod_id inner join specifies on specifies.prod_id = product.prod_id inner join item on item.item_id = specifies.item_id where supplier.supply_id = \'" + supplier + "\'";
    boolean test = false;
    
    try
    {
      result = statement.executeQuery(query);
      if(!result.next())
      {
        System.out.println("You currently don't have any shipments");
      }
      else
      {
        System.out.println("\nHere are your current shipments: \n");
        System.out.println("Amount  Occurrence Product                        Price    Base_size");
        do
        {
          System.out.println(String.format("%-7s %-10s %-30s %-8s %-8s", result.getString("amount"), result.getString("occurrence"), result.getString("Product"), result.getString("price"), result.getString("base_size")));
        } while(result.next());
      }
    }
    catch(Exception e)
    {
      System.out.println("Unable to load shipments");
    }
  }
  
  public static ResultSet query(Connection connection, String query, String error)
  {
    try
    {
      Statement statement = connection.createStatement();
      return statement.executeQuery(query);
    }
    catch(Exception e)
    {
      System.out.println(error);
    }
    return null;
  }
}