pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh '''chmod +x gradlew
./gradlew test'''
      }
    }
    stage('Build') {
      steps {
        sh '''chmod +x gradlew
./gradlew build'''
      }
    }
  }
  post {
    always{
        junit 'Services/build/test-results/test/TEST-com.kiddybank.LogicTests.AccountLogicTest.xml'
        junit 'Services/build/test-results/test/TEST-com.kiddybank.LogicTests.BankLogicTest.xml'
    }
  }
}
