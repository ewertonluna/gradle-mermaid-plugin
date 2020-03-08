package org.zkovari.mermaid.internal;

import java.util.List;

import org.zkovari.mermaid.internal.domain.DependencyNode;

public class MermaidDiagramGenerator {

    private static final String NEW_LINE = System.lineSeparator();

    public String generate(List<DependencyNode> rootNodes) {
        StringBuilder stringBuilder = new StringBuilder("```mermaid").append(NEW_LINE).append("graph TD;")
                .append(NEW_LINE);
        for (DependencyNode rootNode : rootNodes) {
            appendEdges(rootNode, stringBuilder);
        }
        stringBuilder.append("```");
        return stringBuilder.toString();
    }

    private void appendEdges(DependencyNode parentNode, StringBuilder stringBuilder) {
        for (DependencyNode childNode : parentNode.getOutgoing()) {
            stringBuilder.append(toMermaidEdge(parentNode, childNode));
            stringBuilder.append(NEW_LINE);
            appendEdges(childNode, stringBuilder);
        }
    }

    private String toMermaidEdge(DependencyNode rootNode, DependencyNode dependencyNode) {
        return "  " + rootNode.getGavc().getArtifactId() + "-->" + dependencyNode.getGavc().getArtifactId() + ";";
    }

}
