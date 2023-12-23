#!/bin/sh

cd "$(dirname "$0")" || exit
image_name=turbobasic/jenkins-library:latest

docker build --rm --tag $image_name \
    --build-arg UID=$(id -u) --build-arg GID=$(id -g) \
    --build-arg VCS_REF="$(git rev-parse --abbrev-ref HEAD)/$(git rev-parse --short HEAD)" \
    --build-arg BUILD_DATE="$(date -u +%Y-%m-%d/%H:%M)" \
    .

docker run --rm --interactive --tty \
    --volume "$PWD":/app \
    --volume $HOME/.gradle:/home/appuser/.gradle \
    "$image_name"

# In order to work inside a container's shell, execute:
#
#   docker run --rm -it -v "$PWD":/app -v $HOME/.gradle:/home/appuser/.gradle \
#       --entrypoint /bin/bash turbobasic/jenkins-library
