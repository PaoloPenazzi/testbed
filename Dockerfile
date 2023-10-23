# Lightweight Linux image
FROM alpine:latest
# Install JDK 17
RUN apk update ; apk add openjdk17
# Install bash curl git zip unzip
RUN apk add bash curl git zip unzip
# Install Kotlin
RUN curl -s https://get.sdkman.io | bash
# Install SDK
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh"
# Install Kotlin
# Create environment variable for container working dir path
ENV WORKINGDIR=/home/gradle/src
# Change working dir
WORKDIR ${WORKINGDIR}
# Create dir (if dosen't exist) and concede full rights
RUN mkdir -p ${WORKINGDIR} && chmod +x ${WORKINGDIR}
# Copy the Gradle config, source code, and static analysis config
# into the build container. Without --chown gradle throws a permission error
COPY --chown=gradle:gradle . ${WORKINGDIR}
# Expose 8080 port
EXPOSE 8080
# Run the application
RUN ./gradlew build ; ./gradlew run