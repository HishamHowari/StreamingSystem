pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                bat 'docker-compose build --no-cache'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
                bat 'docker-compose up'
            }
        }
    }
}