# Attendance Management System

A comprehensive, desktop-based **Attendance Management System** built with Java Swing and MySQL. This application provides an efficient solution for educational institutions to track and manage student attendance with role-based access for teachers and students.

---

## 📖 Overview

This system simplifies attendance tracking by offering:
- **For Teachers:** A full suite of tools to mark attendance, manage students and subjects, view detailed records with advanced filtering, and export data.
- **For Students:** A personalized dashboard to view their own attendance history, apply filters, and export their records.

The application follows the **Model-View-Controller (MVC)** pattern for a clean, maintainable codebase.

---

## ✨ Features

### 🔐 Authentication & Dashboards
- **Role-Based Login:** Secure login for Teachers and Students.
- **Personalized Dashboards:** Separate, role-specific interfaces upon login.

### 👨‍🏫 Teacher Features
- **Mark Attendance:** Record attendance for subjects with real-time student search (by name or PID).
- **Manage Students:** Perform CRUD (Create, Read, Update, Delete) operations on student accounts.
- **Manage Subjects:** Add or remove academic subjects.
- **View Records:** Access a complete attendance history with advanced multi-criteria filtering (by date, subject, status, student).
- **Modify/Delete Records:** Edit existing attendance entries or remove them with confirmation.
- **Export to CSV:** Export filtered or complete attendance data for reporting.

### 👨‍🎓 Student Features
- **View Personal Attendance:** See a summary and detailed list of their own attendance.
- **Filter Records:** Filter personal history by date range, subject, and status.
- **Export Personal Data:** Export their own attendance records to a CSV file.

### 🛠️ General Features
- **Professional UI:** A clean and intuitive user interface built with Java Swing.
- **Real-time Search & Filtering:** Instant results when searching for students or filtering records.
- **Data Integrity:** Comprehensive error handling and data validation.

---

## 🛠️ Technology Stack

| Component         | Technology / Library                               |
| ----------------- | -------------------------------------------------- |
| **Frontend**      | Java Swing (JDK 23)                                |
| **Backend**       | Java                                               |
| **Database**      | MySQL (via XAMPP)                                  |
| **Database Driver**| MySQL Connector/J (9.0+)                          |
| **Architecture**  | MVC (Model-View-Controller) Pattern               |

---

## 🚀 Installation & Setup

### Prerequisites
1. **Java Development Kit (JDK):** Version 23 or later.
2. **XAMPP:** To host the MySQL database server.
3. **MySQL Connector/J:** The JDBC driver for Java to connect to MySQL.

### Steps to Run
1. **Clone the Repository**
   ```bash
   git clone https://github.com/fatihaBizermimne/AttendanceSystemManagement.git
   cd AttendanceSystemManagement

    Database Setup

     Start the Apache and MySQL modules from your XAMPP Control Panel.

     Open phpMyAdmin (usually at http://localhost/phpmyadmin).

     Create a new database named attendance_system.

     Import the provided SQL file attendance_system.sql to create the necessary tables.

     Configure Database Connection

     Update the database connection settings (URL, username, password) in the src/utils/DatabaseConnection.java file (or the relevant DAO class) to match your local MySQL configuration.

     Add MySQL Connector/J to Classpath

     Ensure the mysql-connector-j-9.0+.jar file is added to your project's build path.

     Run the Application

Compile and run the src/ui/LoginFrame.java (or the main application class) from your IDE.

👤 Default Login Credentials

For testing purposes, you can use the following accounts:


| Role    | Username / PID | Password    |
| ------- | -------------- | ----------- |
| Teacher | T001           | password123 |
| Student | S001           | student123  |

