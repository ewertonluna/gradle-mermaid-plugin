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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.zkovari.mermaid.internal.domain.DependencyNode;

public class GitLabMermaidPostGenerator implements MermaidPostGenerator {

    @Override
    public void generate(Project project, Collection<DependencyNode> rootNodes) {
        try {
            Files.copy(project.getBuildDir().toPath().resolve("graph.md"),
                    project.getRootDir().toPath().resolve("DEPENDENCY-GRAPH.md"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new GradleException("Could not copy file", ex);
        }
    }

}
