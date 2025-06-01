pipeline {
    agent any

    environment {
        APIFOOTBALL_API_KEY = credentials('APIFOOTBALL_API_KEY')
        IMAGE_NAME = "footballapi:${env.BUILD_NUMBER}"
        CONTAINER_NAME = "footballapi"
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'github-creds', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                        sh 'git clone https://$GIT_USERNAME:$GIT_PASSWORD@github.com/your-username/your-repo.git .'
                    }
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
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
    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
    }
}