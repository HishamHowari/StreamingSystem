pipeline {
    agent any

    stages {
        stage('Build') {
            echo 'Building...'
            dir('authenticationservice') {
                sh 'mvn clean install'
            }
            dir('filesystemservice') {
                sh 'mvn clean install'
            }
            dir('uploadvideo') {
                sh 'mvn clean install'
            }
            dir('videostreaming') {
                sh 'mvn clean install'
            }
        }
        stage('Docker-Build') {
            steps {
                echo 'Building Docker...'
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