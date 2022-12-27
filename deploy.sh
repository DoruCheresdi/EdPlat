# deployment script for the application on linux systems,
# needs the remove-containers.sh script too for restarting
# containers

# cleanup:
./remove-containers.sh

docker pull dorucheresdi/education-platform

docker pull mysql:latest

# create network:
docker network create edplat-net

# start a MYSQL server container with user and database:
if ! docker run --name edplat-mysql --network edplat-net \
                -e MYSQL_ROOT_PASSWORD=root -e MYSQL_USER=springuser \
                -e MYSQL_PASSWORD=ThePassword -e MYSQL_DATABASE=db_edplat \
                -d mysql:latest; then
    if ! docker start edplat-mysql; then
        echo "can't run or start mysql container, exiting..."
        exit
    fi
fi
echo "mysql container started"

# wait for the db to initialize:
sleep 10

# run application:
docker run --name edplat-container --network edplat-net \
-e SPRING_DATASOURCE_URL=jdbc:mysql://edplat-mysql:3306/db_edplat \
-e SPRING_DATASOURCE_USERNAME=springuser -e SPRING_DATASOURCE_PASSWORD=ThePassword \
-d -p 8080:8080 dorucheresdi/education-platform

# run this if tables are not already created:
# docker run --name edplat-container --network edplat-net -e SPRING_DATASOURCE_URL=jdbc:mysql://edplat-mysql:3306/db_edplat?createDatabaseIfNotExist=true \
# -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=root -p 8080:8080 dorucheresdi/education-platform

# debug:
# docker logs -f edplat-container
# docker logs -f edplat-mysql
