package org.zkovari.mermaid.internal;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class GitLabMermaidGeneratorPostActionTest {

    private static final String NEW_LINE = System.lineSeparator();

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();

    private Project project;
    private GitLabMermaidGeneratorPostAction action;

    @Before
    public void setUp() {
        project = ProjectBuilder.builder().withProjectDir(testProjectDir.getRoot()).build();
        action = new GitLabMermaidGeneratorPostAction();
    }

    private void checkOutput() throws IOException {
        Path output = project.getRootDir().toPath().resolve("DEPENDENCY-GRAPH.md");

        assertThat(output).exists();
        assertThat(Files.readAllLines(output)).containsExactly("```mermaid", "graph TD", "```");
    }

    @Test
    public void testRun() throws IOException {
        action.run(project, Collections.emptyList(), "graph TD" + NEW_LINE);
        checkOutput();
    }

    @Test
    public void testRunTwice() throws IOException {
        action.run(project, Collections.emptyList(), "graph TD" + NEW_LINE);
        checkOutput();

        action.run(project, Collections.emptyList(), "graph TD" + NEW_LINE);
        checkOutput();
    }

}
