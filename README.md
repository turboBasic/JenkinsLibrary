# JenkinsLibrary

Boilerplate project for development of Jenkins shared library and pipelines.

JenkinsLibrary aims to help developers to easily start new project with Jenkins Shared library and pipelines.
It addresses poorly documented nuances of setting up Gradle project and build environment required for
running Unit and Integration tests, adding correct versions of Jenkins plugins to dependencies etc.

Important feature of JenkinsLibrary is that it keeps both Shared library and pipelines which use it inside the same
project which makes development and testing significantly simpler comparing with the setup where library and pipelines
are kept in separate projects. Still, if you prefer to use JenkinsLibrary as a separate project for library classes
only and reuse it in multiple projects with pipeline-only code, it allows you to do so without any modifications.

JenkinsLibrary uses [JenkinsPipelineUnit](https://github.com/jenkinsci/JenkinsPipelineUnit) library for
mocking pipeline steps and variables, but on top of it adds [Spock](https://github.com/spockframework/spock) v2
framework which allows to use more precise and self-explanatory syntax of describing test cases.


## Table of Contents

1. [Build](#build)
1. [Development environment](#development-environment)
1. [Setting up in JetBrains IDEA](#setting-up-in-jetbrains-idea)
1. [Directory structure](#directory-structure)
1. [Contributing](#contributing)


## Build

The following command creates Docker image with development environment (named `turbobasic/jenkins-library:latest`),
runs a container instance and executes Gradle build and tests inside a container:

```shell
./runDevEnvironment.sh
```

Use `--no-run` option if you only need to build Docker image:

```shell
./runDevEnvironment.sh --no-run
```


## Development environment

In case you want to work inside a container's shell longer (instead of executing every build in a
container and exit), provide `/bin/sh` argument when starting the container. If docker image is already
built, execute the following command:

```shell
docker run --rm -it -v "$PWD":/app -v $HOME/.gradle:/home/appuser/.gradle turbobasic/jenkins-library /bin/sh
```

or use a shortcut of the above command:

```shell
./runDevEnvironment.sh --no-build sh
```

The development environment itself is very compact so in most situation you are fine to use your native
environment. All you need is to have Java 11 and Gradle 8 available in the PATH. I prefer to use
[SDKMan](https://sdkman.io) for switching versions of tools from JVM stack.


## Setting up in JetBrains IDEA

TBA


## Directory structure

Project follows the directory structure of
[Jenkins Shared Library](https://www.jenkins.io/doc/book/pipeline/shared-libraries/#directory-structure)
project. On top of that, `test` and other directories have been added for keeping files related to
additional features. Most directories have detailed Readme files with the description and other
important information about their contents and naming conventions:

- `pipelines`: [Jenkins pipelines](pipelines/README.md)
- `resources`: [Production resources directory](resources/README.md)
- `test/pipelines`: [Tests for jenkins pipelines](test/pipelines/README.md). Structure of this directory follows
  the structure of `pipelines` directory
- `test/resources`: [Test resources directory](test/resources/README.md)
- `test/resources/callStacks`: [Call stack dumps directory](test/resources/callStacks/README.md). This directory
  contains expected call stack dumps for the pipeline tests


## Contributing

See [How to contribute](CONTRIBUTING.md) document
