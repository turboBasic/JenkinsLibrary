package scripts.Admin

import testSupport.PipelineSpockTestBase


class seedSpec extends PipelineSpockTestBase {

    def 'Jenkinsfile works as expected'() {
        given:
            helper.registerAllowedMethod("findFiles", [Map], { Map args ->
                [
                    [path: 'jobs/Jenkinsfile.dsl'],
                    [path: 'jobs/Foo/foo-1/Jenkinsfile.dsl'],
                    [path: 'jobs/Foo/foo-2/Jenkinsfile.dsl'],
                    [path: 'jobs/Bar/bar/Jenkinsfile.dsl'],
                    [path: 'jobs/foo-bar/Jenkinsfile.dsl'],
                ]
            })
            helper.registerAllowedMethod("jobDsl", [Map])
        when:
            runScript 'Admin/seed/Jenkinsfile'
        then:
            printCallStack()
            assertJobStatusSuccess()
    }

}
