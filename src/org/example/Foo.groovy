package org.example


final class Foo implements Serializable {
    private final Script script

    Foo(Script script) {
        this.script = script
    }

    void run() {
        String gitVersion

        script.node() {
            script.stage('init') {
                gitVersion = script.sh(returnStdout: true, script: 'git --version').tokenize().last()
            }
            if (gitVersion < '2.39') {
                return
            }
            script.stage('checkout') {
                script.sh('git clone https://example.org/foo')
            }
        }
    }
}
