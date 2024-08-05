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
                    sh "./gradlew clean build" // Gradle 빌드 수행
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


// 원래 코드
//         stage('Install Docker Compose') {
//             steps {
//                 script {
//                     // Install Docker Compose
//                     sh '''
//                     docker-compose --version
//                     curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
//                     chmod +x /usr/local/bin/docker-compose
//                     docker-compose --version
//                     '''
//                 }
//             }
//         }

//         stage('Verify Docker Compose Installation') { // changed
//             // 'Verify Docker Compose Installation' 단계 정의
//             // (단계의 이름을 설정하여 Docker Compose 설치 여부를 확인하는 단계임을 알립니다)
//
//             steps {
//                 script {
//                 // 셸 스크립트 블록을 정의 시작
//                 // 'docker-compose' 명령어가 시스템에 있는지 확인
//                 // 'command -v docker-compose' 명령어가 성공적으로 실행되지 않으면 (즉, docker-compose가 설치되어 있지 않으면)
//                 // 조건이 참일 때 실행할 블록 시작
//                     // 'docker-compose를 찾을 수 없으므로 설치 중...' 메시지를 출력
//                     // curl 명령어를 사용하여 지정된 URL에서 docker-compose를 다운로드하여 '/usr/local/bin/docker-compose' 위치에 저장
//                     // '-L' 옵션은 리다이렉트를 따라가도록 하고, '$(uname -s)'와 '$(uname -m)'은 현재 시스템의 운영 체제와 아키텍처 정보를 삽입
//                     // 'docker-compose' 바이너리 파일에 실행 권한을 부여
// //                     sh """
// //                         if ! command -v docker > /dev/null; then
// //                         curl -fsSL https://get.docker.com -o get-docker.sh
// //                         sh get-docker.sh
// //                         fi
// //                     """
//                     echo ''
//                     sh '''
//                     if ! command -v docker-compose &> /dev/null
//                     then
//                         echo "docker-compose could not be found, installing..."
//                         curl -L "https://github.com/docker/compose/releases/download/latest/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
//                         chmod +x /usr/local/bin/docker-compose
//                     fi
//                     '''
//                     // 조건 블록 종료
//                     // 셸 스크립트 블록 종료
//                 }
//             }
//         }

// 지피티가 알려준거임.
//         stage('Prepare Nginx Config') {
//             steps {
//                 script {
//                     // Nginx 설정 파일이 있는지 확인하고, 없으면 생성
//                     sh '''
//                     if [ ! -f ./nginx.conf ]; then
//                         echo "Creating nginx.conf"
//                         echo 'server { listen 8008; location / { proxy_pass http://app:18080; } }' > ./nginx.conf
//                     fi
//                     '''
//                 }
//             }
//         }


        stage('Deploy') { // Docker Compose를 사용하여 배포하는 단계
            steps {
                script {
                    // 필요한 경우, Docker Compose 파일 경로를 명확히 지정
                    sh 'cat nginx.conf'  // nginx.conf 파일의 내용을 출력
                    sh 'ls -l nginx.conf'  // This will list the file if it exists
                    // Error: No such container: steach-server-nginx
                    sh 'docker rm -f steach-server-nginx || true' // 엔진엑스 파일 삭제 8/05 5시 50분
                    echo 'docker rm -f steach-server-nginx'
                    sh 'docker-compose -f docker-compose.prod.yml down || true' // 8월 5일 5시에 클루트 쓰며 추가
                    echo 'docker-compose -f docker-compose.prod.yml down || true'
                    sh 'docker-compose -f docker-compose.prod.yml up -d --build' // Docker Compose 파일을 사용하여 컨테이너 실행
                    echo 'docker-compose -f docker-compose.prod.yml up -d --build'
//                     sh 'docker-compose -f docker-compose.prod.yml up -d' // Docker Compose 파일을 사용하여 컨테이너 실행
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
