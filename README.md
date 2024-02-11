## AMTop-Ecommerce

Welcome to AMTop-Ecommerce, a cutting-edge E-commerce web app designed to revolutionize online shopping experiences for both vendors and customers.
---
### Table of Contents

- [Introduction](#introduction)
- [Key Highlights](#key-highlights)
- [Features](#features)
- [Getting Started](#getting-started)
- [Built With](#built-with)
- [Video Link](#video-link)
---
### Introduction

AMTop-Ecommerce is a comprehensive E-commerce platform built using Spring Boot, Java, SQL, React, and TypeScript. It offers two distinct views for vendors and customers, each packed with features to enhance usability and efficiency.
---
### Key Highlights:

- Dual View Support: Seamlessly navigate between Vendor and Customer views, each tailored to optimize user experience.
- Agile Methodologies: We embraced Agile methodologies, utilizing Jira to manage three sprints alongside a meticulous design phase.
- Robust Testing: With a commitment to quality, we implemented unit tests using JUnit, achieving an impressive 78.5% code coverage.
- Comprehensive Design Activities: Our process encompassed ER diagrams, class diagrams, and detailed requirements documentation.
- Efficient Time Management: We provided accurate time estimates for tasks and meticulously logged progress using Jira.
---
### Features

### Main System Features:

- Hashed Passwords: User passwords are securely hashed before being stored in the database, ensuring protection against unauthorized access.
- Admin Access Control: When the root admin adds a new admin, the root admin sets the new admin's email only not his password or any other data (the new admin can complete his data through the sent verification email.
- Verification Email: For users signing up using basic credentials, a verification email is sent to confirm their identity before granting access to the system.
- User Profile Management: where the users can add/edit info to their profiles.
- Browsing & Filtering: Easily explore our extensive product catalog with intuitive search and filtering options.
- Product Details: Dive deep into product specifics, from descriptions to customer reviews.

### Main Customers Features:

- Adding to cart/Wishlist: Effortlessly add items to your cart or Wishlist for a streamlined purchasing journey. Then you can review and edit their selections before making a purchase.
- Order Tracking: Stay updated on order statuses and delivery information at your fingertips.
- Product Reviews and Ratings: Users can leave reviews and ratings for products they've purchased, helping other shoppers make informed decisions.
- Communication: provide vendors contact info so customers can communicate with vendors.
- Tracking Orders: Customers can track the status of their orders, including estimated delivery dates and shipping information.
- Recommendations: E-commerce apps often provide product recommendations based on the offers and bestsellers.
- Track Order History: Customers can review their order history to keep track of past purchases.

### Main Vendors Features:

- Inventory Management: Sellers can track and manage their inventory, including stock levels, and product availability.
- Order Processing: Vendors receive and process customer orders. They can view orders and manage order status updates.
- Pricing Control: Vendors can set and adjust product prices, apply discounts, and run promotions or sales on their products.
- Product Updates: Sellers can edit product listings, update product details, and remove or temporarily hide products from the app.
- Order History: Vendors can access order history to review past sales and transactions.

And much more!
---
### Getting Started

To run AMTop-Ecommerce on your local machine, follow these steps:

1. Clone the repository: `git clone https://github.com/Software-Engineering-E-commerce/AMTop-Ecommerce.git`
3. Install dependencies:
   - Frontend (React): Navigate to the `frontend` directory and run `npm install`
4. Set up the database:
   - Configure the database settings in `backend/src/main/resources/application.properties`
5. Start the backend server: open the backend directory in your prefered IDE and run the "BackEndApplication.java" file in "AMTop-Ecommerce/BackEnd/src/main/java/com/example
/BackEnd/" directory.
6. Start the frontend server: Navigate to the `frontend` directory and run `npm run dev`
7. Access the application in your browser at `http://localhost:3000`
---
### Built With

- Spring Boot
- Java
- SQL
- React
- TypeScript
---
### Video Link

https://drive.google.com/file/d/14RQTeRMq_MmxQyV1Tj_lC0A-O73ntin8/view?usp=drive_link
