pipeline {
    agent any

    environment {
        IMAGE_NAME = 'my-spring-boot-app'
        CONTAINER_REGISTRY = 'manishmachha'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git(
                    branch: 'main',
                    credentialsId: 'github-key',
                    url: 'https://github.com/manishmachha/Springboot_RestApis.git'
                )
            }
        }

        stage('Build JAR') {
            steps {
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat "docker build -t %CONTAINER_REGISTRY%/%IMAGE_NAME%:latest ."
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([credentialsId: 'docker-key']) {
                    bat "docker push %CONTAINER_REGISTRY%/%IMAGE_NAME%:latest"
                }
            }
        }

        stage('Run Docker Image') {
            steps {
                bat """
                    docker pull %CONTAINER_REGISTRY%/%IMAGE_NAME%:latest
                    docker stop app || true
                    docker rm app || true
                    docker run -d -p 9090:9090 --name app %CONTAINER_REGISTRY%/%IMAGE_NAME%:latest
                """
            }
        }
    }
}
