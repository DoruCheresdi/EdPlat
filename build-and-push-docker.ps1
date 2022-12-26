./mvnw package -DskipTests
docker build -t dorucheresdi/education-platform .
docker push dorucheresdi/education-platform
