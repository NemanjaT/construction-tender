# Getting Started

### Starting Application in development mode
Notes before starting the application:
* Required version of Java is 14.
* Application context path is `/tender-api`
* Embedded H2 database is used for local environment.
* H2 console is enabled and accessible at `/tender-api/h2-console`
* Swagger is enabled and accessible at `/tender-api/swagger-ui/index.html`

To start the application in development mode (local profile) via console run the application via command:
* `mvn spring-boot:run -Dspring-boot.run.profiles=local`

To start the application using IDE, run the application and set the following:
* Set the main application as `com.construction.tender.Application`
* Set profiles to `local`
* Set the active module as tender-api if required (IntelliJ).
* Run the application as a Spring Boot or normal Java application.
