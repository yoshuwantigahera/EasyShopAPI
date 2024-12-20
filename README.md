# EasyShopAPI
EasyShop is an online e-commerce platform that allows customers to:

Log in or register for personalized shopping.
Search products by price or category.
Add products to a shopping cart and manage purchases.
This project is built using a REST API for the backend, an Azure SQL Database for data storage, and a frontend comprising HTML, CSS, and JavaScript. Postman was used extensively for testing the API endpoints.

# Features
Customer Login and Registration: Secure authentication.
Product Search: Filter products by price or category.
Shopping Cart: Add, update, or remove items.
Responsive Frontend: Built with HTML, CSS, and JavaScript for a user-friendly interface.
Robust Backend: Built using Java with a RESTful API to handle business logic and data exchange.
Azure Database Integration: Reliable and scalable data storage.
Postman Testing: API functionality thoroughly tested with Postman.
# Tech Stack
Frontend:
HTML5, CSS3
JavaScript
Backend:
Java with Spring Boot
RESTful API
Database:
Azure SQL Database
# Tools:
Postman (API Testing)
Git (Version Control)
Installation and Setup
Prerequisites:
Java 17 or above
Maven (for managing dependencies)
Node.js (optional, if the frontend uses build tools)
Azure SQL Database or a local SQL Server instance
Postman (for testing)
# Steps:
Clone the Repository:

bash
Copy code
git clone [repository-url](https://github.com/yoshuwantigahera/EasyShopAPI.git)
# Set Up the Database:

Configure your Azure SQL Database connection string in the application.properties file.
Run the provided SQL scripts to create tables and seed initial data.
Run the Backend Server:

bash
Copy code
mvn spring-boot:run
Serve the Frontend:

Extract the capstone-client-web-application.zip file.
Open the index.html in your browser or serve it using a web server.
Test API Endpoints:

Import the Postman collections (capstone_postman_collections.zip) into Postman.
Execute requests to verify the functionality.
API Endpoints
Here are some core API endpoints:

Method	Endpoint	Description
POST	/api/auth/login	Login a user
POST	/api/auth/register	Register a new user
GET	/api/products	Fetch all products
GET	/api/products?category={name}	Fetch products by category
GET	/api/products?priceRange={min,max}	Fetch products by price range
POST	/api/cart/add	Add a product to the cart
GET	/api/cart	Fetch the user's cart
Postman Testing
Import the Postman collections:

Use capstone_postman_collections.zip for all team endpoints.
Use capstone_postman_collections-solo.zip for individual project endpoints.
Test individual endpoints by providing the required parameters and request body.

# Screenshots
https://paste.pics/136e02eda68f42e5189a636015cebb1d

https://paste.pics/61119436917d2d43482ac007fc68640c

