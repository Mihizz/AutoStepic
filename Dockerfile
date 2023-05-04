FROM openjdk:8-alpine

COPY target/uberjar/autostepic.jar /autostepic/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/autostepic/app.jar"]
