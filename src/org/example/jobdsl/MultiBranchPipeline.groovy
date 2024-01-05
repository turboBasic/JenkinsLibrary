package org.example.jobdsl

import javaposse.jobdsl.dsl.Item


final class MultiBranchPipeline extends BasePipeline {

    private MultiBranchPipeline(Script script, String pipelineFullName, Closure body) {
        super(script, pipelineFullName, body)
    }

    static void create(Script script, String pipelineFullName=null, Closure body) {
        new MultiBranchPipeline(script, pipelineFullName, body)
            .run()
    }

    @Override
    protected Item createJobDslItem(String fullName) {
        Item jobDslItem = script.multibranchPipelineJob(fullName)
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = jobDslItem

        jobDslItem.with {
            description "Multibranch Pipeline $fullName"

            orphanedItemStrategy {
                discardOldItems {
                    daysToKeep 3
                    numToKeep 20
                }
            }

            body()
        }
    }
}
