import org.example.jobdsl.Pipeline


Pipeline.create(this, 'admin/Seed') {
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
}
