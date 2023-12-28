# syntax=docker/dockerfile:1

# hadolint global ignore=DL3006
ARG BASE_IMAGE=python:3.12.1-alpine3.19
FROM ${BASE_IMAGE}

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
ENV GRADLE_USER_HOME=/app/.gradle

CMD ["./gradlew", "--info", "--no-daemon", "clean", "test"]

# Metadata
ARG BUILD_DATE
ARG IMAGE_NAME
ARG VCS_BRANCH=main
ARG VCS_SHA
ARG VERSION
LABEL \
    org.opencontainers.image.base.name="$BASE_IMAGE" \
    org.opencontainers.image.created="$BUILD_DATE" \
    org.opencontainers.image.authors="turboBasic <off@boun.cr>" \
    org.opencontainers.image.description="Image for testing Jenkins Shared libraries and pipelines" \
    org.opencontainers.image.documentation="https://github.com/turboBasic/JenkinsLibrary/blob/$VCS_BRANCH/README.md" \
    org.opencontainers.image.licenses="MIT" \
    org.opencontainers.image.revision="$VCS_SHA" \
    org.opencontainers.image.source="https://github.com/turboBasic/JenkinsLibrary" \
    org.opencontainers.image.title="$IMAGE_NAME" \
    org.opencontainers.image.url="https://github.com/turboBasic/JenkinsLibrary" \
    org.opencontainers.image.vendor="turboBasic" \
    org.opencontainers.image.version="$VERSION" \
    org.label-schema.docker.cmd="docker run --rm -it -v $(pwd):/app turbobasic/jenkins-library" \
    org.label-schema.usage="/app/README.md"
