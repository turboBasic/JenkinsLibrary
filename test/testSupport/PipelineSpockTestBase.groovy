package testSupport

import com.lesfurets.jenkins.unit.RegressionTest
import spock.lang.Specification


/**
 *  PipelineSpockTestBase class allows test classes which extend it to use capabilities
 *  provided by {@link com.lesfurets.jenkins.unit.BasePipelineTest} class AND
 *  Spock test framework simultaneously.
 *
 *  This is achieved by the following: <br/>
 *  - PipelineSpockTestBase extends Spock's {@link spock.lang.Specification Specification} class <br/>
 *  - PipelineSpockTestBase delegates calls to all missing methods to {@link PipelineTest} class
 *  which in its turn extends {@link com.lesfurets.jenkins.unit.BasePipelineTest BasePipelineTest} class
 *  provided by com.lesfurets.jenkins.unit package <br/>
 */
class PipelineSpockTestBase extends Specification implements RegressionTest {

    @Delegate
    PipelineTest basePipelineTest

    def setup() {
        callStackPath = 'test/resources/callStacks/'
        basePipelineTest = new PipelineTest()
        basePipelineTest.setUp()
    }

}
