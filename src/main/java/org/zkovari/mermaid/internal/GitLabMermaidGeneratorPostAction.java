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
import java.nio.file.StandardOpenOption;
import java.util.Collection;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.zkovari.mermaid.internal.domain.DependencyNode;

public class GitLabMermaidGeneratorPostAction implements MermaidGeneratorPostAction {

    private static final String NEW_LINE = System.lineSeparator();
    private static final String TARGET_FILE_NAME = "DEPENDENCY-GRAPH.md";

    @Override
    public void run(Project project, Collection<DependencyNode> rootNodes, String mermaidDiagram) {
        StringBuilder stringBuilder = new StringBuilder("```mermaid").append(NEW_LINE);
        stringBuilder.append(mermaidDiagram).append("```");

        try {
            Files.write(project.getRootDir().toPath().resolve(TARGET_FILE_NAME), stringBuilder.toString().getBytes(),
                    StandardOpenOption.CREATE);
        } catch (IOException ex) {
            throw new GradleException("Could not write file " + TARGET_FILE_NAME, ex);
        }
    }

}
