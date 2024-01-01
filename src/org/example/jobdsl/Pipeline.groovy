package org.example.jobdsl

import java.nio.file.Path
import java.nio.file.Paths
import javaposse.jobdsl.dsl.jobs.WorkflowJob


class Pipeline {

    protected static final GlobalConfigProvider CONFIG = JenkinsLibraryConfig.instance
    protected static final String PIPELINE_BRANCH = 'main'

    protected Pipeline() {}

    static void create(Script context, String pipelineFullName, Closure body) {
        WorkflowJob pipeline = context.pipelineJob(pipelineFullName)
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = pipeline

        createParentFolders context, pipelineFullName
        pipeline.with {
            logRotator {
                daysToKeep 3
                numToKeep 20
            }

            body()

            definition {
                cpsScm {
                    scm {
                        git {
                            remote {
                                url CONFIG.pipelineRepoUrl
                            }
                            branch PIPELINE_BRANCH
                        }
                    }
                    scriptPath "${CONFIG.pipelineDir}/${pipelineFullName}/Jenkinsfile"
                }
            }
        }
    }

    protected static final List<String> createParentFolders(Script context, String jobFullName) {
        List<Path> folders = []
        List<String> parentFolders = Paths.get(jobFullName)
            .init()
            .collect { Path nextDir ->
                (folders << nextDir).join('/')
            }
        parentFolders.each { String nextFolder ->
            context.folder(nextFolder)
        }
    }

}
