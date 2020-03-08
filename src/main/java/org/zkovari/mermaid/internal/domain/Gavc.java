package org.zkovari.mermaid.internal.domain;

import org.immutables.value.Value;

@Value.Immutable
public interface Gavc {

    String getGroup();

    String getArtifactId();

    String getVersion();

}
