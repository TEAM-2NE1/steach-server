# 1단계: 애플리케이션 빌드
# Gradle과 OpenJDK 17을 사용하는 빌드 이미지
FROM gradle:7.3.3-jdk17 AS build
# 작업 디렉토리 설정
WORKDIR /app
# 현재 디렉토리의 모든 파일을 컨테이너의 작업 디렉토리로 복사
COPY . .
# Gradle을 사용하여 애플리케이션 빌드 (데몬 모드 비활성화)
# copyYML 작업을 실행하고 Gradle을 사용하여 애플리케이션을 빌드합니다(데몬 모드 비활성화).
RUN gradle copyYML build --no-daemon

# 2단계: Docker 이미지 생성
# 경량의 OpenJDK 17 런타임 이미지
FROM openjdk:17-jdk
# 작업 디렉토리 설정
WORKDIR /app
# 빌드 단계에서 생성된 JAR 파일을 복사
COPY --from=build /app/build/libs/*.jar app.jar
# 애플리케이션 설정 파일 복사 위의 copyYml에서 하는거 같음.
#COPY src/main/resources/application.yml application.yml
# 컨테이너 외부에 노출할 포트 설정
EXPOSE 18080
# 컨테이너 시작 시 실행할 명령어 설정
ENTRYPOINT ["java", "-jar", "app.jar"]
