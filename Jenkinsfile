pipeline {
    agent any // 어떤 노드에서나 실행 가능

    environment {
        IMAGE_NAME = 'steach-server' // Docker 이미지 이름 설정
    }

    triggers {
        githubPush() // GitHub 푸시 이벤트 트리거
    }
    stages {
//      특정 커밋, 브랜치, 또는 태그의 소스 코드를 작업 디렉토리에 가져오는 과정을 의미합니다.
//      Git을 예로 들어 설명하면, checkout 명령어는 저장소의 특정 상태를 작업 디렉토리에 복사하여 그 상태에서 작업을 할 수 있도록 합니다.
        stage('Checkout') { // 코드 체크아웃 단계
            steps {
                script {
                 // BRANCH_NAME 변수는 Jenkins가 자동으로 설정해주는 환경 변수로, 빌드 트리거된 브랜치의 이름을 가집니다.
                 // 그러나, 이 변수가 자동으로 설정되지 않는 경우도 있으므로, 이를 명시적으로 설정해야 할 수 있습니다.
                    def branch = env.GIT_BRANCH ? env.GIT_BRANCH.replaceAll(/^origin\//, '') : 'main'
                    echo "Checking out branch: ${branch}" // 변경된 브랜치 표시
                    checkout([
                        $class: 'GitSCM', // Git 소스 코드 관리 클래스 사용
//                      [[name: "${branch}"]]: 이 부분은 branches 키워드의 값으로, 실제로 체크아웃할 브랜치를 나타냅니다. 리스트 내의 딕셔너리 형태로 작성됩니다.
                        branches: [[name: "${branch}"]], // 변경된 브랜치를 지정
                        doGenerateSubmoduleConfigurations: false, // 서브모듈 설정 자동 생성하지 않음
                        extensions: [ // 확장 설정 시작
                            [$class: 'SubmoduleOption', // 서브모듈 옵션 클래스 사용
                             disableSubmodules: false, // 서브모듈 비활성화하지 않음
                             parentCredentials: true, // 부모 자격 증명 사용
                             recursiveSubmodules: true, // 서브모듈을 재귀적으로 초기화 및 업데이트
                             reference: '', // 참조 리포지토리 없음
                             trackingSubmodules: false] // 서브모듈 추적하지 않음
                        ],
                        userRemoteConfigs: [[
                        // https시 token으로 해야하는데 뭔가가 엉청 안됬음. ssh로 시도 중
                            url: 'git@github.com:TEAM-2NE1/steach-server.git', // SSH를 사용한 Git 저장소 URL
                            credentialsId: 'steach-server-jen-ssh' // 사용할 자격증명 ID
                        ]]
                    ])
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

        stage('Verify Docker Installation') { // Docker 설치 확인 단계
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
