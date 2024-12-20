# HealthTracker Backend

Welcome to the HealthTracker Backend! This is the server-side application that powers the HealthTracker system. The HealthTracker app helps users monitor and improve their well-being by tracking key health metrics like exercise, diet, sleep, screen time, water, mental health, and walk. With this backend, users can set personalized health goals, track their progress, and receive tailored suggestions based on their activity and performance.

### Below is the deployed view of the backend running

![image](https://github.com/user-attachments/assets/7c274e6a-b00f-4ce7-b484-5e7437e0fecb)


## Features

So far, the application includes the following key features:

- **Activity Tracking**: Track workouts, exercises, and daily physical activity to monitor progress.
- **Health Goal Setting**: Users can set specific health-related goals and track their achievement over time.
- **Diet Tracking**: Log meals, track daily nutrition, and set daily dietary goals to improve eating habits.
- **Sleep Tracking**: Monitor sleep duration and quality, and set goals for healthy sleep habits.
- **Screen Time Tracking**: Keep track of daily screen time, set usage limits, and promote healthier digital habits.
- **Water Tracking**: Set the daily goal for the water intake, log the actual drink water, and check your performance.
- **Mental Health**: Keep track of Mental Health Goals, set and view the performance of the mental health goals.
- **Walk**: Set the walking goals (steps), log the actual steps, and track actual performance. 
  

Each of these features works together to provide users with a comprehensive understanding of their overall health.

## Tech Stack

The **HealthTracker Backend** is built using the following technologies:

### Framework
- **Javalin** (Kotlin-based)  
  Javalin is a lightweight framework designed for building REST APIs. 

### Database
- **PostgreSQL**  
  We use PostgreSQL, an open-source relational database, for efficient and reliable storage of user data, health logs, and other necessary information.

### ORM
- **Exposed ORM**  
  Exposed is a Kotlin-based ORM that provides a type-safe way to interact with the database. 

### Build Tool
- **Maven**  
  Maven manages project dependencies, builds the project, and runs tests. It ensures a consistent build process across all environments.

## API Testing
Swagger API documentation is added to the project via the file named health-tracker-rest.yaml

There is also a deployed version of the postman file that can be found on this link: https://app.swaggerhub.com/apis/ISRARAHMEDPK000/health-tracker_api/1.0.0#/Users/get_api_users__user_id_

## Prerequisites

Make sure you have the following tools installed:

- **IntelliJ IDEA**  
- **Maven**  
- **PostgreSQL** (or use Docker for local setup)




