FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# copy project files
COPY . /app

# build the application (using the bundled mvnw)
RUN chmod +x mvnw && ./mvnw -DskipTests package -B

EXPOSE 8080

# default upload dir inside container; override with APP_UPLOAD_DIR env var
ENV APP_UPLOAD_DIR=/data/uploads

# create upload dir and run the jar
CMD mkdir -p ${APP_UPLOAD_DIR} && java -jar target/*.jar
