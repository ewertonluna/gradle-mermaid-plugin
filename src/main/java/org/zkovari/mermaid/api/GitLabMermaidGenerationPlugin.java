package org.zkovari.mermaid.api;

import static org.zkovari.mermaid.api.MermaidGenerationPlugin.GENERATE_MERMAID_DEPENDENCIES_DIAGRAM_TASK_NAME;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.zkovari.mermaid.api.tasks.GenerateMermaidDependenciesDiagram;
import org.zkovari.mermaid.internal.GitLabMermaidPostGenerator;

public class GitLabMermaidGenerationPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        if (!project.getPlugins().hasPlugin(MermaidGenerationPlugin.class)) {
            project.getPlugins().apply(MermaidGenerationPlugin.class);
        }
        Task generateMermaidDependenciesDiagramTask = project.getTasks()
                .getByName(GENERATE_MERMAID_DEPENDENCIES_DIAGRAM_TASK_NAME);
        ((GenerateMermaidDependenciesDiagram) generateMermaidDependenciesDiagramTask)
                .addPostGenerator(new GitLabMermaidPostGenerator());
    }

}
