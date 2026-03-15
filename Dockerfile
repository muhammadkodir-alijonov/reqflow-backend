FROM eclipse-temurin:21-jre

WORKDIR /work/

COPY target/quarkus-app/lib/ /work/lib/
COPY target/quarkus-app/*.jar /work/
COPY target/quarkus-app/app/ /work/app/
COPY target/quarkus-app/quarkus/ /work/quarkus/

EXPOSE 6060

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /work/quarkus-run.jar"]
