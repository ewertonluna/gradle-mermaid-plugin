package org.zkovari.mermaid.api;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.zkovari.mermaid.api.tasks.GenerateMermaidDependenciesDiagram;

public class MermaidGenerationPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        GenerateMermaidDependenciesDiagram generateDependencyDiagramsTask = project.getTasks()
                .create("generateMermaidDependenciesDiagram", GenerateMermaidDependenciesDiagram.class);
    }

}
