/*
 * Nico Beard
 * CSE 241
 * Executive Interface
*/

import java.util.Scanner;
import java.sql.*;
import java.io.*;

public class Executive
{
	public static void executiveInterface(Connection connection) throws SQLException, IOException, java.lang.ClassNotFoundException
	{
    try
    {
		boolean test = false;
    int option = 0;
    String supID = "";
    String name = "Regork";
    
    //retain Regork's supplier ID
    String query = "select * from supplier";
    try
    {
      ResultSet result = query(connection, query, "Couldn't get ID");
      if(!result.next())
      {
        System.out.println("You currently don't have any shipments");
      }
      else
      {
        do
        {
          if((result.getString("Name")).equals(name))
          {
            supID = result.getString("supply_id");
          }
        } while (result.next());
      }
    }
    catch(Exception e)
    {
      System.out.println("Couldn't get ID");
    }
    
    do
    {
      Scanner input = new Scanner(System.in);
      System.out.println("\nWelcome to the Executive Interface");
      System.out.println("Enter 1 to add a new supplier");
      System.out.println("Enter 2 to see the purchases of a specific supplier");
      System.out.println("Enter 3 to recall a product");
      System.out.println("Enter 4 to quit");
      try
      {
        option = input.nextInt();
        switch(option)
        {
          case 1:
            addSupplier(connection, supID);
            System.out.println("\nUpdated suppliers:");
            try
            {
              ResultSet result = query(connection, query, "Couldn't output suppliers");
              if(!result.next())
              {
                System.out.println("You currently don't have any shipments");
              }
              else
              {
                System.out.println("\nSupplier_ID  Name                 Address");
                do
                {
                  System.out.println(String.format("%-12s %-20s %-30s", result.getString("supply_id"), result.getString("Name"), result.getString("Address")));
                } while (result.next());
              }
            }
            catch(Exception e)
            {
              System.out.println("Couldn't load supplier table");
            }
            break;
          case 2:
            checkPurchase(connection);
            break;
          case 3:
            recall(connection);
            break;
          case 4:
            test = true;
            break;
          default:
            System.out.println("Incorrect input, try again");
        }
      }
      catch(Exception e)
      {
        System.out.println("Incorrect input");
      }
    } while(!test);
  }
  catch(Throwable throwable)
  {
      System.out.println("Incorrect input");
  }
	}
  
  //method to add supplier
  public static void addSupplier(Connection connection, String supID)
  {
    try
    {
    Scanner input = new Scanner(System.in);
    boolean test = false;
    String name = "";
    String query = "";
    String address = "";
    int newSupID = (int)(Math.random() * ((99999999 - 0) + 1)) + 0;
    System.out.println("Enter the name and address of the supplier you're adding");
    do
    {
      System.out.print("Name: ");
      name = input.nextLine();
      if(name.equals("quit"))
        return;
      System.out.print("Address: ");
      address = input.nextLine();
      
      if(name.length() > 30 || address.length() > 30)
      {
        System.out.println("One of the inputs you entered is incorrect, please try again or enter \"quit\" in the name entry to quit");
        continue;
      }

      //check to see if the user is trying to mess with the database
      if(name.contains(";") || name.contains("\'") || address.contains(";") || address.contains("\'"))
      {
          System.out.println("Hmm, you are trying to do something to do something to the database that is illegal");
          test = false;
          continue;
      }
      
      query = "select * from supplier";
      try
      {
        ResultSet result = query(connection, query, "Couldn't load supplier table");
        if(!result.next())
        {
          System.out.println("You currently don't have any shipments");
        }
        else
        {
          do
          {
            if(!((result.getString("Name")).equals(name) || (result.getString("Address")).equals(address)))
            {
              test = true;
            }
            else
            {
              System.out.println("One of the inputs you entered is incorrect, please try again or enter \"quit\" in the name entry to quit");
            }
          } while (result.next());
        }
      }
      catch(Exception e)
      {
        System.out.println("Couldn't load supplier table");
      }
    } while(!test);
    
    query = "insert into Supplier (supply_id, name, address) values (\'" + newSupID + "\', \'" + name + "\', \'" + address + "\')";
    query(connection, query, "Couldn't add supplier");
  }
  catch(Throwable throwable)
  {
      System.out.println("Incorrect input");
  }
  }
  
