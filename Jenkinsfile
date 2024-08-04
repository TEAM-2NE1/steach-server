pipeline {
    agent any
    environment {
        IMAGE_NAME = 'steach-server'
    }
    triggers {
        githubPush()
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    sshagent (credentials: ['staech-server-jen-ssh']) {
                        def branch = env.GIT_BRANCH ? env.GIT_BRANCH.replaceAll(/^origin\//, '') : 'main'
                        echo "Checking out branch: ${branch}"
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: "${branch}"]],
                            doGenerateSubmoduleConfigurations: false,
                            extensions: [[$class: 'SubmoduleOption',
                                          disableSubmodules: false,
                                          parentCredentials: true,
                                          recursiveSubmodules: true,
                                          reference: '',
                                          trackingSubmodules: false]],
                            userRemoteConfigs: [[
                                url: 'git@github.com:TEAM-2NE1/steach-server.git',
                                credentialsId: 'steach-server-jen-ssh'
                            ]]
                        ])
                    }
                }
            }
        }
        stage('Copy YML Files') {
            steps {
                script {
                    sh './gradlew copyYML'
                }
            }
        }
        stage('Verify Docker Installation') {
            steps {
                script {
                    sh 'docker --version'
                    sh 'docker ps'
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    docker.build("${IMAGE_NAME}:latest")
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    sh 'docker-compose -f docker-compose.prod.yml up -d'
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
