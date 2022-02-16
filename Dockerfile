FROM maven:3-jdk-8-alpine as builder

WORKDIR /workspace/app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src src

RUN mvn clean package
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Our final base image
FROM openjdk:8-jre-alpine

ARG DEPENDENCY=/workspace/app/target/dependency

COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

EXPOSE 8082

ENTRYPOINT ["java","-cp","app:app/lib/*", "com.ostack.bankaccount.BankAccountApplication"]