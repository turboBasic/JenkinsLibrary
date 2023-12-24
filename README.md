Jenkins Library
===============

Boilerplate project for development of Jenkins shared library and pipelines


Build
-----

The following command creates Docker image with development environment (named 
`turbobasic/jenkins-library:latest`), runs a container instance and executes Gradle build and tests 
inside a container:

```shell
./runDevEnvironment.sh
```

Use `--no-run` option if you only need to build Docker image:

```shell
./runDevEnvironment.sh --no-run
```


Development environment
-----------------------

In case you want to work inside a container's shell longer (instead of executing every build in a
container and exit), provide `/bin/sh` argument when starting the container. If docker image is already
built, execute the following command:

```shell
docker run --rm -it -v "$PWD":/app -v $HOME/.gradle:/home/appuser/.gradle turbobasic/jenkins-library sh
```

or use a shortcut of the above command:

```shell
./runDevEnvironment.sh --no-build sh
```

The development environment itself is very compact so in most situation you are fine to use your native
environment. All you need is to have Java 11 and Gradle 8 available in the PATH. I prefer to use
[SDKMan](https://sdkman.io) for switching versions of tools from JVM stack.
