# MULTI-STAGE BUILD

# === 1st phase: build image ===
FROM gradle:6.2.2-jdk11 AS build
LABEL maintainer="confer domclick"

# copy the project files
COPY --chown=gradle:gradle ./ /home/gradle/src
WORKDIR /home/gradle/src
# build artifact
RUN gradle clean build --no-daemon


#  === 2nd phase: target image ===
FROM azul/zulu-openjdk-alpine:11
LABEL maintainer="ru.shishmakov"

EXPOSE 8080

# set project directory
WORKDIR /home/bootapp
# copy built artifact from the 1st image
COPY --from=build /home/gradle/src/build/libs/money-transfer.jar money-transfer.jar
# run app
ENTRYPOINT exec java $JAVA_OPTS -jar money-transfer.jar

# docker image build -f ./docker/Dockerfile -t money-transfer ./ --no-cache
# docker run -p 8080:8080 --name money-transfer-app money-transfer
