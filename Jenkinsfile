pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        APIFOOTBALL_API_KEY = credentials('APIFOOTBALL_API_KEY')
        IMAGE_NAME = "footballapi:${env.BUILD_NUMBER}"
        CONTAINER_NAME = "footballapi"
    }

    stages {
        // stage('Checkout') {
        //     steps {
        //         sh 'git clone -b feature/foot-ball-standings-api https://github.com/girishKM/football-standings.git .'
        //     }
        // }
        stage('Clone Repo') {
            steps {
                git branch: 'feature/foot-ball-standings-api', url: 'git clone https://github.com/girishKM/football-standings.git .'
                dir('repo') {
                    git 'https://github.com/girishKM/football-standings.git'
                }
            }
        }

        stage('Build') {
            steps {
                sh 'docker run --rm -v $PWD:/app -w /app maven:3.9.6-eclipse-temurin-17 mvn clean package -DskipTests'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }
        stage('Docker Deploy') {
            steps {
                sh "docker rm -f ${CONTAINER_NAME} || true"
                sh "docker run -d --name ${CONTAINER_NAME} -e APIFOOTBALL_API_KEY=${APIFOOTBALL_API_KEY} -p 8080:8080 ${IMAGE_NAME}"
            }
        }
    }
}