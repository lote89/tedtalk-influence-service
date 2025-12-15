TedTalk Influence Service:-
A Spring Boot backend service that imports TED Talks data and analyzes speaker influence based on engagement metrics such as views and likes.

The goal of this service is to:-
Import TED Talks data from a CSV file
Provide basic data management capabilities
Analyze speaker influence
Optionally determine the most influential TED Talk per year

Tech Stack:-
Java 17
Spring Boot
Spring Web
Spring Data JPA
H2 (in-memory database)
Maven

How to Run the Project:-

Prerequisites:-
Java 17+
Maven 3+

Run locally:-
mvn spring-boot:run

The application will start at:-
http://localhost:8080

H2 Console:-
http://localhost:8080/h2-console

JDBC URL:-
jdbc:h2:mem:testdb

1.Data Import:-
TED Talks data is imported from a provided CSV file.
Endpoint
POST /api/import


Accepts a CSV file via multipart upload,Parses TED Talk records,Persists them into the in-memory database
Example:
curl -F "file=@iO Data - Java assessment" http://localhost:8080/api/import

2.Data Management (CRUD)

Basic CRUD operations are provided to manage TED Talks after import.
Endpoints
GET	/api/talks	List all TED Talks
GET	/api/talks/{id}	Get a TED Talk by ID
POST	/api/talks	Create a TED Talk
PUT	/api/talks/{id}	Update a TED Talk (partial allowed)
DELETE	/api/talks/{id}	Delete a TED Talk

Example: Create a TED Talk
curl -X POST http://localhost:8080/api/talks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "The power of curiosity",
    "speaker": "Jane Doe",
    "year": 2019,
    "views": 150000,
    "likes": 3000,
    "url": "https://www.ted.com/talks/example"
  }'
  
3.Speaker Influence Analysis:-
Influence Definition
Speaker influence is calculated using a simple weighted formula:
influence score = (views × 0.7) + (likes × 0.3)

Rationale
Views represent reach
Likes represent engagement quality
The formula is intentionally simple and easy to reason about for an MVP

Speaker Influence Ranking
GET /api/influence: Returns a list of speakers ranked by aggregated influence score, including:
Total influence score
Total views
Total likes
Number of talks

Most influential talk for a specific year:-
GET /api/influence/year/{year}

Most influential talk per year (all years):-
GET /api/influence/by-year

Each TED Talk’s influence score is calculated using the same formula, and the top talk per year is selected.

Design Decisions & Assumptions:-

  1.The choices below were made intentionally to balance clarity, reviewability, and time constraints of the assignment.
  2.MVP Scope:-
              The project was scoped as a Minimum Viable Product.
  3.Focus was placed on correctness, readability, and clear separation of responsibilities.
  4.Persistence:-
              An H2 in-memory database was chosen to avoid external dependencies and simplify setup.
  5.DTO Design (Java Records):- DTOs are implemented using Java record classes,Records clearly express immutability and intent,Validation annotations are applied      directly to record components to enforce API-level constraints.
  6.Validation:-Input validation is performed at the API boundary using Jakarta Bean Validation,Invalid requests return 400 Bad Request.
  7.Influence Analysis:-Influence is not normalized by year,Older talks with very high view counts may dominate rankings,This was accepted as a reasonable trade-      off for MVP clarity.
  8.Error Handling:-Simple HTTP status-based error handling (404 for missing resources),No custom exception hierarchy was added to keep the solution lightweight.
  9.Testing:-Automated tests are intentionally omitted due to time constraints.









