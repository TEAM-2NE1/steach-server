# 1단계: 애플리케이션 빌드
# Gradle과 OpenJDK 17을 사용하는 빌드 이미지
FROM gradle:8.8-jdk17 AS build
# 작업 디렉토리 설정
WORKDIR /app
# 현재 디렉토리의 모든 파일을 컨테이너의 작업 디렉토리로 복사
COPY . .


# 2단계: Docker 이미지 생성
# 경량의 OpenJDK 17 런타임 이미지
FROM openjdk:17-jdk
# 작업 디렉토리 설정
WORKDIR /app
# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=build /app/build/libs/*.jar app.jar


########### 스택 오버플로우의 방법
#RUN aptitude -y install docker-compose
#RUN ln -s /usr/local/bin/docker-compose /compose/docker-compose
#RUN curl -L "https://github.com/docker/compose/releases/download/latest/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
#RUN chmod +x /usr/local/bin/docker-compose

## 두번째 방법
#FROM ubuntu:21.04
#RUN apt-get update
#RUN apt-get upgrade -y
#RUN apt-get install -y python3
#RUN apt-get install -y pip
#RUN apt-get install -y curl
#RUN curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
#RUN chmod +x /usr/local/bin/docker-compose



# 컨테이너 외부에 노출할 포트 설정
EXPOSE 18080
# 컨테이너 시작 시 실행할 명령어 설정
ENTRYPOINT ["java", "-jar", "app.jar"]


#RUN apt-get install docker-ce docker-ce-cli containerd.io docker-compose docker-compose-plugin -y
