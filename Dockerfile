# syntax=docker/dockerfile:1

ARG IMAGE_VERSION=python:3.12.1-alpine3.19
FROM ${IMAGE_VERSION}

ARG GRADLE_VERSION=8.5
RUN apk add --no-cache --progress \
        gradle=~$GRADLE_VERSION \
        openjdk11-jdk \
        shadow

# Add user with the same UID/GID as the user who invokes the build
ARG USER_NAME=appuser
ARG UID=1000
ARG GID=1000
RUN groupadd --non-unique --gid $GID $USER_NAME
RUN useradd --non-unique --gid $GID --uid $UID --create-home --shell /bin/sh $USER_NAME
USER $USER_NAME

VOLUME /home/$USER_NAME/.gradle
WORKDIR /app

CMD ["./gradlew", "--info", "--no-daemon", "clean", "test"]

# Metadata
ARG BUILD_DATE
ARG VCS_REF=none
ARG IMAGE_PATCH_VER=0
LABEL \
    org.label-schema.build-date="$BUILD_DATE" \
    org.label-schema.docker.cmd="docker run --rm -it -v \"\$PWD\":/app -v \$HOME/.gradle:/home/$USER_NAME/.gradle turboBasic/jenkins-library" \
    org.label-schema.description="Build image, Gradle $GRADLE_VERSION" \
    org.label-schema.name="turboBasic/jenkins-library" \
    org.label-schema.schema-version="1.0" \
    org.label-schema.url="https://github.com/turboBasic/jenkins-library/blob/main/README.md" \
    org.label-schema.vcs-ref="$VCS_REF" \
    org.label-schema.vcs-url="https://github.com/turboBasic/jenkins-library" \
    org.label-schema.vendor="turboBasic" \
    org.label-schema.version="$IMAGE_PATCH_VER"
