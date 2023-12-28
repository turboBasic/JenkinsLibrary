# syntax=docker/dockerfile:1

# hadolint global ignore=DL3006
ARG IMAGE_VERSION=python:3.12.1-alpine3.19
FROM ${IMAGE_VERSION}

ARG GRADLE_VERSION=8.5
RUN apk add --no-cache \
        gradle=~$GRADLE_VERSION \
        openjdk11-jdk \
        shadow

# Add user with the same UID/GID as the user who invokes the build
ARG USER_NAME=appuser
ARG UID=1000
ARG GID=1000
RUN groupadd --non-unique --gid $GID $USER_NAME && \
    useradd --non-unique --gid $GID --uid $UID --create-home --shell /bin/sh $USER_NAME
USER $USER_NAME

WORKDIR /app

CMD ["./gradlew", "--info", "--no-daemon", "clean", "test"]

# Metadata
ARG BUILD_DATE
ARG IMAGE_NAME
ARG IMAGE_PATCH_VER=0
ARG VCS_REF=none
LABEL \
    org.label-schema.build-date="$BUILD_DATE" \
    org.label-schema.docker.cmd="docker run --rm -it -v \"\$PWD\":/app -v \$HOME/.gradle:/home/$USER_NAME/.gradle turboBasic/jenkins-library" \
    org.label-schema.description="Build image, Gradle $GRADLE_VERSION" \
    org.label-schema.name="$IMAGE_NAME" \
    org.label-schema.schema-version="1.0" \
    org.label-schema.url="https://github.com/turboBasic/JenkinsLibrary/blob/main/README.md" \
    org.label-schema.vcs-ref="$VCS_REF" \
    org.label-schema.vcs-url="https://github.com/turboBasic/JenkinsLibrary" \
    org.label-schema.vendor="turboBasic" \
    org.label-schema.version="$IMAGE_PATCH_VER"
