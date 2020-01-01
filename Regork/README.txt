Nicolas Beard
npb221@lehigh.edu
CSE241

Regork Enterprise

ER Design:
My ER diagram consists of suppliers, products, shipments, and items. Shipments can be supplied to suppliers and from suppliers, thus, fulfilling the recursive relationship suppliers have. Products specify items. That is, items provide a more general form of products. Attributes, which are more generic such as the name and the base_size for each item are in the item table, while the price is more specific to certain products, especially if there is some sort of bargain between suppliers. Products have components in a recursive relationship. Products can be components of themselves and components can be products themselves. Suppliers have inventory of products and shipments contain products. There is a price in the contains relationship in the case that the price of a product changes. We wouldn't want the price paid for the product to be dependent on the current price of the product as the price may change at any given moment.

Data Generation:
I used mockaroo to generate most of my data, excluding obviously the interfaces which perform inserts and deletes on the database. I also manually entered data into the relationship tables since the foreign and primary keys had to match with the tables they were joining.

Interfaces:
There are three interfaces: customer, manager, and executive. The customer and manager interfaces are from the perspective of the supplier. I ask the user to enter the corresponding supplier ID so I know which supplier they are. The primary difference between these interfaces is their perspective. The customer interface is more from the perspective of someone lower in the hierarchy of a supplier. The customer interface focuses more on buying products from other suppliers and checking the inventory and shipment levels after or before buying said products. The manager focuses more on changing the price of the products they currently supply or adding a new product to their inventory, which may not exist in the database already. After doing so they can decide whether the product they add is a component of one of the products they supply. They can then check the shipments to make sure its up to date. Therefore, the manager has more power than the customer and is more interested in updating the current inventory of the supplier. The executive is from the perspective of Regork. They have the most power on the database. The executive can add supplers, check the purchases of given suppliers, and can recall any product they want. In the case of a recall, the product they are recalling and any components associated with that product are deleted. While I acknowledge that this may cause legal problems, I'm assuming Regork has filed the correct paperwork to remove the products from the database. I also concede that I'm assuming all the components of the product and the product itself are the cause of the recall, which may not be true. I'm taking a conservative approach to this and simply removing all aspects of the product as any sort of defect in the product or components of the product may be detrimental to the company's sales and reputation. The manager can simply add the products and components again if they were wrongly recalled.

Files:
Everything but the actual java, class, and Manifest files are in my top level directory, npb221Beard. The java and class files are located in the npb221 folder within npb221Beard. There is a makefile which I used to compile and make the jar file used for testing in the top level directory.

Sample Run:

Welcome to Regork Enterprise
-----------------------------
Are you a customer, manager, or executive? (Enter "quit" to quit)
customer
Hello, please enter your supplier ID: 
lkjlk

Here is a list of the current ID's in the database: 

Supply_ID Name                 Address
41209194  Kroger               519 10th Avenue               
51941967  hello                34 Main Street                
15246394  Outside Land         92 Upper Sayre Park Road      
16788903  Hershey              54 Hershey Road               
25894254  Wow                  800 North Drive               
39425809  Eatery               769 Clayton Street            
50011419  quit                 kljklj                        
72836863  Airsupply            78 upper east side            
88565054  Kai                  90 East Parkway Street        
93002091  Nico                 226 11th Avenue               
26939979  Berger               652 Lake Street               
28289765  nigga                123 nico street               
18992288  Sean Ryan            325 east fifth                
97201875  Kaila                58914 Ohio Way                
30997826  Lindsy               790 Cody Point                
75575600  Tony                 23 Del Mar Avenue             
28311736  Elmira               1817 Melby Drive              
57841262  Florinda             21993 Orin Hill               
32589894  Regork               7 Dwight Plaza                
64234743  Deirdre              027 Banding Avenue            
93680469  Jon                  600 Monument Avenue           
45618740  Syd                  8404 Grover Terrace           
61167738  Jo-ann               9 Division Hill               
66239722  Tate                 912 Manley Point              
93785199  Susanna              9115 3rd Terrace              
72214829  Patty                8956 Lerdahl Street           
87778941  Karissa              2 Schurz Plaza                
71256181  Daile                60112 Thierer Road            
61572910  Beryle               2 Drewry Road                 
81179790  Roderick             48297 Thierer Hill            
95414858  Frederik             85 Anniversary Park           
25684238  Wang                 2 Onsgard Center              
64253005  Tallie               7 Nelson Crossing             
10744227  Nessi                86 Sullivan Terrace           
87343456  Zonnya               205 Mayer Way                 
08292750  Allegra              5 Randy Junction              
79038689  Davin                6 Autumn Leaf Terrace         
16869645  Flossie              28 Becker Crossing            
50555534  Wakefield            278 Warbler Road              
01353311  Marje                98 Monument Center            
23585703  Fowler               60 Thierer Plaza              
59354172  Becca                57 Sherman Alley              
78890926  Lyle                 358 Ridge Oak Way             
63038567  Guy                  876 Parkway Drive             
23883723  Shi                  69 Nico Beard Road            
99333485  Jimmy J              873 Sunrise Drive             
29406982  lkjlkjlkjlkj         lkjlkjlkjkj                   
20013447  Handyman             892 Upper East Side           

