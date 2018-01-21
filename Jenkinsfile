pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'mvn build'
      }
    }
    stage('Deploy') {
      steps {
        echo 'Deployed somewhere!'
      }
    }
  }
}