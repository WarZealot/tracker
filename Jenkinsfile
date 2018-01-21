pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        bat(script: 'mvn build', returnStatus: true, returnStdout: true)
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deployed somewhere!'
      }
    }
  }
}