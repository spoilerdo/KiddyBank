pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh 'chmod +x ./gradlew test'
      }
    }
    stage('Build') {
      steps {
        sh 'chmod +x ./gradlew build'
      }
    }
  }
}