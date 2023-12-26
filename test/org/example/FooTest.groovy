package org.example

import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import org.junit.Before
import org.junit.Test
import testSupport.PipelineTest


class FooTest extends PipelineTest {

    @Before
    @Override
    void setUp() throws Exception {
        super.setUp()
    }

    @Test
    void 'Foo returns expected result'() {
        // given
        helper.addShMock('git --version', 'git version 2.40', 0)
        script = loadInlineScript('''
            import org.example.Foo

            new Foo(this).run()
        ''')

        // when
        script.run()

        // then
        printCallStack()
        assertJobStatusSuccess()
        assertCallStackContains('git clone https://example.org/foo')
    }

    private Boolean callStackContainsOutput(String... lines) {
        lines.every { String line ->
            helper.callStack
                .findAll { it.methodName == 'echo' }
                .any { callArgsToString(it).contains(line) }
        }
    }
}
