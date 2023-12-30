# Pipelines tests directory description

Directory `test/pipelines` contains usual Java/Groovy package named `pipelines`.
The decision behind separating them from other tests in `org.example` package based on a desire
to separate tests for "real" classes from `org.example` production package from the tests for
scripts, which in fact does not belong to any package as they do not expose any classes


## Naming conventions

Test for pipeline `foo/Daily` is kept in `pipelines.foo.DailySpec` class. The reasoning is as
follows:
- this is consistent with our naming of pipelines in `pipelines` directory, i.e. for `foo/Daily`
  pipeline folder part goes to package name (`pipelines.foo`), job short name (`Daily`) goes to
  class name (`pipelines.foo.Daily`), and `Spec` or `Test` suffix is added as per convention
  in JUnit testing framework, resulting in a complete test class name `pipelines.foo.DailySpec`
- in case you have multiple pipelines in Jenkins folder `foo`, eg, `Daily` and `OnCommit`, your
  tests will be in sibling classes `DailySpec` and `OnCommitSpec` in the same package
  `pipelines.foo` which follows the logic that pipelines in the same folder should also belong
  to the same higher-level entity, namely to the package in our scenario
