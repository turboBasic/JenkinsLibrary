package testSupport

import com.lesfurets.jenkins.unit.BasePipelineTest


class PipelineTest extends BasePipelineTest {
    final String PATH = '/some/path'

    Script script = null

    @Override
    void setUp() {
        scriptRoots = ['jobs', 'test/resources', './.']
        scriptExtension = ''
        super.setUp()

        helper.registerAllowedMethod("ansiColor", [String, Closure], {
            String termCode, Closure body -> body()
        })
        helper.registerAllowedMethod("buildDescription", [String])
        helper.registerAllowedMethod("buildName", [String])
        helper.registerAllowedMethod('junit', [Map])
        helper.registerAllowedMethod('publishHTML', [Map])

        binding.setVariable('PATH', PATH)
        addEnvVar('PATH', PATH)
        addEnvVar('BUILD_NUMBER', binding.getVariable('currentBuild').number.toString())

        script = loadInlineScript('')
    }

    @Override
    void printCallStack() {
        super.printCallStack()
        println()
    }

}
