pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        APIFOOTBALL_API_KEY = credentials('APIFOOTBALL_API_KEY')
        IMAGE_NAME = "footballapi_%BUILD_NUMBER%"
        CONTAINER_NAME = "footballapi"
    }

    stages {
        stage('Checkout') {
            steps {
                deleteDir()
                bat 'git clone -b feature/foot-ball-standings-api https://github.com/girishKM/football-standings.git .'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
        stage('Docker Build') {
            steps {
                bat "docker build -t %IMAGE_NAME% ."
            }
        }
        stage('Docker Deploy') {
            steps {
                bat "docker rm -f %CONTAINER_NAME% || exit 0"
                bat "docker run -d --name %CONTAINER_NAME% -e APIFOOTBALL_API_KEY=%APIFOOTBALL_API_KEY% -p 8080:8080 %IMAGE_NAME%"
            }
        }
    }
}