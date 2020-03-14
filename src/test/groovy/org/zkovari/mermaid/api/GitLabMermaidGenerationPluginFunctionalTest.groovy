package org.zkovari.mermaid.api

import java.nio.file.Files

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class GitLabMermaidGenerationPluginFunctionalTest extends Specification{

    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    private String testKitGradleProperties

    File settingsFile
    File propertiesFile
    File rootProjectBuildFile

    def setup() {
        File testKitGradlePropertiesResource = new File(
                getClass().getClassLoader().getResource("testkit-gradle.properties").toURI())
        testKitGradleProperties = new String(Files.readAllBytes(testKitGradlePropertiesResource.toPath()))

        settingsFile = testProjectDir.newFile('settings.gradle')
        propertiesFile = testProjectDir.newFile('gradle.properties')
        rootProjectBuildFile = testProjectDir.newFile('build.gradle')

        propertiesFile << testKitGradleProperties
    }

    BuildResult build(String command) {
        return GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath()
                .withArguments(command, "-s")
                .build()
    }

    def setUpSingleProject() {
        settingsFile << "rootProject.name = 'test-project'"
        rootProjectBuildFile << """
            plugins {
                id 'java'
                id 'org.zkovari.mermaid.ext.gitlab'
            }
            repositories {
                mavenCentral()
            }
        """
    }

    def "run generateMermaidDependenciesDiagram on single project"() {
        given:
        setUpSingleProject()
        rootProjectBuildFile << """
            dependencies {
                compile 'junit:junit:4.12'
            }
        """

        List<String> expectedLines = """```mermaid
graph TD;
  test-project-->junit;
  junit-->hamcrest-core;
```
""".readLines()

        when:
        BuildResult result = build('generateMermaidDependenciesDiagram')

        then:
        assert result.task(":generateMermaidDependenciesDiagram").outcome == TaskOutcome.SUCCESS
        File diagramFile = new File(testProjectDir.root, "DEPENDENCY-GRAPH.md")
        assert diagramFile.exists()
        List<String> actualLines = diagramFile.readLines()
        assert actualLines == expectedLines
    }
}
