package pipelines.admin

import testSupport.PipelineSpockTestBase


class SeedSpec extends PipelineSpockTestBase {

    def 'Pipeline works as expected'() {
        given:
            helper.registerAllowedMethod("findFiles", [Map], { Map args ->
                [
                    [path: 'pipelines/Jenkinsfile.dsl'],
                    [path: 'pipelines/Foo/foo-1/Jenkinsfile.dsl'],
                    [path: 'pipelines/Foo/foo-2/Jenkinsfile.dsl'],
                    [path: 'pipelines/Bar/bar/Jenkinsfile.dsl'],
                    [path: 'pipelines/foo-bar/Jenkinsfile.dsl'],
                ]
            })
            helper.registerAllowedMethod("jobDsl", [Map])
        when:
            runScript 'admin/Seed/Jenkinsfile'
        then:
            printCallStack()
            assertJobStatusSuccess()
    }

}
