/*
 * Nico Beard
 * CSE 241
 * Manager Interface
*/

import java.util.Scanner;
import java.sql.*;
import java.io.*;

public class Manager
{
    public static void managerInterface(Connection connection) throws SQLException, IOException, java.lang.ClassNotFoundException
    {
        try
        {
        System.out.println("Hello, please enter your supplier ID: ");
        String supplier = supID(connection);
        if(supplier.equals("quit"))
        {
            return;
        }


        int option = 0;
        boolean test = false;
        do
        {
            Scanner input = new Scanner(System.in);
            System.out.println("\nWhat would you like to do?");
            System.out.println("Enter 1 to view your products");
            System.out.println("Enter 2 view your shipments");
            System.out.println("Enter 3 to view the components for your products");
            System.out.println("Enter 4 to quit");

            try
            {
                option = input.nextInt();
                input.nextLine();
                switch(option)
                {
                    case 1:
                        boolean product = viewRecord("product", connection, supplier);
                        if(product)
                        {
                            changePrice(connection, supplier);
                        }
                        addProduct(connection, supplier, product);
                        System.out.println("\nUpdated products: ");
                        viewRecord("product", connection, supplier);
                        break;
                    case 2:
                        viewRecord("shipment", connection, supplier);
                        break;
                    case 3:
                        viewRecord("component", connection, supplier);
                        break;
                    case 4:
                        test = true;
                        break;
                    default:
                        System.out.println("Incorrect input, please try again");
                }
            }
            catch(Exception e)
            {
                System.out.println("Incorrect input, please try again");
            }
        } while(!test);
    }
    catch(Throwable throwable)
    {
        System.out.println("Incorrect input");
    }
    }

