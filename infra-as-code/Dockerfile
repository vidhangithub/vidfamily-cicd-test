FROM jenkins/jenkins:lts

USER root

# Install Maven
RUN apt-get update && apt-get install -y maven

# Install Docker CLI
RUN apt-get update && apt-get install -y docker.io


# Add the jenkins user to the docker group
RUN usermod -aG docker jenkins


USER jenkins