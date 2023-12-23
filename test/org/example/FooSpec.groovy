package org.example

import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library
import static com.lesfurets.jenkins.unit.global.lib.ProjectSource.projectSource
import spock.lang.Ignore
import testSupport.PipelineSpockTestBase


class FooSpec extends PipelineSpockTestBase {

    def setup() {
        def library = library()
            .name('JenkinsLibrary')
            .defaultVersion('<not required>')
            .allowOverride(true)
            .implicit(false)
            .targetPath('<not required>')
            .retriever(projectSource())
            .build()
        helper.registerSharedLibrary(library)
    }

    def 'Foo performs clone if git is new'() {
        given:
            helper.addShMock('git --version', 'git version 2.40', 0)

        when:
            new Foo(script).run()
            printCallStack()
            println()

        then:
            assertJobStatusSuccess()
            assertCallStackContains('git clone https://example.org/foo')
    }

    @Ignore('Test with inline script which loads library hangs (https://github.com/jenkinsci/JenkinsPipelineUnit/issues/472)')
    def 'Foo performs clone if called from inline script and in git is new'() {
        given:
            helper.addShMock('git --version', 'git version 2.40', 0)
        when:
            runInlineScript '''
                @Library('JenkinsLibrary') _

                foo()
            '''
            printCallStack()
            println()
        then:
            assertJobStatusSuccess()
    }

    def 'Foo does not clone when called from Jenkinsfile and git is old'() {
        given:
            helper.addShMock('git --version', 'git version 2.28', 0)

        when:
            runScript('test/org/example/FooSpec.1.Jenkinsfile')
            printCallStack()
            println()

        then:
            assertJobStatusSuccess()
            assertCallStack().doesNotContain('git clone')
    }

    def 'Foo works as expected when instantiated from Jenkinsfile1'() {
        given:
            helper.addShMock('git --version', 'git version 2.28', 0)

        when:
            runScript('test/org/example/FooSpec.1.Jenkinsfile')
            printCallStack()
            println()

        then:
            assertJobStatusSuccess()
            assertCallStack().doesNotContain('git clone')
    }

    def 'Foo works as expected when called via Global Var'() {
        given:
            helper.addShMock('git --version', 'git version 2.28', 0)

        when:
            runScript('test/org/example/FooSpec.2.Jenkinsfile')
            printCallStack()
            println()

        then:
            assertJobStatusSuccess()
            assertCallStack().doesNotContain('git clone')
    }

}
