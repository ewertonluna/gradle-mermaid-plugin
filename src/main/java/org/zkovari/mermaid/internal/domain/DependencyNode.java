package org.zkovari.mermaid.internal.domain;

import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface DependencyNode {

    Gavc getGavc();

    List<DependencyNode> getOutgoing();

}
