import testSupport.PipelineSpockTestBase


class JenkinsfileSpec extends PipelineSpockTestBase {

    def 'Jenkinsfile works as expected'() {
        given:
            helper.addShMock('./runDevEnvironment.sh', '...execute build environment...', 0)

        when:
            runScript 'Jenkinsfile'
            printCallStack()
            println()

        then:
            assertJobStatusSuccess()
    }

}
