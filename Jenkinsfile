pipeline {
    agent any // 어떤 노드에서나 실행 가능
// 작업 하기
    environment {
        IMAGE_NAME = 'steach-server' // Docker 이미지 이름 설정
    }

    triggers {
        githubPush() // GitHub 푸시 이벤트 트리거
    }
    stages {
        stage('Checkout') { // 코드 체크아웃 단계
//             steps {
//                 git credentialsId: 'staech-server-jen', url: 'https://github.com/TEAM-2NE1/steach-server.git' // Git 저장소에서 코드 가져오기
//             }
            steps {
                script {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: 'jen']],
                        userRemoteConfigs: [[
                            url: 'https://github.com/TEAM-2NE1/steach-server.git',
                            credentialsId: 'staech-server-jen'
                        ]]
                    ])
                }
            }
        }


        stage('Verify Docker Installation') {
            steps {
                script {
                    sh 'docker --version' // Docker가 설치되어 있고, 명령어가 올바르게 작동하는지 확인합니다.
                    sh 'docker ps' // Docker 데몬에 접근할 수 있는지 확인
                }
            }
        }

        stage('Build') { // Docker 이미지 빌드 단계
            steps {
                script {
                    sh './gradlew clean build' // Gradle 빌드 수행
                    docker.build("${IMAGE_NAME}:latest") // Docker 이미지를 빌드하고 latest 태그 추가
                }
            }
        }

        stage('Deploy') { // Docker Compose를 사용하여 배포하는 단계
            steps {
                script {
                    // 필요한 경우, Docker Compose 파일 경로를 명확히 지정
                    sh 'docker-compose down' // 기존 컨테이너 종료
                    sh 'docker-compose -f docker-compose.prod.yml up -d' // Docker Compose 파일을 사용하여 컨테이너 실행
                }
            }
        }
    }

    post {
        always {
            sh 'docker-compose logs' // Docker Compose 로그 출력
            cleanWs() // 작업 공간 정리
        }
    }
}
