package org.example.jobdsl

import javaposse.jobdsl.dsl.Item


final class WorkflowPipeline extends BasePipeline {

    private WorkflowPipeline(Script script, String pipelineFullName, Closure body) {
        super(script, pipelineFullName, body)
    }

    static void create(Script script, String pipelineFullName=null, Closure body) {
        new WorkflowPipeline(script, pipelineFullName, body)
            .run()
    }

    @Override
    protected Item createJobDslItem(String fullName) {
        Item jobDslItem = script.pipelineJob(fullName)
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = jobDslItem

        jobDslItem.with {
            description "Workflow Pipeline $fullName"

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
                    scriptPath "${CONFIG.pipelineDir}/${fullName}/Jenkinsfile"
                }
            }
        }
    }

}
