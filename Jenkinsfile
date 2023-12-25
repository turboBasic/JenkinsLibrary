withNode {
    stage('checkout') {
        checkout scm
    }
    stage('unit-test') {
        try {
            buildAndRunTests()
        }
        catch(e) { throw e }
        finally {
            publishReports()
        }
    }
}

void withNode(Closure body) {
    if (env.NODE_LABEL) {
        node(env.NODE_LABEL, body)
    }
    else {
        node(body)
    }
}

void buildAndRunTests() {
    ansiColor('xterm') {
        sh './runDevEnvironment.sh'
    }
}

void publishReports() {
    catchError(buildResult: 'SUCCESS', message: 'Failure in report generation') {
        junit testResults: 'build/test-results/test/*.xml', allowEmptyResults: true
        publishHTML target: [
            allowMissing: true,
            alwaysLinkToLastBuild: false,
            keepAll: true,
            reportDir: 'build/reports/tests/test',
            reportFiles: 'index.html',
            reportName: 'Test summary report'
        ]
    }
}
