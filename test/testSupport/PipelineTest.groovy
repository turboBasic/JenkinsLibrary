package testSupport

import com.lesfurets.jenkins.unit.BasePipelineTest


class PipelineTest extends BasePipelineTest {
    final String PATH = '/some/path'

    Script script = null

    @Override
    void setUp() {
        scriptRoots = ['jobs', 'resources', './.']
        scriptExtension = ''
        super.setUp()

        helper.registerAllowedMethod("buildDescription", [String], {})
        helper.registerAllowedMethod("buildName", [String], {})

        binding.setVariable('PATH', PATH)
        addEnvVar('PATH', PATH)
        addEnvVar('BUILD_NUMBER', binding.getVariable('currentBuild').number.toString())

        script = loadInlineScript('')
    }

}
