FROM tomcat:8.5.82-jre8-openjdk-buster
ADD target/com.zensar.zenconverseassist-v1.war com.zensar.zenconverseassist-v1.war
COPY com.zensar.zenconverseassist-v1.war /usr/local/tomcat/webapps
