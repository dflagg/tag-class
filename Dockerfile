FROM openjdk:8
MAINTAINER dgflagg

ENV TAG_APP_HOME /usr/local/tag-class
RUN mkdir "$TAG_APP_HOME"
ADD target/tag-class-1.0-SNAPSHOT.jar $TAG_APP_HOME
WORKDIR $TAG_APP_HOME
CMD ["java", "-jar", "tag-class-1.0-SNAPSHOT.jar"]