FROM openjdk:8
MAINTAINER dgflagg

#dir in docker image where app will be run from
ENV DOCKER_APP_HOME /usr/local/tag-class
#version agnostic name reference of the jar that will be installed
ENV JAR_VERSION_NAME tag-class-*.jar
#set working dir to the dir jar will be installed and app will be run from
WORKDIR DOCKER_APP_HOME
#adds the jar from the local target build dir to the docker app home dir
ADD target/$JAR_VERSION_NAME .
#copy the jar to a jar without the version number
RUN cp $JAR_VERSION_NAME tag-class.jar
#remove the jar with the version number
RUN rm $JAR_VERSION_NAME
#run the jar
CMD ["java", "-jar", "tag-class.jar"]