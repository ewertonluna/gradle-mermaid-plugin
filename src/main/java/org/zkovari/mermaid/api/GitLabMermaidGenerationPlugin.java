package org.zkovari.mermaid.api;

import static org.zkovari.mermaid.api.MermaidGenerationPlugin.GENERATE_MERMAID_DEPENDENCIES_DIAGRAM_TASK_NAME;

import org.gradle.api.GradleException;
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
        if (generateMermaidDependenciesDiagramTask instanceof GenerateMermaidDependenciesDiagram) {
            ((GenerateMermaidDependenciesDiagram) generateMermaidDependenciesDiagramTask)
                    .addPostGenerator(new GitLabMermaidPostGenerator());
        } else {
            throw new GradleException("Could not apply plugin " + GitLabMermaidGenerationPlugin.class.getName()
                    + ". Cannot find task of type " + GenerateMermaidDependenciesDiagram.class.getName() + " with name "
                    + GENERATE_MERMAID_DEPENDENCIES_DIAGRAM_TASK_NAME);
        }
    }

}
