pipeline {
    agent any // 어떤 노드에서나 실행 가능

    environment {
        IMAGE_NAME = 'steach-server' // Docker 이미지 이름 설정
//         GRADLE_OPTS = "-Xms512m -Xmx1024m -XX:MaxMetaspaceSize=512m"
    }

    triggers {
        githubPush() // GitHub 푸시 이벤트 트리거
    }
    stages {
        stage('Checkout') { // 코드 체크아웃 단계
            steps {
                script {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: 'jen']],
                        userRemoteConfigs: [[
                            url: 'https://github.com/TEAM-2NE1/steach-server.git',
                            credentialsId: 'steach-server-jen'
                        ]]
                    ])
                }
                // ====================Pipeline Syntax로 얻은 script코드 ==================
                script {
                    checkout scmGit(
                        branches: [[name: '*/jen'], [name: '*/main'], [name: '*/develop']],
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
//                     sh "./gradlew clean build" // Gradle 빌드 수행
                    sh './gradlew --no-daemon clean build'
                    echo 'build Image'
                    docker.build("${IMAGE_NAME}:latest") // Docker 이미지를 빌드하고 latest 태그 추가
                }
            }
        }

//         stage('Deploy with Docker Compose') {
//             steps {
//                 // Docker Compose를 사용하여 컨테이너 실행
//                 sh 'docker run -itd -v /var/run/docker.sock:/var/run/docker.sock -v /root/test/:/var/tmp/ docker/compose:1.24.1 -f /var/tmp/docker-compose.yaml up -d'
//             }
//         }
// arning: Failed to open the file /usr/local/bin/docker-compose: Permission
// Warning: denied
//
// 100     9  100     9    0     0     30      0 --:--:-- --:--:-- --:--:--    30
// curl: (23) Failure writing output to destination


        stage('Deploy') { // Docker Compose를 사용하여 배포하는 단계
            steps {
                script {
                    sh 'docker-compose --version'
                    // 필요한 경우, Docker Compose 파일 경로를 명확히 지정
                    sh 'pwd'  // 현재 작업 디렉토리 출력 /var/jenkins_home/workspace/steach-server-webhook
                    sh 'ls -la'  // 현재 디렉토리의 파일 목록 출력
                    sh 'cat nginx.conf'  // nginx.conf 파일의 내용을 출력
                    sh 'ls -l nginx.conf'  // This will list the file if it exists
                    sh 'ls -l /var/jenkins_home/workspace/steach-server-webhook/nginx.conf'
                    // Error: No such container: steach-server-nginx
                    sh 'docker rm -f steach-server-nginx || true' // 엔진엑스 파일 삭제 8/05 5시 50분
                    sh 'docker-compose -f docker-compose.prod.yml down || true' // 8월 5일 5시에 클루트 쓰며 추가
                    sh 'docker-compose -f docker-compose.prod.yml up -d --build' // Docker Compose 파일을 사용하여 컨테이너 실행
                    sh 'docker logs steach-server-nginx'
                    // docker exec -it steach-server-nginx cat /etc/nginx/nginx.conf
                    // nginx 내부를 보는 코드
                }
            }
        }
    }

    post {
        always {
            sh 'docker-compose -f docker-compose.prod.yml logs' // Docker Compose 로그 출력
            cleanWs() // 작업 공간 정리
        }
    }
}
