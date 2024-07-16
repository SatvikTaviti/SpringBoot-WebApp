# Spring Boot Web Application: Registration and Data Management System

## Overview
This Spring Boot web application provides a robust system for user registration, data management, and communication functionalities. It simplifies the process of managing user data, exporting it for analysis, and communicating with users via email.

## Features

### 1. Registration Form
- **User Registration**: Allows users to register with their details.

### 2. Data Management
- **Data Insertion**: Inserts user registration data into a SQL Server database.
- **Display and Search**: Displays user details in a table with search functionality.

### 3. Export Functionality
- **Export to Excel**: Allows exporting user data into an Excel sheet.
- **Export to PDF**: Generates PDF reports of user data.

### 4. PDF Conversion
- **HTML to PDF**: Converts HTML files to PDF format for easy sharing and printing.

### 5. Email Communication
- **Email Sending**: Sends emails to selected recipients, providing a direct communication channel.

## Technologies Used
- **Backend**: Java, Spring Boot, Spring MVC, Spring Data JPA
- **Database**: SQL Server
- **Frontend**: Thymeleaf (for server-side rendering)
- **PDF Conversion**: PDFBox or any suitable library
- **Email**: JavaMail API or Spring Mail

## Installation and Setup
1. **Clone the Repository**:
   
   git clone <repository_url>
   
2. **Database Configuration**:

  Install SQL Server and create a database schema.
  Update application.properties with your database credentials.
  
3. **Build and Run**:

  mvn clean install
  java -jar target/<jar_filename>.jar
  
4. **Access the Application**:
  Open a web browser and go to http://localhost:8080.


## Technologies Used
- **Registration**: Fill out the registration form to create new user entries.
- **Data Management**: View and search user details displayed in a table.
- **Export**: Download user data in Excel or PDF formats.
- **PDF Conversion**: Convert HTML documents to PDF for sharing or archival purposes.
- **Email Communication**: Send emails to selected recipients directly from the application.


## Contributing

Contributions are welcome! Please fork the repository and submit pull requests with improvements or additional features.

