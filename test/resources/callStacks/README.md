# Pipeline call stacks directory description

Directory `test/resources/callStacks` contains expected call stacks of Jenkins pipelines and classes.
Test cases may use them for comparing actual call stacks with expected results.

Call stack files use the following naming conventions:

`<ShortClassName>_<Test_case_name_with_spaces_replaced_with_underscores>.txt`

eg. if you have test suite class `org.example.JenkinsfileSpec` and call stack file named
`JenkinsfileSpec_Jenkinsfile_works_as_expected.txt`, then you can assert that actual call
stack is the same as expected with the following statement in your test case:

```groovy
testNonRegression 'Jenkinsfile_works_as_expected'
```

`testNonRegression` takes the name of current test suite class (`JenkinsfileSpec`), then appends
argument and file extension `.txt`, finds expected call stack dump file and compares actual
call stack with the contents of the file.
