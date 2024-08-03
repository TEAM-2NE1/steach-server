pipeline {
    agent any // 어떤 노드에서나 실행 가능

    environment {
        IMAGE_NAME = 'steach-server' // Docker 이미지 이름 설정
    }

    stages {
        stage('Checkout') { // 코드 체크아웃 단계
            steps {
                git credentialsId: 'staech-server-jen', url: 'https://github.com/TEAM-2NE1/steach-server.git' // Git 저장소에서 코드 가져오기
            }
        }

        stage('Build') { // Docker 이미지 빌드 단계
            steps {
                script {
                    docker.build("${IMAGE_NAME}:latest") // Docker 이미지를 빌드하고 latest 태그 추가
                }
            }
        }

        stage('Deploy') { // Docker Compose를 사용하여 배포하는 단계
            steps {
                script {
                    sh 'docker-compose -f docker-compose.prod.yml up -d' // Docker Compose 파일을 사용하여 컨테이너 실행
                }
            }
        }
    }

    post {
        always {
            cleanWs() // 작업 공간 정리
        }
    }
}
