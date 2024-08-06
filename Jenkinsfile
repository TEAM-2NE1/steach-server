pipeline {
    agent any // 어떤 노드에서나 실행 가능

    environment {
        IMAGE_NAME = 'steach-server' // Docker 이미지 이름 설정
    }

    triggers {
        githubPush() // GitHub 푸시 이벤트 트리거
    }
    stages {
        stage('Checkout') { // 코드 체크아웃 단계
            steps {
                script {
                    // BRANCH_NAME 변수는 Jenkins가 자동으로 설정해주는 환경 변수로, 빌드 트리거된 브랜치의 이름을 가집니다.
                    // 그러나, 이 변수가 자동으로 설정되지 않는 경우도 있으므로, 이를 명시적으로 설정해야 할 수 있습니다.
                    def branch = env.GIT_BRANCH ? env.GIT_BRANCH.replaceAll(/^origin\//, '') : 'main'
                    echo "Checking out branch: ${branch}" // 변경된 브랜치 표시
                    checkout([
                        $class: 'GitSCM',
                    // [[name: "${branch}"]]: 이 부분은 branches 키워드의 값으로, 실제로 체크아웃할 브랜치를 나타냅니다. 리스트 내의 딕셔너리 형태로 작성됩니다.
                        branches: [[name: "${branch}"]], // 변경된 브랜치를 지정
                        userRemoteConfigs: [[
                            url: 'https://github.com/TEAM-2NE1/steach-server.git',
                            credentialsId: 'steach-server-jen'
                        ]]
                    ])
                }
                // ====================Pipeline Syntax로 얻은 script코드 ==================
                script {
                    checkout scmGit(
                        // BRANCH_NAME 변수는 Jenkins가 자동으로 설정해주는 환경 변수로, 빌드 트리거된 브랜치의 이름을 가집니다.
                        // 그러나, 이 변수가 자동으로 설정되지 않는 경우도 있으므로, 이를 명시적으로 설정해야 할 수 있습니다.
                        def branch = env.GIT_BRANCH ? env.GIT_BRANCH.replaceAll(/^origin\//, '') : 'main'
                        echo "Checking out branch: ${branch}" // 변경된 브랜치 표시
                        branches: [[name: "${branch}"]], // 변경된 브랜치를 지정
                        extensions: [submodule(parentCredentials: true, recursiveSubmodules: true, reference: '', trackingSubmodules: true)],
                        userRemoteConfigs: [[url: 'https://github.com/TEAM-2NE1/steach-server.git']]
                    )
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
                    sh 'chmod +x ./gradlew' // gradlew 파일에 실행 권한 추가
                    sh './gradlew --no-daemon clean build'
                    echo 'build Image'
                    docker.build("${IMAGE_NAME}:latest") // Docker 이미지를 빌드하고 latest 태그 추가
                }
            }
        }

        stage('Deploy') { // Docker Compose를 사용하여 배포하는 단계
            steps {
                script {
                    sh 'docker-compose --version'
                    // 필요한 경우, Docker Compose 파일 경로를 명확히 지정
                    sh 'pwd'  // 현재 작업 디렉토리 출력 /var/jenkins_home/workspace/steach-server-webhook
                    sh 'ls -la'  // 현재 디렉토리의 파일 목록 출력
                    sh 'cat ./nginx.conf'
                    sh 'ls -l ./nginx.conf'
                    sh 'docker rm -f steach-server-nginx || true' // 엔진엑스 파일 삭제 8/05 5시 50분
                    sh 'docker-compose -f docker-compose.prod.yml down || true' // 8월 5일 5시에 클루트 쓰며 추가
                    sh 'docker-compose -f docker-compose.prod.yml up -d --build' // Docker Compose 파일을 사용하여 컨테이너 실행
                    sh 'docker logs steach-server-nginx'
                }
            }
        }
    }

    post {
        always {
            sh 'docker-compose -f docker-compose.prod.yml logs' // Docker Compose 로그 출력
            sh 'docker logs steach-server-nginx'
            sh 'docker logs steach-server'
            cleanWs() // 작업 공간 정리
        }
    }
}