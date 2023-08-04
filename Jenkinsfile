pipeline {
        agent any

        stages {
            stage('Build') {
                steps {
                    git branch: 'master', url: 'https://github.com/Vova778/external-java-lab.git'
                    sh './gradlew clean compileJava'
                }
            }
            stage('Test') {
                steps {
                    sh './gradlew test'
                }
            }
            stage('SonarQube Analysis') {
                steps {
                    withSonarQubeEnv(installationName:'sq1') {
                        sh "./gradlew sonarqube"
                    }
                }
            }
        }
    }