pipeline {
    agent any
    stages {
        stage('scm checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/vidhangithub/vidfamily-cicd-test']])
            }
        }
        stage('maven build') {
            steps {
                 sh 'mvn clean install'
            }
        }
        stage('Build image') {
            steps {
                 sh 'docker build -t vidhanchandrauk/vid-family-service-1.0 .'
            }
        }
         stage('push image to hub') {
            steps {
                withCredentials([string(credentialsId: 'dockerhubpassword', variable: 'dockerhubupwd')]) {
                sh 'docker login -u vidhanchandrauk@gmail.com -p ${dockerhubpwd}'
                sh 'docker push vidhanchandrauk/vid-family-service-1.0'
                }
            }
        }
    }
}
