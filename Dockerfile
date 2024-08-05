# 1단계: 애플리케이션 빌드
# Gradle과 OpenJDK 17을 사용하는 빌드 이미지
FROM gradle:8.8-jdk17 AS build
# 작업 디렉토리 설정
WORKDIR /app
# 현재 디렉토리의 모든 파일을 컨테이너의 작업 디렉토리로 복사
COPY . .
# Gradle을 사용하여 애플리케이션 빌드 (데몬 모드 비활성화)
# copyYML 작업을 실행하고 Gradle을 사용하여 애플리케이션을 빌드합니다(데몬 모드 비활성화).
# 추후에 삭제해야하나?
# 작업이 실행되었으나 소스 파일이 없어서 NO-SOURCE 상태로 종료됨.
#RUN #radle copyYML build --no-daemon


# 2단계: Docker 이미지 생성
# 경량의 OpenJDK 17 런타임 이미지
FROM openjdk:17-jdk
# 작업 디렉토리 설정
WORKDIR /app
# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=build /app/build/libs/*.jar app.jar
# 애플리케이션 설정 파일 복사 위의 copyYml에서 하는거 같음.
# COPY src/main/resources/application.yml application.yml

# Nginx 설정 파일이 올바르게 마운트되었는지 확인하기 위한 로그 출력
RUN echo "Checking if /etc/nginx/nginx.conf exists..."
RUN ls -l /etc/nginx/nginx.conf || echo "/etc/nginx/nginx.conf does not exist"


## 스택 오버플로우의 방법
#RUN aptitude -y install docker-compose
#RUN ln -s /usr/local/bin/docker-compose /compose/docker-compose
#RUN curl -L "https://github.com/docker/compose/releases/download/latest/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
#RUN chmod +x /usr/local/bin/docker-compose
#
## 두번째 방법
#FROM ubuntu:21.04
#RUN apt-get update
#RUN apt-get upgrade -y
#RUN apt-get install -y python3
#RUN apt-get install -y pip
#RUN apt-get install -y curl
#RUN curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
#RUN chmod +x /usr/local/bin/docker-compose

# Docker 및 Docker Compose 설치
#USER root
#RUN apt-get update && \
#    apt-get install -y apt-transport-https ca-certificates curl gnupg lsb-release && \
#    curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg && \
#    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/debian $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null && \
#    apt-get update && \
#    apt-get install -y docker-ce docker-ce-cli containerd.io && \
#    curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
#    chmod +x /usr/local/bin/docker-compose
#
## Jenkins 사용자 생성 및 Docker 그룹에 추가
#RUN useradd -m jenkins && \
#    usermod -aG docker jenkins
#
#USER jenkins


# 컨테이너 외부에 노출할 포트 설정
EXPOSE 18080
# 컨테이너 시작 시 실행할 명령어 설정
ENTRYPOINT ["java", "-jar", "app.jar"]


#RUN apt-get install docker-ce docker-ce-cli containerd.io docker-compose docker-compose-plugin -y
