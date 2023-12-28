#!/bin/sh

main() {
    cd "$(dirname "$0")" || exit
    setDefaults
    IMAGE_NAME=turbobasic/jenkins-library
    USER_NAME=appuser

    END_OF_OPTIONS=
    while [ -z "$END_OF_OPTIONS" ] && [ -n "$1" ]; do
        case "$1" in
            --no-build)
                NO_BUILD=true
                shift;;
            --no-run)
                NO_RUN=true
                shift;;
            *)
                END_OF_OPTIONS=true
        esac
    done

    if [ -z "$NO_BUILD" ]; then
        docker build --rm --tag "$IMAGE_NAME":latest \
            --build-arg UID="$(id -u)" --build-arg GID="$(id -g)" \
            --build-arg VCS_REF="$VCS_REF" \
            --build-arg USER_NAME="$USER_NAME" \
            --build-arg IMAGE_NAME="$IMAGE_NAME" \
            --build-arg BUILD_DATE="$(date -u "+%Y-%m-%d %H:%M")" \
            .
        docker image prune --force --filter "label=org.label-schema.name=$IMAGE_NAME"
    fi

    if [ -z "$NO_RUN" ]; then
        mkdir --parents "$GRADLE_HOME_DIR"
        # shellcheck disable=SC2086
        docker run --rm $DOCKER_OPTIONS \
            --volume "$PWD:/app" \
            --volume "$GRADLE_HOME_DIR:/home/$USER_NAME/.gradle" \
            "$IMAGE_NAME" "$@"
        if [ "$CI" != true ] && [ $# = 0 ]; then
            echo "Tests execution report: file://$PWD/build/reports/tests/test/index.html"
        fi
    fi
}

setDefaults() {
    NO_BUILD=
    NO_RUN=
    DOCKER_OPTIONS=
    GRADLE_HOME_DIR="$PWD/.gradle"

    VCS_REF="$(git rev-parse --abbrev-ref HEAD 2>/dev/null)/$(git rev-parse --short HEAD 2>/dev/null)"
    if [ "$VCS_REF" = "/" ]; then
        VCS_REF="not-in-version-control"
    fi

    if [ "$CI" != true ]; then
        DOCKER_OPTIONS="--interactive --tty"
        GRADLE_HOME_DIR="$HOME/.gradle"
        true
    fi
}


# Start body of script
set -o errexit
[ "$CI" = "true" ] && set -o xtrace

main "$@"
