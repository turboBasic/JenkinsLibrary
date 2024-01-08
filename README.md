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

1. [Usage](#usage)
1. [Setting up in JetBrains IDEA](#setting-up-project-in-jetbrains-idea)
1. [Development environment](#development-environment)
1. [Directory structure](#directory-structure)
1. [Contributing](#contributing)


<!-- begin of help -->

## Usage

### Build and test commands

- `./dev all-test`: build project and execute Integration and Unit tests.
- `./dev integration-test`: build project and execute Integration tests.
- `./dev test`: build project and execute Unit tests.  
  If you provide arguments after `./dev test` command, they are appended to `gradle clean test` command-line and
  resulting command is executed
- `./dev test --tests "pipelines.admin.**"`: build project and execute all tests in `pipelines.admin` package
- `./dev test --tests "pipelines.admin.**" --tests org.example.FooSpec`: build project and execute all tests in
  `pipelines.admin` package and tests for class `org.example.FooSpec`
- `./dev test -Dpipeline.stack.write=true`: build project, execute tests and update
  [call stack files](test/resources/callStacks/README.md)

### Other commands

- `./dev docker [--push]`: only build Docker image. If `--push` switch is provided, Docker image is pushed to Docker Hub
- `./dev help`: display this help about commands
- `./dev lint`: lint project with MegaLinter
- `./dev sh`: create development environment and enter shell session inside it. Inside development environment you can
  use Gradle to execute any advanced commands

<!-- end of help -->


## Setting up project in JetBrains IDEA

Execute `./gradlew openIdea` in the project. When IDEA opens the project click *Skip* if it proposes to import Gradle
project


## Development environment

The development environment invoked by `./dev sh` command is very compact so in most situation you are
fine to use your native environment for building and executing tests. All you need is to have Java 11
and Gradle 8 available in the PATH.

Recommended way of usage is the `./dev` wrapper (see [Usage](#usage))
but if you would like to use native environment you can use [SDKMan](https://sdkman.io) for installing
and switching versions of tools from JVM stack.

## Directory structure

Project follows the directory structure of
[Jenkins Shared Library](https://www.jenkins.io/doc/book/pipeline/shared-libraries/#directory-structure)
project. On top of that, `test` and other directories have been added for keeping files related to
additional features. Most directories have detailed Readme files with the description and other
important information about their contents and naming conventions:

- `pipelines`: [Jenkins pipelines](pipelines/README.md)
- `resources`: [Production resources directory](resources/README.md)
- `src`: Production source code for Jenkins Shared Library classes. Subdirectories here
  follow conventional Groovy/Java package layout
- `test`: Unit-tests' source code. Subdirectories here follow conventional Groovy/Java
  package layout
- `test/pipelines`: [Tests for jenkins pipelines](test/pipelines/README.md). Structure of this directory follows
  the structure of `pipelines` directory
- `test/resources`: [Test resources directory](test/resources/README.md)
- `test/resources/callStacks`: [Call stack dumps directory](test/resources/callStacks/README.md). This directory
  contains expected call stack dumps for the pipeline tests
- `testIntegration`: Integration tests' source code
- `testIntegration/resources`: [Resources directory](testIntegration/resources/README.md) for Integration tests
- `vars`: Custom steps aka _Global vars_ in Jenkins Shared library


## Contributing

See [How to contribute](CONTRIBUTING.md) document
