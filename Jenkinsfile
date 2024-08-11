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
                    env.BRANCH_NAME = branch
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
                    def branch = env.GIT_BRANCH ? env.GIT_BRANCH.replaceAll(/^origin\//, '') : 'main'
                    checkout scmGit(
                        // BRANCH_NAME 변수는 Jenkins가 자동으로 설정해주는 환경 변수로, 빌드 트리거된 브랜치의 이름을 가집니다.
                        // 그러나, 이 변수가 자동으로 설정되지 않는 경우도 있으므로, 이를 명시적으로 설정해야 할 수 있습니다.
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

        stage('SonarQube Analysis') {
            steps {
                script {
                // 수정
                    def branchName = env.BRANCH_NAME
                    sh "./gradlew --info --warning-mode all sonar -Dsonar.projectKey=steach-server-${branchName} -Dsonar.projectName=steach-server-${branchName}"
                }
            }
        }

        stage('Deploy Server') {
          steps {
                script {
                    sh 'docker-compose --version'
                    sh 'docker-compose -f docker-compose.prod.yml down || true'
                    sh 'docker-compose -f docker-compose.prod.yml up -d --build'

                    // steach-server 컨테이너 ID 가져오기
                    def containerId = sh(script: "docker ps -qf 'name=steach-server'", returnStdout: true).trim()

                    // 컨테이너 로그 경로 설정
                    env.SERVER_LOG_PATH = "/var/lib/docker/containers/${containerId}"
                    echo 'log path : ${env.SERVER_LOG_PATH}'
                }
            }
        }

        stage('Deploy Alloy') {
            steps {
                script {
                    // Alloy 컨테이너가 이미 존재하면 삭제 (없으면 무시)
                    sh """
                        if [ \$(docker ps -aq -f name=alloy) ]; then
                            docker rm -f alloy
                        fi
                    """

                    // 동적으로 Docker Compose 파일 생성
                    writeFile file: 'docker-compose.alloy.yml', text: """
                    services:
                      alloy:
                        image: grafana/alloy:latest
                        container_name: alloy
                        ports:
                          - 12345:12345
                        volumes:
                          - /home/ubuntu/grafana/alloy/alloy-config.alloy:/etc/alloy/config.alloy
                          - ${env.SERVER_LOG_PATH}:/tmp/app-logs
                        command: run --server.http.listen-addr=0.0.0.0:12345 --storage.path=/var/lib/alloy/data /etc/alloy/config.alloy
                        networks:
                          - steach-server-network
                    networks:
                      steach-server-network:
                        external: true
                        name: steach-server-network
                    """

                    sh 'docker-compose -f docker-compose.alloy.yml up -d --build'
                }
            }
        }
    }

    post {
        always {
            sh 'docker-compose -f docker-compose.prod.yml logs || true' // Docker Compose 로그 출력
            sh 'docker logs steach-server || true' // steach-server 컨테이너 로그 출력, 없으면 오류 무시
            cleanWs() // 작업 공간 정리
        }
    }
}
