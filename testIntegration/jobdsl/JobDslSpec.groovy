package jobdsl

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement
import org.apache.commons.io.FileUtils
import org.junit.ClassRule
import org.jvnet.hudson.test.JenkinsRule
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


class JobDslSpec extends Specification {

    static final String DSL_SCRIPTS_PATTERN = [
        '**/Jenkinsfile.dsl',
    ].join(',')

    static final Path projectPath = Paths.get('').toAbsolutePath()

    static final List<Path> dslFiles = new FileNameFinder()
        .getFileNames(projectPath.toString(), DSL_SCRIPTS_PATTERN)
        .collect { Paths.get(it) }

    static final String buildDir = projectPath.toString() + '/build'

    @Shared
    @ClassRule
    JenkinsRule jenkinsRule = new JenkinsRule()

    void cleanupSpec() {
        println "\n---\nJobs generated by test suite ${this.getClass().name}:"
        println generatedJobConfigs
            .collect { '- ' + Paths.get(jenkinsHomeDir).relativize(it) }
            .join('\n')

        saveConfigXmlFiles()
        saveJenkinsTestHarnessHomeDir()
    }

    void saveConfigXmlFiles() {
        generatedJobConfigs.each { Path source ->
            Path relativePath = Paths.get(jenkinsHomeDir).relativize(source)
            Path target = Paths.get("$buildDir/$relativePath")

            Files.createDirectories(target.getParent())
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    void saveJenkinsTestHarnessHomeDir() {
        File jenkinsTestHarnessCopy = Paths.get("$buildDir/jenkins-test-harness").toFile()
        FileUtils.deleteDirectory(jenkinsTestHarnessCopy)
        FileUtils.copyDirectory(Paths.get(jenkinsHomeDir).toFile(), jenkinsTestHarnessCopy)
    }

    String getJenkinsHomeDir() {
        jenkinsRule.jenkins.rootDir.absolutePath
    }

    List<Path> getGeneratedJobConfigs() {
        new FileNameFinder()
            .getFileNames(jenkinsHomeDir, '**/jobs/**/config.xml')
            .collect { Paths.get(it) }
    }

    @Unroll
    def 'JobDSL script #relativePath is valid'(File file, Path relativePath) {
        given:
            def jobManagement = new JenkinsJobManagement(
                System.out,
                [
                    __FILE__: file.path,
                    JENKINS_URL: 'http://127.0.0.1',
                    REPO_ROOT_DIR: projectPath,
                ],
                new File('.')
            )
            println "\nValidate JobDSL script $relativePath"
        when:
            new DslScriptLoader(jobManagement).runScript(file.text)
        then:
            noExceptionThrown()
        where:
            file << dslFiles.collect { it.toFile() }
            relativePath << dslFiles.collect { projectPath.relativize(it) }
    }

}