    //verify supplier ID
    public static String supID(Connection connection) throws SQLException, IOException, java.lang.ClassNotFoundException
    {
        String supplier = "";
        try
        {
        Scanner input = new Scanner(System.in);
        supplier = input.nextLine();
        //get all the ids in the database
        Statement statement = connection.createStatement();
        String query = "select * from supplier";
        ResultSet result = null;
        boolean correct = false;
        boolean results = false;
        String id = "";
        boolean match = false;
        
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
                    } while(result.next());
                }
            }
            catch(Exception e)
            {
                System.out.println("Incorrect entity inputted");
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
        
        //if they inputted a valid supplier, allow them to move on
        if(match)
        {
            return supplier;
        }
    }
    catch(Throwable throwable)
    {
        System.out.println("Incorrect input");
        System.exit(0);
    }
        return supplier;
    }

    //view corresponding records
    public static boolean viewRecord(String record, Connection connection, String supplier) throws SQLException, IOException, java.lang.ClassNotFoundException //for viewing results of selecting certain records
	{
		Statement statement = connection.createStatement();
		String query = "";
		ResultSet result = null;
		boolean correct;
		boolean results = false;
        boolean counter = false;

		//print out products
		if(record.contains("product"))
		{
            query = "select product.prod_id, item.item_id, item.name, product.price, item.base_size from item inner join specifies on specifies.item_id = item.item_id inner join product on specifies.prod_id = product.prod_id inner join inventory on inventory.prod_id = product.prod_id inner join supplier on inventory.supply_id = supplier.supply_id where supplier.supply_id = \'" + supplier + "\'";
        try
			{
				result = statement.executeQuery(query);
				if(!result.next())
				{
					System.out.println("You currently are not supplying any products");
                    return counter;
				}
				else
				{
                    counter = true;
					System.out.println("\nProd_ID	 Item_ID  Name                           Price    Base_size");
					do
					{
						System.out.println(String.format("%-8s %-8s %-30s %-8s %-8s", result.getString("Prod_id"), result.getString("Item_id"), result.getString("Name"), result.getString("Price"), result.getString("Base_size")));
					}
					while(result.next());
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
    
        //print out shipments
		if(record.contains("shipment"))
		{
            query = "select shipment.amount, shipment.occurrence, item.name as Product, contains.price, item.base_size from supplier inner join supply_to on supply_to.supply_id = supplier.supply_id inner join shipment on supply_to.ship_id = shipment.ship_id inner join contains on shipment.ship_id = contains.ship_id inner join product on product.prod_id = contains.prod_id inner join specifies on specifies.prod_id = product.prod_id inner join item on item.item_id = specifies.item_id where supplier.supply_id = \'" + supplier + "\'";
			try
			{
				result = statement.executeQuery(query);
				if(!result.next())
				{
					System.out.println("You currently don't have any shipments");
				}
				else
				{
					System.out.println("Amount  Occurrence Product                        Price    Base_size");
					do
					{
						System.out.println(String.format("%-7s %-10s %-30s %-8s %-8s", result.getString("amount"), result.getString("occurrence"), result.getString("Product"), result.getString("price"), result.getString("base_size")));
					} while(result.next());
				}
			}
			catch(Exception e)
			{
				System.out.println("Couldn't load shipments");
			}
		}
    
        //print out components
		if(record.contains("component"))
		{
            query = "select component.comp_id, product.prod_id, item.item_id, item.name, product.price, item.base_size from supplier inner join inventory on supplier.supply_id = inventory.supply_id inner join product on product.prod_id = inventory.prod_id inner join component on component.prod_id = product.prod_id inner join specifies on product.prod_id = specifies.prod_id inner join item on item.item_id = specifies.item_id where supplier.supply_id = \'" + supplier + "\'";
            try
			{
				result = statement.executeQuery(query);
				if(!result.next())
				{
					System.out.println("You currently don't have any components");
				}
				else
				{
					System.out.println("\nComponent_ID Product_ID Item_ID  Name                           Price    Base_size");
					do
					{
						System.out.println(String.format("%-12s %-10s %-8s %-30s %-8s %-8s", result.getString("comp_id"), result.getString("prod_id"), result.getString("item_id"), result.getString("Name"), result.getString("Price"), result.getString("Base_size")));
					}
					while(result.next());
				}
			}
			catch(Exception e)
			{
				System.out.println("Couldn't load components");
			}
		}
        return counter;
    }

    //method to change price of products
    public static void changePrice(Connection connection, String supplierID) throws SQLException, IOException, java.lang.ClassNotFoundException, java.util.InputMismatchException
    {
        try
        {
        int option = 0;
        Scanner input = new Scanner(System.in);
        String prodID = "";
        boolean test = false;
        ResultSet result = null;
        Statement statement = connection.createStatement();
        String query = "select product.prod_id, item.item_id, item.name, item.base_size, product.price from item inner join specifies on item.item_id = specifies.item_id inner join product on product.prod_id = specifies.prod_id inner join inventory on product.prod_id = inventory.prod_id inner join supplier on inventory.supply_id = supplier.supply_id where supplier.supply_id = \'" + supplierID + "\'";

        do
        {
            System.out.println("\nEnter 1 to change the price of your products");
            System.out.println("Enter 2 to move on");
            Scanner userInput = new Scanner(System.in);
            try
            {
                option = userInput.nextInt();
                userInput.nextLine();
                switch(option)
                {
                    case 1:
                        test = true;
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Incorrect input, try again");
                }
            }
            catch(Exception e)
            {
                System.out.println("Incorrect input, try again");
            }
        } while(!test);


        test = false;
        System.out.println("\nEnter the product ID that you would like to change the price of or \"quit\" to quit");
        do
        {
            prodID = input.nextLine();
            try
            {
                result = statement.executeQuery(query);
                if(!result.next())
                {
                    System.out.println("That entity doesn't exist");
                }
                else
                {
                    do
                    {
                        if(prodID.equals(result.getString("prod_id")))
                        {
                            test = true;
                        }
                    } while(result.next());
                }
            }
            catch(Exception e)
            {
                System.out.println("Incorrect entity inputted");
            }

            //exit the method if they decide to quit
            if(prodID.equals("quit"))
            {
                return;
            }

            //check to see if the user is trying to mess with the database
            if(prodID.contains(";") || prodID.contains("\'"))
            {
                test = false;
            }

            //check to see if the id they entered is valid, offering them a way out if they want
            if(!test)
            {
                System.out.println("\nHmm, the product ID you entered doesn't match our records");
            }

        } while(!test);

        test = false;
        double price = 0;
        int counter1 = 0;
        try
        {
            do
            {
                if(price == 0 && counter1 != 0)
                {
                    return;
                }
                counter1 = 1;
                System.out.println("\nWhat would you like to change the price to?");
                while(!input.hasNextDouble())
                {
                    input.next();
                    System.out.println("Incorrect input, try again");
                }
                price = input.nextDouble();
                if(price > 0 && price < 10000000)
                {
                    test = true;
                }
                else
                {
                    System.out.println("The price you entered is out of range, enter again or enter 0 to quit");
                }
            } while(!test);
            if(price != 0)
            {
                query = "update product set price = \'$" + price + "\' where prod_id = \'" + prodID + "\'";
                query(connection, query, "Couldn't update price of specified product");
                System.out.println("\nPrice changed");
            }
        }
        catch(Exception e)
        {
            System.out.println("Incorrect input");
        }
    }
    catch(Throwable throwable)
    {
        System.out.println("Incorrect input");
    }
    }
    
    //method to add products
    public static void addProduct(Connection connection, String supplier, boolean product) throws SQLException, IOException, java.lang.ClassNotFoundException, java.util.InputMismatchException
    {
        try
        {
        Scanner input = new Scanner(System.in);
        String query = "";
        String name = "";
        double price = 0.0;
        int base_size = 0;
        boolean test = false;
        int option = 0;

        do
        {
            System.out.println("\nEnter 1 to add a product");
            System.out.println("Enter 2 to move on");
            Scanner userInput = new Scanner(System.in);
            try
            {
                option = userInput.nextInt();
                userInput.nextLine();
                switch(option)
                {
                    case 1:
                        test = true;
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Incorrect input, try again");
                }
            }
            catch(Exception e)
            {
                System.out.println("Incorrect input, try again");
            }
        } while(!test);


        test = false;
        System.out.println("\nEnter the name, price, and base size for the product you are adding");
        do
        {
            try
            {
                System.out.print("Name: ");
                name = input.nextLine();
                System.out.print("Price: ");
                while(!input.hasNextDouble())
                {
                    input.next();
                    System.out.println("You must enter a number:");
                }
                price = input.nextDouble();
                input.nextLine();
                System.out.print("Base Size: ");
                while(!input.hasNextInt())
                {
                    input.next();
                    System.out.println("You must enter a number:");
                }
                base_size = input.nextInt();
                input.nextLine(); 
                
                if(name.length() > 30 || String.valueOf(price).length() > 8 || price < 0 || base_size < 0 || base_size > 99)
                {
                    System.out.println("One of the inputs you entered is incorrect. The base_size must be between 0 and 100.");
                    System.out.println("Please try again or enter \"quit\" in the name entry to quit");
                }
                else
                {
                    test = true;
                }

                //check to see if the user is trying to mess with the database
                if(name.contains(";") || name.contains("\'"))
                {
                    System.out.println("Hmm, it seems you're trying to do something illegal in the database");
                    test = false;
                }
                
                if(name.equals("quit"))
                {
                    return;
                }
            }
            catch(Exception e)
            {
                System.out.println("Incorrect option");
            }
        } while(!test);

        //generate the product and item id's for the new product
        int prod_id = (int)(Math.random() * ((99999999 - 0) + 1)) + 0;
        int item_id = (int)(Math.random() * ((99999999 - 0) + 1)) + 0;
        int ship_id = (int)(Math.random() * ((99999999 - 0) + 1)) + 0;

        //check who the product is being supplied from
        boolean correct = false;
        System.out.println("\nWhich supplier is supplying the product you are adding?");
        String supplyFrom = "";
        ResultSet result = null;
        Statement statement = connection.createStatement();
        query = "select * from supplier";
        do
        {
            //exit the method if they decide to quit
            if(supplyFrom.equals("quit"))
            {
                return;
            }
            supplyFrom = input.nextLine();
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
                        
                        if(supplyFrom.equals(result.getString("supply_id")))//check if their input is a valid supplier in the database
                        {
                            correct = true;
                        }
                    } while(result.next());
                }
            }
            catch(Exception e)
            {
                System.out.println("There are no suppliers");
            }

            //check to see if the user is trying to mess with the database
            if(supplyFrom.contains(";") || supplyFrom.contains("\'"))
            {
                System.out.println("Hmm, you are trying to do something to do something to the database that is illegal");
                correct = false;
            }
        
            //check to see if the id they entered is valid, offering them a way out if they want
            if(!correct)
            {
                System.out.println("\nHmm, the ID you entered doesn't match our records. You can enter a new ID to try again or enter \"quit\" to quit");
            }

            //make sure the product they are adding is being supplied from themselves
            if(supplier.equals(supplyFrom))
            {
                System.out.println("\nThe product you're adding can't be supplied by yourself (enter \"quit\" to quit)");
                correct = false;
            }
        
        } while(!correct);


        boolean component = false;
        String comp_id = "";
        if(product)
        {
            test = false;
            //check to see if the product they are adding is a component for another product
            do
            {
                System.out.println("\nIs the product that you are adding a component for another product?");
                System.out.println("Enter 1 for yes");
                System.out.println("Enter 2 for no");
                try
                {
                    while(!input.hasNextInt())
                    {
                        input.next();
                        System.out.println("Incorrect input, try again");
                    }
                    option = input.nextInt();
                    input.nextLine();
                    switch(option)
                    {
                        case 1:
                            comp_id = addComponent(connection, supplier, prod_id);
                            if(!(comp_id.equals("quit")))
                            {
                                component = true;
                            }
                            test = true;
                            break;
                        case 2:
                            test = true;
                            break;
                        default:
                            System.out.println("Incorrect input, try again");
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Incorrect input, try again");
                }
            } while(!test);
        }

        int day = (int)(Math.random() * ((31 - 0) + 1)) + 0;
        int month = (int)(Math.random() * ((12 - 0) + 1)) + 0;
        int year = (int)(Math.random() * ((2019 - 2000) + 1)) + 2000;
        double amount = (Math.random() * ((10 - 0) + 1)) + 0;
        amount = Math.round(amount * 100.0) / 100.0;
        
        query = "insert into Product (prod_id, price) values (\'" + prod_id + "\', \'$" + price + "\')";
        query(connection, query, "Unable to insert product");
        
        query = "insert into Item (item_id, name, base_size) values (\'" + item_id + "\', \'" + name + "\', \'" + base_size + "\')";
        query(connection, query, "Unable to insert item");

        query = "insert into specifies (prod_id, item_id) values (\'" + prod_id + "\', \'" + item_id + "\')";
        query(connection, query, "Unable to insert into specifies");
        
        query = "insert into inventory (supply_id, prod_id) values (\'" + supplier + "\', \'" + prod_id + "\')";
        query(connection, query, "Unable to insert into inventory");

        query = "insert into shipment (ship_id, amount, occurrence) values (\'" + ship_id + "\', \'$" + amount + "\', \'" + month + "/" + day + "/" + year + "\')";
        query(connection, query, "Unable to insert into shipment");

        query = "insert into contains (ship_id, prod_id, price) values (\'" + ship_id + "\', \'" + prod_id + "\', \'$" + price + "\')";
        query(connection, query, "Unable to insert into contains");

        query = "insert into supply_to (supply_id, ship_id) values (\'" + supplier + "\', \'" + ship_id + "\')";
        query(connection, query, "Unable to insert into supply_to");

        query = "insert into supply_from (supply_id, ship_id) values (\'" + supplyFrom + "\', \'" + ship_id + "\')";
        query(connection, query, "Unable to insert into supply_from");

        if(component)
        {
            query = "insert into Component (prod_id, comp_id) values (\'" + prod_id + "\', \'" + comp_id + "\')";
            query(connection, query, "Couldn't add component");
        }
    }
    catch(Throwable throwable)
    {
        System.out.println("Incorrect input");
    }
    }

    //method to make a product a component of another product
    public static String addComponent(Connection connection, String supplier, int prodID) throws SQLException, IOException, java.lang.ClassNotFoundException, java.util.InputMismatchException
    {
        String prod_id = "";
        try
        {
        Scanner userInput = new Scanner(System.in);
        boolean test = false;
        ResultSet result = null;
        prod_id = "";
        String query = "select product.prod_id, item.item_id, item.name, item.base_size, product.price from item inner join specifies on item.item_id = specifies.item_id inner join product on product.prod_id = specifies.prod_id inner join inventory on product.prod_id = inventory.prod_id inner join supplier on inventory.supply_id = supplier.supply_id where supplier.supply_id = \'" + supplier + "\'";
        Statement statement = connection.createStatement();

        //load the products that the user can choose from
        viewRecord("product", connection, supplier);

        System.out.println("\nEnter the product ID of the item that will be the component:");
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
                    do
                    {
                        if(prod_id.equals(result.getString("prod_id")))
                        {
                            test = true;
                        }
                    } while(result.next());
                }
            }
            catch(Exception e)
            {
                System.out.println("Incorrect input, try again");
            }

            //check to see if the user is trying to mess with the database
            if(prod_id.contains(";") || prod_id.contains("\'"))
            {
                System.out.println("Hmm, you are trying to do something to do something to the database that is illegal");
                test = false;
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
                return prod_id;
            }
        
        } while(!test);
    }
    catch(Throwable throwable)
    {
        System.out.println("Incorrect input");
    }
        return prod_id;
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
            System.out.println(error);
        }
        return null;
    }
}