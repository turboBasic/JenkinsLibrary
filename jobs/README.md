# Directory description

Directory `jobs` contains scripts with Jenkins pipelines and [JobDSL scripts](https://github.com/jenkinsci/job-dsl-plugin)
which deploy pipelines to Jenkins.

Subdirectories of `jobs` directory are mapped the *Folders* and *Pipelines* on Jenkins server
using the following conventions:

- Each directory with PascalCase name is mapped to a Folder on Jenkins server
- Each directory with kebab-case name is mapped to a Pipeline. Pipeline code is taken from `Jenkinsfile`
file in this project directory, the deployment script executed by a JobDSL plugin is taken from
`Jenkinsfile.dsl` file