  //method to check the purchases of other suppliers
  public static void checkPurchase(Connection connection)
  {
    try
    {
    Scanner input = new Scanner(System.in);
    boolean test = false;
    String query = "select * from supplier";
    String supplierID = "";
    
    System.out.println("Enter the supplier ID for which you would like to see their purchases");
    do
    {
      supplierID = input.nextLine();
      try
      {
        ResultSet result = query(connection, query, "Couldn't load supplier table");
        if(!result.next())
        {
          System.out.println("There are no suppliers");
        }
        else
        {
          System.out.println("\nSupplier_ID  Name                 Address");
          do
          {
            System.out.println(String.format("%-12s %-20s %-30s", result.getString("supply_id"), result.getString("Name"), result.getString("Address")));
            if(result.getString("supply_id").equals(supplierID))
            {
              test = true;
            }
          } while (result.next());
        }
      }
      catch(Exception e)
      {
        System.out.println("Couldn't load supplier table");
      }
      System.out.println();
      if(!test)
      {
        System.out.println("The supplier ID you entered is invalid, please try again or enter \"quit\" to quit");
      }

      //check to see if the user is trying to mess with the database
      if(supplierID.contains(";") || supplierID.contains("\'"))
      {
          System.out.println("Hmm, you are trying to do something to do something to the database that is illegal");
          test = false;
      }
      
      if(supplierID.equals("quit"))
        return;

    } while(!test);
    
    //load the shipments along with their products
    query = "select supplier.name as Supplier, item.name as Product, item.base_size, contains.price, shipment.occurrence from supplier inner join supply_to on supply_to.supply_id = supplier.supply_id inner join shipment on supply_to.ship_id = shipment.ship_id inner join contains on contains.ship_id = shipment.ship_id inner join product on contains.prod_id = product.prod_id inner join specifies on specifies.prod_id = product.prod_id inner join item on specifies.item_id = item.item_id where supplier.supply_id = \'" + supplierID + "\'";
    try
    {
      ResultSet result = query(connection, query, "Couldn't output suppliers");
      if(!result.next())
      {
        System.out.println("That supplier doesn't have any purchases");
      }
      else
      {
        System.out.println("\nSupplier             Product                        Base_size Price    Date");
        do
        {
          System.out.println(String.format("%-20s %-30s %-9s %-8s %-30s", result.getString("Supplier"), result.getString("Product"), result.getString("Base_size"), result.getString("Price"), result.getString("Occurrence")));
        } while (result.next());
      }
    }
    catch(Exception e)
    {
      System.out.println("Couldn't load supplier table");
    }
  }
  catch(Throwable throwable)
    {
        System.out.println("Incorrect input");
    }
  }
  
  //method to check how well products are doing
  public static void recall(Connection connection)
  {
    try
    {
    Scanner userInput = new Scanner(System.in);
    String query = "select product.prod_id, supplier.supply_id, item.name, item.base_size, product.price from item inner join specifies on specifies.item_id = item.item_id inner join product on specifies.prod_id = product.prod_id inner join inventory on product.prod_id = inventory.prod_id inner join supplier on supplier.supply_id = inventory.supply_id";
    boolean test = false;
    String testID = "";
    String prod_id = "";
    ResultSet result = null;
    
    System.out.println("Enter the product ID of the item that you want to recall:");
    prod_id = userInput.nextLine();
    do
    {
      try
      {
        result = query(connection, query, "Can't load products");
        if(!result.next())
        {
          System.out.println("That entity doesn't exist");
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
      
      //exit the method if they decide to quit
      if(prod_id.equals("quit"))
      {
        return;
      }
      
    } while(!test);
    
    boolean component = true;
    boolean component1 = true;
    String newProdID = "";
    do
    {
      //get the item id
      String item_id = "";
      query = "select * from product natural join specifies natural join item";
      try
      {
          result = query(connection, query, "Couldn't load items");
          if(!result.next())
          {
              System.out.println("Unable to load items list");
          }
          else
          {
              do
              {
                  if(prod_id.equals(result.getString("prod_id")))
                  {
                      item_id = result.getString("item_id");
                  }
              } while(result.next());
          }
      }
      catch(Exception e)
      {
          System.out.println("Couldn't load item list");
      }

      //get the shipment id
      String ship_id = "";
      query = "select product.prod_id, shipment.ship_id from product inner join contains on product.prod_id = contains.prod_id inner join shipment on shipment.ship_id = contains.ship_id";
      try
      {
          result = query(connection, query, "Couldn't load items");
          if(!result.next())
          {
              System.out.println("Unable to load items list");
          }
          else
          {
              do
              {
                  if(prod_id.equals(result.getString("prod_id")))
                  {
                      ship_id = result.getString("ship_id");
                  }
              } while(result.next());
          }
      }
      catch(Exception e)
      {
          System.out.println("Couldn't load item list");
      }

      //check to see if the product is a component of any other products
      query = "select * from product natural join component";
      component = false;
      component1 = false;
      try
      {
          result = query(connection, query, "Couldn't load items");
          if(!result.next())
          {
              System.out.println("Unable to load items list");
          }
          else
          {
              do
              {
                  if(prod_id.equals(result.getString("prod_id")))
                  {
                      component = true;
                      newProdID = result.getString("comp_id");
                  }
                  if(prod_id.equals(result.getString("comp_id")))
                  {
                      component1 = true;
                      newProdID = result.getString("prod_id");
                  }
              } while(result.next());
          }
      }
      catch(Exception e)
      {
          System.out.println("Couldn't load item list");
      }

      //queries to delete all aspects of the item and complete full recall
      query = "delete from item where item_id = \'" + item_id + "\'";
      query(connection, query, "Couldn't delete from item table");

      if(component || component1)
      {
        query = "delete from component where comp_id = \'" + prod_id + "\'";
        query(connection, query, "Couldn't delete from component table");
      }

      query = "delete from product where prod_id = \'" + prod_id + "\'";
      query(connection, query, "Couldn't delete from product table");

      query = "delete from shipment where ship_id = \'" + ship_id + "\'";
      query(connection, query, "Couldn't delete from shipment table");

      if(component || component1)
      {
        prod_id = newProdID;
      }
      else
      {
        System.out.println("\nProduct has been recalled");
      }
    } while(component || component1);
  }
  catch(Throwable throwable)
    {
        System.out.println("Incorrect input");
    }
  }
  
  //method to execute queries
  public static ResultSet query(Connection connection, String query, String error)
  {
    try
    {
      Statement statement = connection.createStatement();
      return statement.executeQuery(query);
    }
    catch(Exception e)
    {
      System.out.println(e);
      System.out.println(error);
    }
    return null;
  }
}