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
                withDockerRegistry([credentialsId: 'docker-hub', url: '']) {
                    sh 'docker push $CONTAINER_REGISTRY/$IMAGE_NAME'
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(['ec2-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ubuntu@13.51.168.243 '
                    docker pull $CONTAINER_REGISTRY/$IMAGE_NAME &&
                    docker stop my-app || true &&
                    docker rm my-app || true &&
                    docker run -d -p 9090:9090 --name app $CONTAINER_REGISTRY/$IMAGE_NAME
                    '
                    '''
                }
            }
        }
    }
}
