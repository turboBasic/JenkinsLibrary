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
        then:
            printCallStack()
            assertJobStatusSuccess()
            assertCallStackContains('git clone https://example.org/foo')
    }

    def 'Foo performs clone if called from inline script and git is new'() {
        given:
            helper.addShMock('git --version', 'git version 2.40', 0)
        when:
            runInlineScript '''
                @Library('JenkinsLibrary') _

                foo()
            '''
        then:
            printCallStack()
            assertJobStatusSuccess()
    }

    def 'Foo does not clone when called from Jenkinsfile and git is old'() {
        given:
            helper.addShMock('git --version', 'git version 2.28', 0)
        when:
            runScript 'org/example/FooSpec.1.Jenkinsfile'
        then:
            printCallStack()
            assertJobStatusSuccess()
            assertCallStack().doesNotContain('git clone')
    }

    def 'Foo works as expected when instantiated from Jenkinsfile1'() {
        given:
            helper.addShMock('git --version', 'git version 2.28', 0)
        when:
            runScript 'org/example/FooSpec.1.Jenkinsfile'
        then:
            printCallStack()
            assertJobStatusSuccess()
            assertCallStack().doesNotContain('git clone')
    }

    def 'Foo works as expected when called via Global Var'() {
        given:
            helper.addShMock('git --version', 'git version 2.28', 0)
        when:
            runScript 'org/example/FooSpec.2.Jenkinsfile'
        then:
            printCallStack()
            assertJobStatusSuccess()
            assertCallStack().doesNotContain('git clone')
    }

}
