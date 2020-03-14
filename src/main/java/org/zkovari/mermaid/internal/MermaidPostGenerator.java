package org.zkovari.mermaid.internal;

import java.util.Collection;

import org.gradle.api.Project;
import org.zkovari.mermaid.internal.domain.DependencyNode;

public interface MermaidPostGenerator {

    void generate(Project project, Collection<DependencyNode> rootNodes);

}
