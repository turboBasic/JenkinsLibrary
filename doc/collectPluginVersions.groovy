class Plugin {
    String groupId
    String shortName
    String version

    @Override
    String toString () {
        "${groupId}:${shortName}:${version}"
    }

    static Plugin fromManifest(File manifest) {
        String groupId = extractLineByPrefix(manifest, "Group-Id: ")
        String shortName = extractLineByPrefix(manifest, "Short-Name: ")
        String version = extractLineByPrefix(manifest, "Plugin-Version: ")
        new Plugin(groupId: groupId, shortName: shortName, version: version)
    }

    private static String extractLineByPrefix(File manifest, String prefix) {
        manifest
            .readLines()
            .find { it.startsWith(prefix) }
            .replace(prefix, "")
    }
}

Boolean isRelevant(Plugin plugin) {
    !(plugin.groupId in [
        'io.jenkins.blueocean',
        'org.jenkins-ci.plugins.aws-java-sdk',
        'ru.yandex.qatools.allure',
    ]) &&
    !(plugin.shortName in ['jacoco']) &&    // jacoco brings weird dependencies
    !plugin.version.contains('private')     // Filter plugins installed from file
}

final String jenkinsHome = System.getenv("JENKINS_HOME")

//noinspection GrDeprecatedAPIUsage
final Map<String, Plugin> mavenCoordinates = new FileNameFinder()
    .getFileNames("${jenkinsHome}/plugins", "**/META-INF/MANIFEST.MF")
    .collect { fileName -> Plugin.fromManifest(new File(fileName)) }
    .findAll { isRelevant it }
    .sort { a, b -> (a.groupId + a.shortName) <=> (b.groupId + b.shortName) }
    .collectEntries { plugin -> [(plugin.shortName): plugin] }

println "jobDslVersion=${mavenCoordinates.get("job-dsl").version}"
println "jenkinsVersion=${jenkins.model.Jenkins.getInstance().getVersion()}"
println 'jenkinsPlugins='
println mavenCoordinates
    .values()
    .collect { "${it.groupId}:${it.shortName}".padRight(64) + it.version }
    .join('\n')
