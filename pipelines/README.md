# Pipelines directory description

Directory `pipelines` contains *Jenkinsfile*-s and [JobDSL scripts](https://github.com/jenkinsci/job-dsl-plugin)
which deploy pipelines to Jenkins server.

Subdirectories of `pipelines` directory are mapped the *Folders* and *Pipelines* on Jenkins server
using the following conventions:

- Each directory with kebab-case name is mapped to a Folder on Jenkins server
- Each directory with PascalCase name is mapped to a Pipeline. Pipeline code is taken from `Jenkinsfile`
file in this directory, the deployment script executed by a JobDSL plugin is taken from sibling
`Jenkinsfile.dsl` file
- **Exception**: pipeline `jenkins-library/OnCommit` and its `Jenkinsfile.dsl` uses Jenkinsfile from the
  root directory of project
