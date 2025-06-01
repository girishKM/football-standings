pipeline {
    agent {
        // Use the Docker daemon of the host Jenkins is running on
        docker { image 'maven:3.9.6-eclipse-temurin-17' }
    }

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
                script {
                    // Use the host Docker daemon (Jenkins must be started with -v /var/run/docker.sock:/var/run/docker.sock)
                    dockerImage = docker.build("${IMAGE_NAME}")
                }
            }
        }
        stage('Docker Deploy') {
            steps {
                script {
                    // Stop and remove any existing container
                    sh "docker rm -f ${CONTAINER_NAME} || true"
                    // Run the new container
                    sh "docker run -d --name ${CONTAINER_NAME} -e APIFOOTBALL_API_KEY=${APIFOOTBALL_API_KEY} -p 8080:8080 ${IMAGE_NAME}"
                }
            }
        }
    }
    post {
        always {
            junit '**/target/surefire-reports/*.xml'
        }
    }
}