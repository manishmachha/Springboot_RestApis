pipeline {
    agent any

    environment {
        IMAGE_NAME = 'my-spring-boot-app'
        CONTAINER_REGISTRY = 'manishmachha'
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://manishmachha: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@github.com/manishmachha/Springboot_RestApis.git'
            }
        }

        stage('Build JAR') {
                steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $CONTAINER_REGISTRY/$IMAGE_NAME .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([credentialsId: 'dockhub-pass', url: '']) {
                    sh 'docker push $CONTAINER_REGISTRY/$IMAGE_NAME'
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(['ec2-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ubuntu@13.48.136.66 '
                    docker pull manishmachha/my-spring-boot-app:latest &&
                    docker stop my-spring-boot-app || true &&
                    docker rm my-spring-boot-app || true &&
                    docker run -d -p 9090:9090 --name app manishmachha/my-spring-boot-app:latest
                    '
                    '''
                }
            }
        }
    }
}
