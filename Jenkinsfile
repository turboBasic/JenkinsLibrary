withNode {
    stage('checkout') {
        checkout scm
    }
    stage('verify') {
        parallel failFast: false,
            lint: {
                lint()
            },
            'unit-test': {
                unitTest()
            }
    }
}

void withNode(Closure body) {
    Closure wrappedBody = { -> ansiColor 'xterm', body }
    if (env.NODE_LABEL) {
        node env.NODE_LABEL, wrappedBody
    }
    else {
        node wrappedBody
    }
}

void unitTest() {
    try {
        sh './runDevEnvironment.sh'
    }
    finally {
        publishJUnitReports()
    }
}

void lint() {
    final String ARGS = [
        '--user root',
        '--env VALIDATE_ALL_CODEBASE=true',
        "--volume ${env.WORKSPACE}:/tmp/lint",
        '--entrypoint=""',
    ].join(' ')

    try {
        docker.image('oxsecurity/megalinter-python:v7').inside(ARGS) {
            sh '/entrypoint.sh'
        }
    }
    finally {
        resetFilePermissions()
        publishLintReports()
    }
}

void publishJUnitReports() {
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

void publishLintReports() {
    archiveArtifacts artifacts: 'megalinter-reports/megalinter.log, linters_logs/ERROR-*', allowEmptyArchive: true
}

void resetFilePermissions() {
    sh 'sudo find -mindepth 1 -maxdepth 1 -exec chown --preserve-root --recursive jenkins: {} + || true'
    sh 'sudo find -mindepth 1 -maxdepth 1 -exec chmod --preserve-root --recursive u+rw {} + || true'
}
