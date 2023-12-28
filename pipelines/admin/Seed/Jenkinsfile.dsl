pipelineJob 'admin/Seed', {
    parameters {
        choiceParam {
            name 'LOOKUP_STRATEGY'
            description 'Describes how relative path to jobs are processed: relative to the Seed job or Jenkins root'
            choices(['SEED_JOB', 'JENKINS_ROOT'])
        }
    }
    properties {
        disableConcurrentBuilds()
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url 'https://github.com/turboBasic/JenkinsLibrary'
                    }
                    branch 'main'
                }
            }
            scriptPath jenkinsFileName('admin/Seed')
        }
    }
}

String jenkinsFileName(String jobName) {
    "pipelines/$jobName/Jenkinsfile"
}
