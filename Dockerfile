# Step 1: Use openjdk image as a base image
FROM openjdk:21 as builder

# Step 2: Create a non-root user and group
RUN groupadd -r myuser && useradd -r -g myuser myuser

# Step 3: Set the working directory and ownership
WORKDIR /home/myuser
RUN chown -R myuser:myuser /home/myuser

# Change to non-root user
USER myuser

# Copy application files
COPY target/*.jar app.jar

# Extract layers
RUN java -Djarmode=layertools -jar app.jar extract

# Step 4: Use openjdk image as a base image
FROM openjdk:21

# Step 5: Create a non-root user and group
RUN groupadd -r myuser && useradd -r -g myuser myuser

# Step 6: Set the working directory and ownership
WORKDIR /home/myuser
RUN chown -R myuser:myuser /home/myuser

# Change to non-root user
USER myuser

# Copy extracted files from builder stage
COPY --from=builder /home/myuser/dependencies/ ./dependencies/
COPY --from=builder /home/myuser/spring-boot-loader/ ./spring-boot-loader/
COPY --from=builder /home/myuser/snapshot-dependencies/ ./snapshot-dependencies/
COPY --from=builder /home/myuser/application/ ./application/

# Expose the port
EXPOSE 8444

# Define the entry point
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
