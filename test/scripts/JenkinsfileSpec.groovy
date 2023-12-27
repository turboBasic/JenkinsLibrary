package scripts

import testSupport.PipelineSpockTestBase


class JenkinsfileSpec extends PipelineSpockTestBase {

    def 'Jenkinsfile works as expected'() {
        given:
            helper.addShMock('./runDevEnvironment.sh', '...execute build environment...', 0)
        when:
            runScript 'Jenkinsfile'
        then:
            printCallStack()
            assertJobStatusSuccess()
        then:
            testNonRegression 'Jenkinsfile_works_as_expected'
    }

}
