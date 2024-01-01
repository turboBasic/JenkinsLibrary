package org.example.jobdsl

import javaposse.jobdsl.dsl.jobs.MultibranchWorkflowJob


final class MultiBranchPipeline extends Pipeline {

    private MultiBranchPipeline() {}

    static void create(Script context, String pipelineFullName, Closure body) {
        MultibranchWorkflowJob pipeline = context.multibranchPipelineJob(pipelineFullName)
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = pipeline

        createParentFolders context, pipelineFullName
        pipeline.with {
            description "Multibranch Pipeline $pipelineFullName"

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
