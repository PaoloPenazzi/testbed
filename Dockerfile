# Use the base Kotlin image from danysk/kotlin
FROM danysk/kotlin

# Set the working directory in the container
WORKDIR /app

# Copy your Kotlin application files to the container
COPY . /app

RUN cd app/ ; chmod +x gradlew

# Run the Gradle build
RUN ./gradlew build

# Command to run your application
CMD ["java", "-jar", "build/libs/testbed-0.1.0.jar"]