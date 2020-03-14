package org.zkovari.mermaid.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.zkovari.mermaid.internal.domain.DependencyNode;

public class GitLabMermaidPostGenerator implements MermaidPostGenerator {

    @Override
    public void generate(Project project, Collection<DependencyNode> rootNodes) {
        try {
            Files.copy(project.getBuildDir().toPath().resolve("graph.md"),
                    project.getRootDir().toPath().resolve("DEPENDENCY-GRAPH.md"));
        } catch (IOException ex) {
            throw new GradleException("Could not copy file", ex);
        }
    }

}
