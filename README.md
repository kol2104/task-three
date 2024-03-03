## Task two

### Getting start

Build and run the application:
```
mvn clean package
```
```
java -jar ./target/task-three-0.0.1-SNAPSHOT.jar
```

### JaCoCo coverage report

Make JaCoCo tests coverage report with:

      mvn clean test jacoco:report

### SonarQube quality check

Check quality of code with:

      mvn clean verify sonar:sonar -Dsonar.token=<your_token>

### Feedback

- Was it easy to complete the task using AI?

Yes

- How long did task take you to complete? (Please be honest, we need it to gather anonymized statistics)

4 hours

- Was the code ready to run after generation? What did you have to change to make it usable?

In the majority

- Which challenges did you face during completion of the task?

Integrate with Google maps
