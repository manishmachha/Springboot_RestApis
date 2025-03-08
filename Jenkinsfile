pipeline {
    agent any

    environment {
        IMAGE_NAME = 'my-spring-boot-app'
        CONTAINER_REGISTRY = 'manishmachha'
        EC2_USER = 'ubuntu'
        EC2_HOST = '13.48.136.66'
        PRIVATE_KEY_PATH = 'C:\\Users\\your-username\\.ssh\\ec2-key.pem' // Update this path
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', 
                    credentialsId: 'github-key',
                    url: 'https://github.com/manishmachha/Springboot_RestApis.git'
            }
        }

        stage('Build JAR') {
            steps {
                bat 'mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat "docker build -t %CONTAINER_REGISTRY%/%IMAGE_NAME%:latest ."
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([credentialsId: 'docker-key', url: '']) {
                    bat "docker push %CONTAINER_REGISTRY%/%IMAGE_NAME%:latest"
                }
            }
        }

        stage('Run Docker Image') {
            steps {
                bat """
                        docker pull %CONTAINER_REGISTRY%/%IMAGE_NAME%:latest ^&^
                        docker stop %IMAGE_NAME% ^|^| exit 0 ^&^
                        docker rm %IMAGE_NAME% ^|^| exit 0 ^&^
                        docker run -d -p 9090:9090 --name app %CONTAINER_REGISTRY%/%IMAGE_NAME%:latest
                """
            }
        }
    }
}
