pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                dir('authenticationservice') {
                    catchError(buildResult: 'UNSTABLE', message: 'Authentication Service build failed') {
                        bat '"C:\\apache-maven-3.9.6\\bin\\mvn" clean install'
                    }
                }
                dir('filesystemservice') {
                    catchError(buildResult: 'UNSTABLE', message: 'Filesystem Service build failed') {
                        bat '"C:\\apache-maven-3.9.6\\bin\\mvn" clean install'
                    }
                }
                dir('uploadvideo') {
                    catchError(buildResult: 'UNSTABLE', message: 'Upload Video build failed') {
                        bat '"C:\\apache-maven-3.9.6\\bin\\mvn" clean install'
                    }
                }
                dir('videostreaming') {
                    catchError(buildResult: 'UNSTABLE', message: 'Video Streaming build failed') {
                        bat '"C:\\apache-maven-3.9.6\\bin\\mvn" clean install'
                    }
                }
            }
        }
        stage('Docker-Build') {
            steps {
                echo 'Building Docker...'
                catchError(buildResult: 'FAILURE', message: 'Docker build failed') {
                    bat 'docker-compose build --no-cache'
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
                catchError(buildResult: 'FAILURE', message: 'Docker-compose up failed') {
                    bat 'docker-compose up'
                }
            }
        }
    }
}