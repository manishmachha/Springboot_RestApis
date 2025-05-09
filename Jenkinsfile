pipeline {
    agent any

    environment {
        IMAGE_NAME = 'my-spring-boot-app'
        CONTAINER_REGISTRY = 'manishmachha'
        EC2_USER = 'ubuntu'
        EC2_HOST = '3.95.63.94'
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
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $CONTAINER_REGISTRY/$IMAGE_NAME:latest .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry([credentialsId: 'docker-key', url: '']) {
                    sh 'docker push $CONTAINER_REGISTRY/$IMAGE_NAME:latest'
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(['ec2-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no $EC2_USER@$EC2_HOST <<EOF
                    docker pull $CONTAINER_REGISTRY/$IMAGE_NAME:latest
                    docker stop $IMAGE_NAME || true
                    docker rm $IMAGE_NAME || true
                    docker run -d -p 9090:9090 --name $IMAGE_NAME $CONTAINER_REGISTRY/$IMAGE_NAME:latest
EOF
            '''
                }
             }
        }
    }
}