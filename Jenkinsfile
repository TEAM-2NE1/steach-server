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
                        doGenerateSubmoduleConfigurations: false, // 서브모듈 설정을 자동으로 생성하지 않음
                        extensions: [[$class: 'SubmoduleOption', recursiveSubmodules: true]], // 서브모듈을 재귀적으로 초기화 및 업데이트
                        userRemoteConfigs: [[
                            url: 'https://github.com/TEAM-2NE1/steach-server.git',
                            credentialsId: 'staech-server-jen'
                        ]]
                    ])
                }
            }
        }

//         서브모듈이 포함된 프로젝트의 모든 서브모듈을 초기화하고 최신 상태로 업데이트하는 것을 의미합니다.
        stage('Update Submodules') { // 서브모듈 업데이트 단계
            steps {
                script {
                    sh 'git submodule update --init --recursive' // 서브모듈 초기화 및 업데이트
                }
            }
        }

        stage('Copy YML Files') { // YML 파일 복사 단계
            steps {
                script {
                    sh './gradlew copyYML' // Gradle 태스크 실행
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
//                     sh 'which docker' // Docker가 설치되어 있는지 확인
                    docker.build("${IMAGE_NAME}:latest") // Docker 이미지를 빌드하고 latest 태그 추가
                }
            }
        }

        stage('Deploy') { // Docker Compose를 사용하여 배포하는 단계
            steps {
                script {
                    // 필요한 경우, Docker Compose 파일 경로를 명확히 지정
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
