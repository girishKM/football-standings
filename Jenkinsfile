pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        APIFOOTBALL_API_KEY = credentials('APIFOOTBALL_API_KEY')
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
                bat "docker build -t footballapi_${env.BUILD_NUMBER} ."
            }
        }
        stage('Docker Deploy') {
            steps {
                bat "docker rm -f footballapi || exit 0"
                bat "docker run -d --name footballapi -e APIFOOTBALL_API_KEY=${env.APIFOOTBALL_API_KEY} -p 8081:8080 footballapi_${env.BUILD_NUMBER}"
            }
        }
    }
}