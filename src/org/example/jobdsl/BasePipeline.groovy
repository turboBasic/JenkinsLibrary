package org.example.jobdsl

import java.nio.file.Path
import java.nio.file.Paths
import javaposse.jobdsl.dsl.Item


abstract class BasePipeline {

    protected static final GlobalConfigProvider CONFIG = JenkinsLibraryConfig.instance
    protected static final String PIPELINE_BRANCH = 'main'

    final Closure body
    final Script script
    private String pipelineFullName

    protected BasePipeline(Script script, String pipelineFullName, Closure body) {
        this.script = script
        this.pipelineFullName = pipelineFullName
        this.body = body
    }

    protected abstract Item createJobDslItem(String fullName)

    protected final Item run() {
        createParentFolders()
            println "INFO: __FILE__ = ${script.__FILE__}"
            println "INFO: repo root = ${getRepoRootDir()}"
            println "INFO: Generating pipeline name ${getPipelineFullName()}"
        createJobDslItem(getPipelineFullName())
    }

    private final String getPipelineFullName() {
        if (pipelineFullName) {
            return pipelineFullName
        }
        Path dslScriptDir = Paths.get(script.__FILE__).parent
        Path pipelineBaseDir = Paths.get(getRepoRootDir(), CONFIG.pipelineDir)
        pipelineBaseDir.relativize(dslScriptDir)
    }

    private final String getRepoRootDir() {
        if (!script.REPO_ROOT_DIR) {
            throw new MissingPropertyException("Environment REPO_ROOT_DIR is missing")
        }
        script.REPO_ROOT_DIR
    }

    private final List<String> createParentFolders() {
        List<Path> folders = []
        List<String> parentFolders = Paths.get(getPipelineFullName())
            .init()
            .collect { Path nextDir ->
                (folders << nextDir).join('/')
            }
        parentFolders.each { String nextFolder ->
            script.folder(nextFolder)
        }
    }

}
