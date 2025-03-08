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
                    bat "docker run -d -p 9090:9090 --name app %CONTAINER_REGISTRY%/%IMAGE_NAME%:latest"
                }
            }
        }

        // stage('Deploy to EC2') {
        //     steps {
        //         sshagent(['ec2-key']) {
        //             sh '''
        //             ssh -o StrictHostKeyChecking=no ubuntu@13.48.136.66 '
        //             docker pull manishmachha/my-spring-boot-app:latest &&
        //             docker stop my-spring-boot-app || true &&
        //             docker rm my-spring-boot-app || true &&
        //             docker run -d -p 9090:9090 --name app manishmachha/my-spring-boot-app:latest
        //             '
        //             '''
        //         }
        //     }
        // }
    }
}
