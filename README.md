## Task Notification System - Spring Boot
This is a task notification system built using Spring Boot, designed to provide real-time notifications for tasks assigned to users. The application runs on a virtual machine (VM) and utilizes WebSockets, a database, caching, and a front-end for displaying notifications.

# Table of Contents
* Overview
* Setup Instructions
* Design Decisions
* Architecture Diagram


# Overview


The Task Notification System allows users to receive real-time notifications about the tasks assigned to them. 
The system is composed of a backend Spring Boot application that interacts with WebSockets for real-time communication, a database for storing task data, and a caching layer to optimize performance. 
The front-end interface listens for task notifications and displays them to the user as they occur.

The application is designed to be run on a virtual machine (VM), providing flexibility and scalability for deployment.

# Setup Instructions

# Prerequisites

* Java 21 or later
* Maven (or Gradle)
* A running virtual machine with access to the internet
* Database (PostgreSQL) setup


# Step-by-Step Guide
# Navigate to VM with your access(ssh):

* e.g ssh username@your.ip
* locate the repos or git clone https://github.com/abelmasingita/task-notification-system-backend.git
* cd task-notification-system-backend

# Install dependencies: If you're using Maven, run:

* mvn spring-boot:run

* Access the application: After the application starts, you can access the back-end through the provided URL (e.g., http://45.220.164.81:8080) on your VM.

# Test the system:

Test real-time notifications by creating tasks and observing notifications in the front-end interface.


# Design Decisions

* WebSocket for Real-Time Communication: I opted for WebSockets to provide low-latency, bi-directional communication between the backend and the front-end. This allows for real-time updates whenever a task is assigned or updated.


* Database Choice: We use a relational database (PostgreSQL) to persist task-related data. The choice of a relational database allows for structured storage of task information and supports SQL queries for analytics.

* Caching: To improve performance, caching is used to store frequently accessed data (such as notifications). This minimizes the number of database calls and speeds up the response time for users.


* Backend Framework: Spring Boot was chosen for its simplicity, scalability, and the ease with which we can integrate WebSocket support. It provides a robust foundation for the API and WebSocket services.

* Frontend Integration: The frontend communicates with the backend through WebSockets to receive real-time notifications. We use modern JavaScript frameworks (React (NextJs)) on the front end to manage UI updates and handle WebSocket connections.

# Trade-offs & Limitations:

* Scalability: Although WebSockets provide real-time communication, managing WebSocket connections at scale (e.g., with a large number of concurrent users) can be challenging. Load balancing and ensuring consistent session management across multiple instances is required.
* Session Persistence: The application relies on WebSocket sessions to deliver notifications. If the WebSocket connection is lost, notifications may be missed unless implemented with additional mechanisms like or persistent storage.
* VM Resource Constraints: Running the application on a virtual machine means the system's performance is dependent on the resources available on the VM. Scaling may require upgrading the VM or deploying the system in a distributed cloud environment.

# Architecture Diagram
Below is the high-level architecture of the Task Notification System, showing the flow between the different components:

![Architecture Diagram](https://github.com/user-attachments/assets/e6986430-d5ac-4a91-b1cd-f37582ee2f7e)

* Front-End (UI): The front-end is responsible for displaying real-time task notifications and interacting with the backend.
* Spring Boot App (WebSocket Server): The backend application handles notification logic, WebSocket communication, and integrates with the database and caching layer.
* Caching Layer ( Redis): Frequently accessed task data is stored in a caching system to improve performance and reduce database load.
* Database: The database persists all notification-related data and user information.


# APIS

* Register -Create New User

![image](https://github.com/user-attachments/assets/c9af3132-7fe3-4738-ae57-736deb79baa9)


* Login - Get Auth Token

![image](https://github.com/user-attachments/assets/b750223e-3a6a-47f5-8cf5-9e06c0858b02)

* Get All Notifications

![image](https://github.com/user-attachments/assets/074a7dc8-c840-416d-ab0b-351c1a456905)

* Get Preferences -  Notification Settings

![image](https://github.com/user-attachments/assets/02c3a6bb-3a85-46cf-85fe-9e380f666bc4)


* Add Notifications

![image](https://github.com/user-attachments/assets/0ddbd4d5-2561-41c9-87d2-2e843495fbd1)

* Add Preferenes

![image](https://github.com/user-attachments/assets/bf9ed0c3-b8a1-4161-b60a-9333b0ea73b2)



