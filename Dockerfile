FROM python:3.12.1-alpine3.19

ARG GRADLE_VER=8.5
RUN apk add --no-cache --progress \
        bash \
        git'>='2.40 \
        gradle=~$GRADLE_VER \
        openjdk11-jdk \
        shadow

# Add user with the same UID/GID as the user who invokes the build
ARG UNAME=appuser
ARG UID=1000
ARG GID=1000
RUN groupadd --gid $GID --non-unique $UNAME
RUN useradd --create-home --uid $UID --gid $GID --non-unique --shell /bin/bash $UNAME

USER $UNAME
WORKDIR /app

# Default action: build and execute tests
ENTRYPOINT ["gradle"]
CMD ["--info", "clean", "test"]

# Metadata
ARG BUILD_DATE
ARG VCS_REF
ARG IMAGE_PATCH_VER=0
LABEL \
    org.label-schema.build-date=$BUILD_DATE \
    org.label-schema.docker.cmd="docker run --rm -it -v \"\$PWD\":/app -v \$HOME/.gradle:/home/$UNAME/.gradle turboBasic/jenkins-library" \
    org.label-schema.description="Build image, Gradle $GRADLE_VER" \
    org.label-schema.name="turboBasic/jenkins-library" \
    org.label-schema.schema-version="1.0" \
    org.label-schema.url="https://github.com/turboBasic/jenkins-library/blob/main/README.md" \
    org.label-schema.vcs-ref=$VCS_REF \
    org.label-schema.vcs-url="https://github.com/turboBasic/jenkins-library" \
    org.label-schema.vendor="turboBasic" \
    org.label-schema.version=$IMAGE_PATCH_VER
