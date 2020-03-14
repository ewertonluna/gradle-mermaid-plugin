package org.zkovari.mermaid.api

import java.nio.file.Files

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.Specification

class MermaidGenerationPluginFunctionalTest extends Specification {
    private File childProjectABuildFile
    private File childProjectBBuildFile

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

    def setUpSingleProject() {
        settingsFile << "rootProject.name = 'test-project'"
        rootProjectBuildFile << """
            plugins {
                id 'java'
                id 'org.zkovari.mermaid'
            }
            repositories {
                mavenCentral()
            }
        """
    }

    def setUpMultiProject() {
        Files.createDirectory(testProjectDir.folder.toPath().resolve("project-a"))
        Files.createDirectory(testProjectDir.folder.toPath().resolve("project-b"))
        settingsFile << """
rootProject.name = 'root-project'
include 'project-a'
include 'project-b'
"""
        rootProjectBuildFile << """
            plugins {
                id 'java'
                id 'org.zkovari.mermaid'
            }
            subprojects {
                apply plugin: 'java'
                
                repositories {
                    mavenCentral()
                }
            }
        """

        childProjectABuildFile = testProjectDir.newFile('project-a/build.gradle')
        childProjectBBuildFile = testProjectDir.newFile('project-b/build.gradle')
    }

    BuildResult build(String command) {
        return GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withPluginClasspath()
                .withArguments(command, "-s")
                .build()
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
        File diagramFile = new File(testProjectDir.root, "build/graph.md")
        assert diagramFile.exists()
        List<String> actualLines = diagramFile.readLines()
        assert actualLines == expectedLines
    }

    def "run generateMermaidDependenciesDiagram on single project when gitlab extension is applied"() {
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
        File diagramFile = new File(testProjectDir.root, "build/graph.md")
        assert diagramFile.exists()
        List<String> actualLines = diagramFile.readLines()
        assert actualLines == expectedLines
    }

    def "run generateMermaidDependenciesDiagram on multi project"() {
        given:
        setUpMultiProject()
        childProjectABuildFile << """
            dependencies {
                compile project(':project-b')
                compile 'junit:junit:4.12'
            }
        """
        childProjectBBuildFile << """
            dependencies {
                compile 'org.apache.httpcomponents:httpclient:4.5.12'
            }
        """

        List<String> expectedLines = """```mermaid
graph TD;
  project-a-->project-b;
  project-b-->httpclient;
  httpclient-->httpcore;
  httpclient-->commons-logging;
  httpclient-->commons-codec;
  project-a-->junit;
  junit-->hamcrest-core;
```
""".readLines()

        when:
        BuildResult result = build('generateMermaidDependenciesDiagram')

        then:
        assert result.task(":generateMermaidDependenciesDiagram").outcome == TaskOutcome.SUCCESS
        File diagramFile = new File(testProjectDir.root, "build/graph.md")
        assert diagramFile.exists()
        List<String> actualLines = diagramFile.readLines()
        assert actualLines == expectedLines
    }
}
