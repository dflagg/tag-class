#build
mvn clean package

#run locally docker
docker run -d -p 8080:8080 dgflagg/tag-class:latest

#run locally sans docker
java -jar target/tag-class-*.jar

#swagger
http://localhost:8080/tag-class/swagger-ui.html