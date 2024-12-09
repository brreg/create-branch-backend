FROM maven as builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:resolve --batch-mode --no-transfer-progress --quiet

COPY src ./src

RUN mvn install -Dmaven.test.skip=true --batch-mode --no-transfer-progress --quiet


FROM eclipse-temurin:21-jre-noble

WORKDIR /app

COPY --from=builder /app/target/create-branch-backend-1.jar .

COPY certs /opt/certs

# Import certificates into the Java cacerts truststore
RUN keytool -importcert -file /opt/certs/MicrosoftRSA2017.crt -alias microsoftroot -cacerts -storepass changeit -noprompt \
 && keytool -importcert -file /opt/certs/DigiCertGlobalRootG2.crt.pem -alias digicertg2 -cacerts -storepass changeit -noprompt \
 && keytool -importcert -file /opt/certs/DigiCertGlobalRootCA.crt -alias digicertca -cacerts -storepass changeit -noprompt

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/create-branch-backend-1.jar"]