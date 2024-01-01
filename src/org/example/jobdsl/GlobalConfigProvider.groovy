package org.example.jobdsl


interface GlobalConfigProvider {
    String getPipelineDir()
    String getPipelineRepoUrl()
}
