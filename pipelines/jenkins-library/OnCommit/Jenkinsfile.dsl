import org.example.jobdsl.MultiBranchPipeline


MultiBranchPipeline.create(this, 'jenkins-library/OnCommit') {
    branchSources {
        git {
            id 'JenkinsLibrary'
            remote 'https://github.com/turboBasic/JenkinsLibrary'
        }
    }
    factory {
        workflowBranchProjectFactory {
            scriptPath 'Jenkinsfile'
        }
    }
    triggers {
        cron 'H 1 * * *'
        pollSCM {
            scmpoll_spec '*/5 * * * *'
            ignorePostCommitHooks false
        }
    }
}
