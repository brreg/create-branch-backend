FROM maven as builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:resolve --batch-mode --no-transfer-progress --quiet

COPY src ./src

RUN mvn install -Dmaven.test.skip=true --batch-mode --no-transfer-progress --quiet


FROM eclipse-temurin:21-jre-noble

WORKDIR /app

COPY --from=builder /app/target/create-branch-backend-1.jar .

ENTRYPOINT ["java","-jar","/app/create-branch-backend-1.jar"]