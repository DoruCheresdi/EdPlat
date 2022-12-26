# EducationPlatform

An educational platform web application created using Spring Framework,
Spring Boot, Spring Security, Spring Data and Thymeleaf.

Supports CRUD operations for users, courses, assignments and submissions.
Uses MySQL for data persistence.

# Deployment (currently at http://ec2-34-226-147-224.compute-1.amazonaws.com:8080/ )

To deploy the application to linux systems, use the deploy.sh and
remove-containers.sh scripts. What they do:
- remove containers if started previously
- pull the latest images from docker hub
- create a docker network between the mysql and the application containers
- create a mysql container with the database and the user/password
- create the container running a jar containing an embedded
web server and the application

It is recommended to change the root and springuser passwords or retrieve them
from a secrets vault.

If, when running the deploy script, the connection to the MySQL database is
refused, try increasing the wait time for the MySQL server initialization in
the script.
