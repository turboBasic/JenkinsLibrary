withNode {
    stage('checkout') {
        checkout scm
    }
    parallel failFast: false,
        lint: {
            stage('lint') { lint() }
        },
        unitTest: {
            stage('unitTest') { unitTest() }
        }
}

void withNode(Closure body) {
    node getNodeName(), {
        ansiColor 'xterm', body
    }
}

String getNodeName() {
    env.NODE_LABEL ?: 'CloudSmall'
}

void unitTest() {
    try {
        sh './dev test'
    }
    finally {
        publishJUnitReports()
    }
}

void lint() {
    try {
        sh './dev lint'
        if (env.BRANCH_NAME != 'main') {
            sh "./dev lint-commits"
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
