package pipelines.jenkins_library

import testSupport.PipelineSpockTestBase


class OnCommitSpec extends PipelineSpockTestBase {

    def 'Pipeline works as expected'() {
        given:
            helper.addShMock('./dev test', '...execute build environment...', 0)
        when:
            runScript 'Jenkinsfile'
        then:
            printCallStack()
            assertJobStatusSuccess()
        then:
            testNonRegression 'Pipeline_works_as_expected'
    }

}
