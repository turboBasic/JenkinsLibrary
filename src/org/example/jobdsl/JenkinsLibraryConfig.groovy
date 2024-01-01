package org.example.jobdsl


@Singleton
final class JenkinsLibraryConfig implements GlobalConfigProvider {

    static final String PIPELINE_DIR = 'pipelines'
    static final String PIPELINE_REPO_URL = 'https://github.com/turboBasic/JenkinsLibrary'

    String getPipelineDir() {
        PIPELINE_DIR
    }

    String getPipelineRepoUrl() {
        PIPELINE_REPO_URL
    }

}
