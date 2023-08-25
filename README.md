# Reggie-Food-Delivery-App
## Features Introduction
This project is to implement a takeaway **restaurant management** system with front and backend. 

The **backend management system** includes: administrator/employee login, administrator add/disable/delete/modify employee account, modify employee information, manage dish categories (add/delete), manage dishes/set menus (modify/suspend/delete/create new/query), query user billing (view/delivery), log out.

The **front-end user interface** includes user login, user order (select specification/quantity), add shopping cart, add new delivery address, set default harvesting address, view order details and other features.

![1692914202035](https://github.com/JunweiZhangOrSteven/Reggie-Food-Delivery-App/assets/102798557/8c1115a3-34a7-47ff-a1c8-1a20cab27a82)
![1692914234080](https://github.com/JunweiZhangOrSteven/Reggie-Food-Delivery-App/assets/102798557/4db8158d-aa6e-41a2-889b-56bbb9ed93d6)

## Database Design
The database includes 11 tables:

<img width="176" alt="image" src="https://github.com/JunweiZhangOrSteven/Reggie-Food-Delivery-App/assets/102798557/99949a93-2e60-42b5-9fd1-2d9827f7f26c">

## Backend Employee and Admin Login/Logout
Build EmployeeController for Employee management.

The processes of function realization are:
1. Encrypt password via MD5
2. Query database via username
3. If there is no result in database, return error message.
4. Verify password
5. Check Employee Status, if employee has been suspended, return suspended message
6. Login successfully, save Employee id into session (current user)

**NOTICE** If user has not login, we can't let user access home page (especially the Employee data).

So we need to adding Login Check Filter to prevent user accessing the home page without login

## Employee Create/Delete/Update
Basic database operations

## Employee Information Paging Query
1. webpage submitting ajax request, put parameters page, pageSize and name on server side.
2. Controller recives parameters and calls service to query data
3. Service invokes Mapper to operate database, Query paging data
4. Controller responds the webpage with queried paging data
5. Page displays those data by ElementUI and Table conponents.

## Public Field Filling
That is the one feature of mybatis-plus.

We build MyMetaObjectHandler component to set the rules for public field auto filling. Then add annotation @TableField(fill = FieldFill.XXX) on entity classes. mybatis will help us filling those fields autometically when execute corresponding database operation.

## Category Create/Read/Update/Delete
The create/read/update operations for category are some basic interactions with database. The only thing we need to worry about is checking the related dish and setmeal before we delete this category. (if there is related dish or setmeal, we can't delete this category)

## File Upload/Download
Implement File upload/download features in common controller.

## Dish Create/Read/Update/Delete
The frontend page send a dish data that related with two tables in the backend database, dish and dishFlavor. For convenience we build a dishDto model as the return data to the frontend. In the backend, we do multi-table operations by mybatis on the service layer. By the help with mybatis, we don't do multi-table query on the database, that could save some execution time.

## Caching Optimization
Our App may have large number of use, with the high concurrent, the frequent accessing database can lead poor system performance degradation.
We can use redis to optimize our system. We save our data into redis 60 minutes So we don't always need to query database to read our data.



