FROM maven:3.8.5-openjdk-18

RUN mkdir socialmedia

WORKDIR socialmedia

COPY pom.xml .

RUN mvn verify --fail-never

COPY . .

RUN mvn package -Dmaven.test.skip=true

CMD ["java", "-jar", "target/SocialMedia-1.0.jar"]
