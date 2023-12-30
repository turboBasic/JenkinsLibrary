# Test resources directory description

Directory `test/resources` keeps the following items related to tests of Jenkins pipelines
and classes:

- `callStacks/**` - expected call stacks of Jenkins pipelines and classes. See its
  [Readme file](callStacks/README.md) for more details
- `org/example/**` - other subdirectories follow standard structure of Java/Groovy packages.  
  For example, if your test class `org.example.FooSpec` needs Jenkinsfile to execute and
  assert that results are correct, place it in `org/example/FooSpec.Jenkinsfile` file.
