/*******************************************************************************
 * Copyright 2020 Zsolt Kovari
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.zkovari.mermaid.internal;

import java.util.HashSet;
import java.util.Set;

import org.gradle.api.Project;
import org.gradle.api.artifacts.ModuleDependency;
import org.gradle.api.artifacts.ModuleVersionIdentifier;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.artifacts.ResolvedDependency;
import org.zkovari.mermaid.internal.domain.DependencyNode;
import org.zkovari.mermaid.internal.domain.Gavc;
import org.zkovari.mermaid.internal.domain.ImmutableDependencyNode;
import org.zkovari.mermaid.internal.domain.ImmutableDependencyNode.Builder;
import org.zkovari.mermaid.internal.domain.ImmutableGavc;

public class ProjectDependencyGraphBuilder {

    private final Set<Gavc> visitedDependencies;

    private ProjectDependencyGraphBuilder() {
        visitedDependencies = new HashSet<>();
    }

    public static ProjectDependencyGraphBuilder newInstance() {
        return new ProjectDependencyGraphBuilder();
    }

    public DependencyNode buildNode(Project project, String configuration) {
        ImmutableDependencyNode.Builder rootNodeBuilder = ImmutableDependencyNode.builder();
        rootNodeBuilder.gavc(toGavc(project));

        for (ResolvedDependency resolvedDependency : getDependencies(project, configuration)) {
            addOutgoingNodesRecursively(rootNodeBuilder, resolvedDependency, visitedDependencies);
        }

        return rootNodeBuilder.build();
    }

    private Set<ResolvedDependency> getDependencies(Project project, String configuration) {
        return project.getConfigurations().getByName(configuration).getResolvedConfiguration()
                .getFirstLevelModuleDependencies(
                        dep -> (dep instanceof ProjectDependency) || (dep instanceof ModuleDependency));
    }

    private void addOutgoingNodesRecursively(ImmutableDependencyNode.Builder parentNodeBuilder,
            ResolvedDependency resolvedDependency, Set<Gavc> visitedNodes) {
        ImmutableGavc gavc = toGavc(resolvedDependency);
        if (visitedNodes.contains(gavc)) {
            return;
        } else {
            visitedNodes.add(gavc);
        }
        Builder nodeBuilder = ImmutableDependencyNode.builder().gavc(gavc);
        resolvedDependency.getChildren()
                .forEach(child -> addOutgoingNodesRecursively(nodeBuilder, child, visitedNodes));

        ImmutableDependencyNode node = nodeBuilder.build();
        parentNodeBuilder.addOutgoing(node);
    }

    private ImmutableGavc toGavc(ResolvedDependency resolvedDependency) {
        ModuleVersionIdentifier module = resolvedDependency.getModule().getId();
        return ImmutableGavc.builder().group(module.getGroup()).artifactId(module.getName())
                .version(module.getVersion()).build();
    }

    private Gavc toGavc(Project project) {
        return ImmutableGavc.builder().group(project.getGroup().toString()).artifactId(project.getName())
                .version(project.getVersion().toString()).build();
    }

}
