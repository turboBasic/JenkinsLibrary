package testSupport

import com.lesfurets.jenkins.unit.RegressionTest
import spock.lang.Specification


class PipelineSpockTestBase extends Specification implements RegressionTest {

    @Delegate
    PipelineTest basePipelineTest

    def setup() {
        // Set callstacks path for RegressionTest
        callStackPath = 'test/callstacks/'
        basePipelineTest = new PipelineTest()
        basePipelineTest.setUp()
    }

}
