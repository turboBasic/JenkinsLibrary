package testSupport

import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import com.lesfurets.jenkins.unit.BasePipelineTest


class PipelineTest extends BasePipelineTest {
    final String PATH = '/some/path'
    final String WORKSPACE = '/workspace'

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
        addEnvVar('BUILD_NUMBER', binding.getVariable('currentBuild').number.toString())
        addEnvVar('PATH', PATH)
        addEnvVar('WORKSPACE', WORKSPACE)

        script = loadInlineScript('')
    }

    @Override
    void printCallStack() {
        super.printCallStack()
        println()
    }

    Boolean callStackContainsOutput(String... lines) {
        lines.every { String line ->
            helper.callStack
                .findAll { it.methodName == 'echo' }
                .any { callArgsToString(it).contains(line) }
        }
    }

}
