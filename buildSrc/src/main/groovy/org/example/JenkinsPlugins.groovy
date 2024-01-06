package org.example

import java.nio.file.Paths
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact


final class JenkinsPlugins {
    private static JenkinsPlugins instance
    private final Project project
    private final List<JenkinsPlugin> plugins = []


    /*  Constructors and initializers */

    private JenkinsPlugins(Project project) {
        this.project = project
    }

    static void create(Project project) {
        if (!instance) {
            instance = new JenkinsPlugins(project)
        } else {
            if (instance.project != project) {
                throw new IllegalArgumentException("org.example.JenkinsPlugins instance already exists for project ${instance.project}")
            } else {
                println "WARNING: Multiple events of creating instance of org.example.JenkinsPlugins for the same project detected"
            }
        }
    }

    static JenkinsPlugins get() {
        if (!instance) {
            throw new IllegalStateException("org.example.JenkinsPlugins is not initialized")
        }
        instance
    }


    /*  Public API */

    String getJenkinsVersion() {
        project.property('jenkinsVersion')
    }

    String getTestHarnessVersion() {
        project.property("testHarnessVersion.$jenkinsVersion")
    }

    List<String> all() {
        ArrayList.copyOf(getDeclaredJenkinsPlugins().values())
    }

    String getAt(String name) {
        getDeclaredJenkinsPlugins()[name]
    }

    void each(Closure body) {
        all().each(body)
    }

    /*
        ResolvedArtifact:
            name: workflow-job
            moduleVersion:
                id: org.jenkins-ci.plugins.workflow:workflow-job:2.41
                    group: org.jenkins-ci.plugins.workflow
                    name: workflow-job
                    version: 2.41

     */
    void registerResolvedPlugins(Set<ResolvedArtifact> resolvedArtifacts) {
        resolvedArtifacts
            .findAll { ResolvedArtifact artifact -> artifact.extension ==~ /[hj]pi/ }
            .each { ResolvedArtifact artifact ->
                JenkinsPlugin plugin = new JenkinsPlugin(artifact)
                plugins << plugin
            }
    }

    void generatePluginsIndex(String destinationFileName) {
        new File(destinationFileName).setText(
            getPluginsIndex().join('\n'),
            'UTF-8'
        )
    }

    String pluginFileNameForJenkinsInstance(String filePath) {
        plugins.find { JenkinsPlugin plugin ->
            plugin.originalFileName == Paths.get(filePath).getFileName().toString()
        }
            .fileNameForJenkinsInstance
    }


    /*  Private part */

    private Map<String, String> getJenkinsPluginVersionsFromProperties() {
        project.property("jenkinsPluginVersions.$jenkinsVersion")
            .tokenize()
            .collate(2)
            .findAll {
                def (String module, String version) = it
                !module.startsWith('#')
            }
            .collectEntries {
                def (String module, String version) = it
                [(module.replaceAll(/:$/, '')): version]
            }
    }

    private Map<String, String> getDeclaredJenkinsPlugins() {
        getJenkinsPluginVersionsFromProperties()
            .collectEntries { String module, version ->
                String pluginName = module.split(':').last()
                [(pluginName): "$module:$version"]
            }
    }

    private List<String> getPluginsIndex() {
        plugins
            .collect { JenkinsPlugin plugin -> plugin.name }
            .toSorted()
    }

    private class JenkinsPlugin {
        final ResolvedArtifact artifact

        JenkinsPlugin(ResolvedArtifact artifact) {
            this.artifact = artifact
        }

        String getName() {
            artifact.name
        }

        String getOriginalFileName() {
            artifact.file.name
        }

        String getFileNameForJenkinsInstance() {
            "${artifact.name}.${artifact.extension}"
        }

        private String getVersion() {
            artifact.moduleVersion.id.version
        }

        private String getResolvedModuleVersion() {
            artifact.moduleVersion
        }

        private String getDeclaredPluginVersion() {
            getDeclaredJenkinsPlugins()[name]
        }

        String toString() {
            "{name=$name, version=$version, declaredPluginVersion=$declaredPluginVersion, resolvedModuleVersion=$resolvedModuleVersion, originalFileName=$originalFileName, fileNameForJenkinsInstance=$fileNameForJenkinsInstance"
        }
    }

}
