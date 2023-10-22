# Getting Started

1. Either start the compose file that is provided in the project or use your installed PostgreSQL. Easier to start the
compose file tbh, easier to run the migrations as well.
2. Run the migrations from CMD. **mvn migration:up -Dmigration.env=postgresql**
3. Start the application.
4. For DB integration tests, Docker is required. The DB is setup by Testcontainers.