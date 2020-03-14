package org.zkovari.mermaid.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;
import org.zkovari.mermaid.api.tasks.GenerateMermaidDependenciesDiagram;

public class MermaidGenerationPluginTest {

    Project project;

    @Before
    public void setUp() {
        project = ProjectBuilder.builder().build();
    }

    @Test
    public void testApplyPlugin() {
        project.getPlugins().apply(MermaidGenerationPlugin.class);

        Task task = project.getTasks().findByName("generateMermaidDependenciesDiagram");
        assertThat(task).isNotNull().isInstanceOf(GenerateMermaidDependenciesDiagram.class);
    }

}