Hmm, the ID you entered doesn't match our records. You can enter a new ID to try again or enter "quit" to quit
15246394

Here is a list of the current ID's in the database: 

Supply_ID Name                 Address
41209194  Kroger               519 10th Avenue               
51941967  hello                34 Main Street                
15246394  Outside Land         92 Upper Sayre Park Road      
16788903  Hershey              54 Hershey Road               
25894254  Wow                  800 North Drive               
39425809  Eatery               769 Clayton Street            
50011419  quit                 kljklj                        
72836863  Airsupply            78 upper east side            
88565054  Kai                  90 East Parkway Street        
93002091  Nico                 226 11th Avenue               
26939979  Berger               652 Lake Street               
28289765  nigga                123 nico street               
18992288  Sean Ryan            325 east fifth                
97201875  Kaila                58914 Ohio Way                
30997826  Lindsy               790 Cody Point                
75575600  Tony                 23 Del Mar Avenue             
28311736  Elmira               1817 Melby Drive              
57841262  Florinda             21993 Orin Hill               
32589894  Regork               7 Dwight Plaza                
64234743  Deirdre              027 Banding Avenue            
93680469  Jon                  600 Monument Avenue           
45618740  Syd                  8404 Grover Terrace           
61167738  Jo-ann               9 Division Hill               
66239722  Tate                 912 Manley Point              
93785199  Susanna              9115 3rd Terrace              
72214829  Patty                8956 Lerdahl Street           
87778941  Karissa              2 Schurz Plaza                
71256181  Daile                60112 Thierer Road            
61572910  Beryle               2 Drewry Road                 
81179790  Roderick             48297 Thierer Hill            
95414858  Frederik             85 Anniversary Park           
25684238  Wang                 2 Onsgard Center              
64253005  Tallie               7 Nelson Crossing             
10744227  Nessi                86 Sullivan Terrace           
87343456  Zonnya               205 Mayer Way                 
08292750  Allegra              5 Randy Junction              
79038689  Davin                6 Autumn Leaf Terrace         
16869645  Flossie              28 Becker Crossing            
50555534  Wakefield            278 Warbler Road              
01353311  Marje                98 Monument Center            
23585703  Fowler               60 Thierer Plaza              
59354172  Becca                57 Sherman Alley              
78890926  Lyle                 358 Ridge Oak Way             
63038567  Guy                  876 Parkway Drive             
23883723  Shi                  69 Nico Beard Road            
99333485  Jimmy J              873 Sunrise Drive             
29406982  lkjlkjlkjlkj         lkjlkjlkjkj                   
20013447  Handyman             892 Upper East Side           

What would you like to do?
Enter 1 to buy products
Enter 2 to check inventory levels
Enter 3 to check shipments of items bought
Enter 4 to quit
2

Here are your current inventory levels: 

Supply_ID Supplier Product                        Price    Base_size
15246394  Outside Land Rice                           $6373.0  45      

What would you like to do?
Enter 1 to buy products
Enter 2 to check inventory levels
Enter 3 to check shipments of items bought
Enter 4 to quit
lkjlkj
Incorrect input, try again
3

