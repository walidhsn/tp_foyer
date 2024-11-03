FROM openjdk:17-jdk

# Create a non-root user and group
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

EXPOSE 8089

ENV APP_HOME /usr/src/app

COPY target/*.jar $APP_HOME/app.jar

WORKDIR $APP_HOME

# Change ownership of the app directory
RUN chown -R appuser:appgroup $APP_HOME

# Switch to the non-root user
USER appuser

CMD ["java", "-jar", "app.jar"]