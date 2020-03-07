package org.zkovari.mermaid.api

import java.nio.file.Files

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class MermaidGenerationPluginFunctionalTest extends Specification {

    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    private String testKitGradleProperties

    File settingsFile
    File propertiesFile
    File buildFile

    def setup() {
        File testKitGradlePropertiesResource = new File(
                getClass().getClassLoader().getResource("testkit-gradle.properties").toURI())
        testKitGradleProperties = new String(Files.readAllBytes(testKitGradlePropertiesResource.toPath()))
        settingsFile = testProjectDir.newFile('settings.gradle')
        propertiesFile = testProjectDir.newFile('gradle.properties')
        buildFile = testProjectDir.newFile('build.gradle')

        settingsFile << "rootProject.name = 'test-project'"
        propertiesFile << testKitGradleProperties
        buildFile << """
            plugins {
                id 'org.zkovari.mermaid'
            }
        """
    }

    BuildResult build(String command) {
        return GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath()
                .withArguments(command)
                .build()
    }

    BuildResult buildAndFail(String command) {
        return GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath()
                .withArguments(command)
                .buildAndFail()
    }

    def "run generateMermaidDependenciesDiagram"() {
        when:
        BuildResult result = build('generateMermaidDependenciesDiagram')

        then:
        assert result.task(":generateMermaidDependenciesDiagram").outcome == TaskOutcome.UP_TO_DATE
    }
}
