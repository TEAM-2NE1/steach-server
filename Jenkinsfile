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
                        ]],
                        doGenerateSubmoduleConfigurations: false, // 기본 서브모듈 구성을 비활성화하고 커스텀 설정을 적용하기 위해서
                        submoduleCfg: [],
                        // SubmoduleOption 클래스를 사용하여 서브모듈 옵션을 설정합니다.
                        // 서브모듈을 재귀적으로 업데이트합니다. 즉, 서브모듈 내의 서브모듈도 함께 초기화 및 업데이트합니다.
                        // 트래킹 서브모듈을 비활성화합니다. 이는 서브모듈이 특정 브랜치를 트래킹하지 않도록 설정합니다.
                        extensions: [[$class: 'SubmoduleOption', recursiveSubmodules: true, trackingSubmodules: false]]
                    ])
                }
            }
        }

//         stage('Update Submodules') { // 서브모듈 업데이트 단계 추가
//             steps {
//                 script {
//                 // 그러나, 만약 서브모듈이 여러 단계에 걸쳐서 업데이트되어야 하는 경우가 있다면, 아래와 같이 명령어를 반복하여 호출할 수 있습니다.
//                 // 두번 하는거 같은데 잘 모르겠음.
//                     sh 'git submodule update --init --recursive' // 서브모듈 초기화 및 업데이트
//                     sh 'git submodule update --remote' // 원격 저장소에서 최신 서브모듈 업데이트
//                 }
//             }
//         }

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
//                     sh 'chmod +x ./gradlew' // gradlew 파일에 실행 권한 추가
//                     sh './gradlew build' // Gradle 빌드 수행
                    docker.build("${IMAGE_NAME}:latest") // Docker 이미지를 빌드하고 latest 태그 추가
                }
            }
        }

        stage('Verify Docker Compose Installation') { // changed
            // 'Verify Docker Compose Installation' 단계 정의
            // (단계의 이름을 설정하여 Docker Compose 설치 여부를 확인하는 단계임을 알립니다)

            steps {
                script {
                // 셸 스크립트 블록을 정의 시작
                // 'docker-compose' 명령어가 시스템에 있는지 확인
                // 'command -v docker-compose' 명령어가 성공적으로 실행되지 않으면 (즉, docker-compose가 설치되어 있지 않으면)
                // 조건이 참일 때 실행할 블록 시작
                    // 'docker-compose를 찾을 수 없으므로 설치 중...' 메시지를 출력
                    // curl 명령어를 사용하여 지정된 URL에서 docker-compose를 다운로드하여 '/usr/local/bin/docker-compose' 위치에 저장
                    // '-L' 옵션은 리다이렉트를 따라가도록 하고, '$(uname -s)'와 '$(uname -m)'은 현재 시스템의 운영 체제와 아키텍처 정보를 삽입
                    // 'docker-compose' 바이너리 파일에 실행 권한을 부여
                    sh '''
                    if ! command -v docker-compose &> /dev/null
                    then
                        echo "docker-compose could not be found, installing..."
                        curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
                        chmod +x /usr/local/bin/docker-compose
                    fi
                    '''
                    // 조건 블록 종료
                    // 셸 스크립트 블록 종료
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
            sh 'docker-compose -f docker-compose.prod.yml logs' // Docker Compose 로그 출력
            cleanWs() // 작업 공간 정리
        }
    }
}