Here are your current shipments: 

Amount  Occurrence Product                        Price    Base_size
$7.26   7/30/2019  Rice                           $6373.0  45      

What would you like to do?
Enter 1 to buy products
Enter 2 to check inventory levels
Enter 3 to check shipments of items bought
Enter 4 to quit
1
Enter the product ID of the item that you want to purchase:
lkjlj

Here is a list of the current products in the database: 

Product_ID Suppier_ID Name    		             Base_size Price
78902908   15246394   Rice                           45        $6373.0 
16193274   87778941   Salt                           43        $73846.0
3956375    93785199   Beans                          87        $6383.0 
39269391   87778941   Olive oil                      54        $73673.0
97162283   32589894   Muffin - Banana Nut Individual 78        $6232.00
87637325   93680469   Tea Leaves - Oolong            2         $8773.49
78352276   66239722   Capon - Whole                  96        $795.42 
36066316   95414858   Toothpick Frilled              82        $330.52 
16953379   72214829   Bread - Onion Focaccia         24        $967.54 
13472124   64234743   Scotch - Queen Anne            73        $9928.81
13649981   32589894   Cheese - Comte                 86        $4974.58
87329280   93785199   Creme De Menthe Green          17        $4532.06
78075733   10744227   Wine - White, Pinot Grigio     83        $4462.80
11619186   66239722   Cheese - Bocconcini            43        $9000.0 
73149498   93002091   Wine - Balbach Riverside       69        $9999.99
90918396   08292750   Vol Au Vents                   78        $9323.25
94127038   64234743   Ocean Spray - Kiwi Strawberry  81        $897.65 
83648221   93785199   Soup V8 Roasted Red Pepper     98        $253.57 
90031039   23585703   Ice Cream - Super Sandwich     17        $3253.10
96179927   63038567   Leaves                         45        $8393.0 
65631471   16869645   Cinammon                       78        $89989.0

Hmm, the product you entered doesn't match our records. You can enter a new product ID to try again or enter "quit" to quit
36066316

Here is a list of the current products in the database: 

Product_ID Suppier_ID Name    		             Base_size Price
78902908   15246394   Rice                           45        $6373.0 
16193274   87778941   Salt                           43        $73846.0
3956375    93785199   Beans                          87        $6383.0 
39269391   87778941   Olive oil                      54        $73673.0
97162283   32589894   Muffin - Banana Nut Individual 78        $6232.00
87637325   93680469   Tea Leaves - Oolong            2         $8773.49
78352276   66239722   Capon - Whole                  96        $795.42 
36066316   95414858   Toothpick Frilled              82        $330.52 
16953379   72214829   Bread - Onion Focaccia         24        $967.54 
13472124   64234743   Scotch - Queen Anne            73        $9928.81
13649981   32589894   Cheese - Comte                 86        $4974.58
87329280   93785199   Creme De Menthe Green          17        $4532.06
78075733   10744227   Wine - White, Pinot Grigio     83        $4462.80
11619186   66239722   Cheese - Bocconcini            43        $9000.0 
73149498   93002091   Wine - Balbach Riverside       69        $9999.99
90918396   08292750   Vol Au Vents                   78        $9323.25
94127038   64234743   Ocean Spray - Kiwi Strawberry  81        $897.65 
83648221   93785199   Soup V8 Roasted Red Pepper     98        $253.57 
90031039   23585703   Ice Cream - Super Sandwich     17        $3253.10
96179927   63038567   Leaves                         45        $8393.0 
65631471   16869645   Cinammon                       78        $89989.0

Item has been bought, you can see it in your inventory levels

What would you like to do?
Enter 1 to buy products
Enter 2 to check inventory levels
Enter 3 to check shipments of items bought
Enter 4 to quit
2

Here are your current inventory levels: 

Supply_ID Supplier Product                        Price    Base_size
15246394  Outside Land Toothpick Frilled              $330.52  82      
15246394  Outside Land Rice                           $6373.0  45      

What would you like to do?
Enter 1 to buy products
Enter 2 to check inventory levels
Enter 3 to check shipments of items bought
Enter 4 to quit
4
Are you a customer, manager, or executive? (Enter "quit" to quit)
quit
