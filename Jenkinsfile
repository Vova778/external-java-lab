pipeline {
        agent any

        stages {
            stage('SonarQube Analysis') {
                steps {
                    withSonarQubeEnv(installationName:'sonar') {
                        sh "./gradlew sonarqube"
                    }
                }
            }
        }
    }