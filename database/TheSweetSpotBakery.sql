CREATE DATABASE Bakery;

CREATE TABLE users (
    ID INT PRIMARY KEY IDENTITY(1,1),           
    UserName VARCHAR(100) NOT NULL,       
    Password VARCHAR(100) NOT NULL,        
    Question VARCHAR(100) NOT NULL,       
    Answer VARCHAR(100) NOT NULL,          
    Date DATE NULL                         
);

CREATE TABLE products (
    ID INT IDENTITY(1,1),           
    Product_ID VARCHAR(100) NULL,         
    Product_Name VARCHAR(100) NULL,      
    Type VARCHAR(100) NULL,               
    Stock INT NULL,                        
    Price DECIMAL(8,3) NULL,               
    Status VARCHAR(100) NULL,            
    Image VARCHAR(500) NULL,               
    Date DATE NULL                      
);

CREATE TABLE customer(
ID INT PRIMARY KEY IDENTITY(1,1),
	Customer_ID Int,
	Product_ID VARCHAR(100),
	Product_Name Varchar(100),
	Type VARCHAR(100),
	Quantity Int,
	Price Decimal(8,3),
	Date DATE,
	Image VARCHAR(500),
	Em_Username Varchar(100)
);

CREATE TABLE receipt(
ID INT PRIMARY KEY IDENTITY(1,1) NOT NULL,
	Customer_ID Int NOT NULL,
	Total Decimal(10,3) NOT NULL,
	Date DATE,
	Em_Username Varchar(100) NOT NULL
);

-- Procedure to remove item from cart
CREATE PROCEDURE sp_RemoveFromCart
    @CustomerID INT,
    @ProductID VARCHAR(100)
AS
BEGIN
    DECLARE @Quantity INT
    
    -- Get quantity being removed
    SELECT @Quantity = Quantity FROM customer 
    WHERE Customer_ID = @CustomerID AND Product_ID = @ProductID
    
    -- Restore stock
    UPDATE products 
    SET Stock = Stock + @Quantity,
        Status = 'Available'
    WHERE Product_ID = @ProductID
    
    -- Remove from cart
    DELETE FROM customer 
    WHERE Customer_ID = @CustomerID AND Product_ID = @ProductID
END

CREATE TABLE employees (
    ID INT IDENTITY(1,1),
	Employee_ID INT PRIMARY KEY,               
    Em_Username VARCHAR(100) NOT NULL,
    Items_Sold INT DEFAULT 0,
    Customers_Handled INT DEFAULT 0,
    Sale DECIMAL(10,2) DEFAULT 0.00,
    Date DATE DEFAULT GETDATE(),
    FOREIGN KEY (Employee_ID) REFERENCES users(ID)
);

