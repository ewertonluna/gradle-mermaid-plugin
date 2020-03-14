package org.zkovari.mermaid.api.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.zkovari.mermaid.internal.MermaidDiagramGenerator;
import org.zkovari.mermaid.internal.MermaidPostGenerator;
import org.zkovari.mermaid.internal.ProjectDependencyGraphBuilder;
import org.zkovari.mermaid.internal.domain.DependencyNode;

public class GenerateMermaidDependenciesDiagram extends DefaultTask {

    private String configuration;
    private File output;
    private List<MermaidPostGenerator> postGenerators;

    public GenerateMermaidDependenciesDiagram() {
        postGenerators = new ArrayList<>();
    }

    private MermaidDiagramGenerator diagramGenerator;

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    @Input
    public String getConfiguration() {
        return configuration;
    }

    public void setDiagramGenerator(MermaidDiagramGenerator diagramGenerator) {
        this.diagramGenerator = diagramGenerator;
    }

    @OutputFile
    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public boolean addPostGenerator(MermaidPostGenerator arg0) {
        return postGenerators.add(arg0);
    }

    @TaskAction
    public void generate() {
        List<DependencyNode> allRootNodes = new LinkedList<>();
        ProjectDependencyGraphBuilder graphBuilder = ProjectDependencyGraphBuilder.newInstance();

        if (getProject().getSubprojects().isEmpty()) {
            DependencyNode rootNode = graphBuilder.buildNode(getProject(), configuration);
            allRootNodes.add(rootNode);
        } else {
            for (Project project : getProject().getSubprojects()) {
                DependencyNode childNode = graphBuilder.buildNode(project, configuration);
                allRootNodes.add(childNode);
            }
        }

        write(getOutput(), allRootNodes);

        postGenerators.forEach(postGenerator -> postGenerator.generate(getProject(), allRootNodes));
    }

    private void write(File location, List<DependencyNode> rootNodes) {
        String mermaidGraphString = diagramGenerator.generate(rootNodes);

        try {
            Files.write(location.toPath(), mermaidGraphString.getBytes());
        } catch (IOException ex) {
            throw new GradleException(ex.getMessage(), ex);
        }
    }

}
