pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building...'
                dir('authenticationservice') {
                    catchError(buildResult: 'UNSTABLE', message: 'Authentication Service build failed') {
                        sh 'mvn clean install'
                    }
                }
                dir('filesystemservice') {
                    catchError(buildResult: 'UNSTABLE', message: 'Filesystem Service build failed') {
                        sh 'mvn clean install'
                    }
                }
                dir('uploadvideo') {
                    catchError(buildResult: 'UNSTABLE', message: 'Upload Video build failed') {
                        sh 'mvn clean install'
                    }
                }
                dir('videostreaming') {
                    catchError(buildResult: 'UNSTABLE', message: 'Video Streaming build failed') {
                        sh 'mvn clean install'
                    }
                }
            }
        }
        stage('Docker-Build') {
            steps {
                echo 'Building Docker...'
                script {
                    if (isUnix()) {
                        catchError(buildResult: 'FAILURE', message: 'Docker build failed') {
                            sh 'docker-compose build --no-cache'
                        }
                    } else {
                        catchError(buildResult: 'FAILURE', message: 'Docker build failed') {
                            bat 'docker-compose build --no-cache'
                        }
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying...'
                script {
                    if (isUnix()) {
                        catchError(buildResult: 'FAILURE', message: 'Docker-compose up failed') {
                            sh 'docker-compose up'
                        }
                    } else {
                        catchError(buildResult: 'FAILURE', message: 'Docker-compose up failed') {
                            bat 'docker-compose up'
                        }
                    }
                }
            }
        }
    }
}

def isUnix() {
    return System.properties['os.name'].toLowerCase().contains('nix') ||
           System.properties['os.name'].toLowerCase().contains('nux') ||
           System.properties['os.name'].toLowerCase().contains('mac')
}