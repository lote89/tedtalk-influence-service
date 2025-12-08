# Simple TED Talks Influence API

This is a minimal Spring Boot application created for the iO Java Assessment.  
It focuses on simplicity and clarity to stay within the 4-hour guideline.

## Features

- Import a CSV containing TED Talk data:
  - title
  - author (mapped to speaker)
  - date
  - views
  - likes
  - link

- Store talks in an in-memory H2 database (no setup needed)

- Calculate a simple influence score:
  score = views * 0.7 + likes * 0.3
  
- Expose three REST endpoints:

| Method | Endpoint         | Description               |
|--------|------------------|---------------------------|
| POST   | /api/import      | Upload a CSV file         |
| GET    | /api/talks       | List all TED Talks        |
| GET    | /api/influence   | Speaker influence ranking |

## Assumptions

- CSV headers match the provided dataset  
- author is mapped internally to the domain field speaker 
- Date field may be missing or unable to parse  
- Views/likes may contain commas or characters → cleaned automatically  
- Focus is on clarity over complexity due to the timebox  

## How to Run 

If you want to run the project locally:
mvn spring-boot:run

The API will be available at:
http://localhost:8080/api

##  Why this design?

- **Simplicity:** Lightweight implementation that solves the core requirements  
- **Records:** Clean and minimal DTOs  
- **H2 Database:** Zero configuration, ideal for assessments  
- **Straightforward influence formula:** Easy to justify and explain  

## Note

The CSV file is **not** included in the project.  
It is uploaded by the user at runtime via the /api/import endpoint.

## Not Included

To keep the solution within the 4-hour timebox, the following items were not included:

- Postman collection  
- Automated test cases  

The API is simple enough to test manually using curl or any REST client, and the
core focus was placed on CSV import, data storage, and influence calculation.






