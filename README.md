# Mood Tracker API

A Spring Boot application for tracking daily moods with REST API endpoints.

## Features

- Add a mood entry
- View mood history
- Edit a mood entry
- View weekly mood summary
- Delete mood entries

## API Endpoints

### Add Mood
```http
POST /api/moods
```
Request body:
```json
{
    "date": "2023-11-14",
    "mood": "Happy",
    "notes": "Had a great day!"
}
```

### Get All Moods
```http
GET /api/moods
```

### Get Mood by ID
```http
GET /api/moods/{id}
```

### Get Mood by Date
```http
GET /api/moods/date/{date}
```

### Update Mood
```http
PUT /api/moods/{id}
```
Request body:
```json
{
    "date": "2023-11-14",
    "mood": "Excited",
    "notes": "Updated notes!"
}
```

### Delete Mood
```http
DELETE /api/moods/{id}
```

### Get Weekly Moods
```http
GET /api/moods/weekly
```

### Get Weekly Summary
```http
GET /api/moods/weekly/summary
```

## Setup Instructions

1. Ensure you have Java 17 or later installed
2. Clone the repository
3. Build the project using Maven or your IDE
4. Run the application
5. Access the API at `http://localhost:8080`
6. Access H2 Console at `http://localhost:8080/h2-console` (development only)

## Running the CLI
1. First, start the Spring Boot application (REST API server)
```
./gradlew bootRun
```
2. Then, in a separate terminal, run the CLI client:
```
./gradlew runCli
```

## Database Configuration

The application uses H2 database with the following configuration:
- URL: jdbc:h2:file:./moodtracker
- Username: sa
- Password: password

## Technologies Used

- Spring Boot 3.1.5
- Spring Data JPA
- H2 Database
- Lombok
- Java 17
