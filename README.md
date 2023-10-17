#### This repository created for educational purposes. The end goal was to create a Gift Certificate Store web application.
> You can see the full Backend in branch [master](https://github.com/Vova778/external-java-lab/tree/master)

> You can also see the Front by clicking on the following link. [Gift Certificate Store (Frontend)](https://github.com/Vova778/certificate-store-frontend)

# Gift Certificate Store (Backend)

## Description

REST API Service for Gift Certificate Store.

## Application requirements

* JDK version: 15.
* Application packages root: com.epam.esm.
* Java Code Convention is mandatory (exception: margin size –120 characters).
* Gradle, latest version. Multi-module project.
* Spring Framework, the latest version.
* Database: PostgreSQL/MySQL, latest version.
* Testing: JUnit, the latest version, Mockito.
* Repository layer should be tested using integration tests with an in-memory embedded database (all operations with certificates) only for module_2.
* Service layer should be covered with unit tests not less than 80%.
* JPA & Hibernate (module_3 branch):
    * Hibernate should be used as a JPA implementation for data access.
    * Spring Transaction should be used in all necessary areas of the application.
    * Audit data should be populated using JPA features.
* Spring Data (module_4+ branches).
* Pagination, Sorting, Filtering and HATEOAS.
* Spring Security framework.
* Application should support only stateless user authentication and verify integrity of JWT token.
* CI-CD:
    * Code analysis tool: SonarQube.
    * Jenkins security (install Role strategy plugin). Remove anonymous access. Create administrator user (all permissions) and developer user (build job, cancel builds).
    * Jenkins build job (pool, run test, build) to checkout your repository, use pooling interval.
    * Jenkins should use local SonarQube installation. Analyze source code with SonarQube after Maven builds your project. Use JaCoCo for code coverage.
    * Jenkins should deploy application (after passing SonarQube quality gate) under local tomcat server.

User Permissions:
```
 - Guest:
    * Read operations for main entity.
    * Signup.
    * Login.
 - User:
    * Make an order on main entity.
    * All read operations.
 - Administrator (can be added only via database call):
    * All operations, including addition and modification of entities.
```
## General requirements

* Code should be clean and should not contain any “developer-purpose” constructions.
* App should be designed and written with respect to OOD and SOLID principles.
* Code should contain valuable comments where appropriate.
* Public APIs should be documented (Javadoc).
* Clear layered structure should be used with responsibilities of each application layer defined.
* JSON should be used as a format of client-server communication messages.
* Convenient error/exception handling mechanism should be implemented: all errors should be meaningful and localized on backend side.
* Abstraction should be used everywhere to avoid code duplication.
* Several configurations should be implemented.
* Jenkins should be integrated with SonarQube.