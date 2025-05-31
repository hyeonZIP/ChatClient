FROM openjdk:17-jre-slim

LABEL authors="aminez"
LABEL description="SpringBoot LLM Application"

WORKDIR /app

# GitHub Actions에서 빌드된 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
