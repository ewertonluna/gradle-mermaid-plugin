package org.zkovari.mermaid.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;
import org.zkovari.mermaid.api.tasks.GenerateMermaidDependenciesDiagram;

public class GitLabMermaidGenerationPluginTest {

    Project project;

    @Before
    public void setUp() {
        project = ProjectBuilder.builder().build();
    }

    @Test
    public void testApplyPlugin() {
        project.getPlugins().apply(GitLabMermaidGenerationPlugin.class);

        Task task = project.getTasks().findByName("generateMermaidDependenciesDiagram");
        assertThat(task).isNotNull().isInstanceOf(GenerateMermaidDependenciesDiagram.class);
    }

    @Test
    public void testApplyPlugin_whenMermaidPluginIsApplied() {
        project.getPlugins().apply(MermaidGenerationPlugin.class);
        project.getPlugins().apply(GitLabMermaidGenerationPlugin.class);

        Task task = project.getTasks().findByName("generateMermaidDependenciesDiagram");
        assertThat(task).isNotNull().isInstanceOf(GenerateMermaidDependenciesDiagram.class);
    }

}
