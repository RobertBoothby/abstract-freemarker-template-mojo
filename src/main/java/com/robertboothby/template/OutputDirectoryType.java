package com.robertboothby.template;

import org.apache.maven.model.Resource;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.function.BiConsumer;

/**
 * Enumeration based strategy pattern for adding output directories to the Maven project, used as a parameter on the
 * {@link }AbstractGeneratorMojo}.
 */
public enum OutputDirectoryType {

    /**
     * Treat the output directory as a source directory
     */
    SOURCE          ((p, f) -> p.addCompileSourceRoot(f.getAbsolutePath())),
    /**
     * Treat the output directory as a test source directory
     */
    TEST_SOURCE     ((p, f) -> p.addTestCompileSourceRoot(f.getAbsolutePath())),
    /**
     * Treat the output directory as a resource directory
     */
    RESOURCE        ((p, f) -> p.addResource(resourceForDirectory(f))),
    /**
     * Treat the output directory as a test resource directory
     */
    TEST_RESOURCE   ((p, f) -> p.addTestResource(resourceForDirectory(f))),
    /**
     * Treat the output directory as having no significance
     */
    NONE            ((p, f) -> {});

    private final BiConsumer<MavenProject, File> strategy;

    OutputDirectoryType(BiConsumer<MavenProject, File> strategy) {
        this.strategy = strategy;
    }

    /**
     * This method is invoked to add the directory specified in the file as specified by the enumerated type.
     * @param project The project to add the directory to.
     * @param file The File representing the directory to add.
     */
    public void addToProject(MavenProject project, File file){
        strategy.accept(project,file);
    }

    private static Resource resourceForDirectory(File directory){
        Resource resource = new Resource();
        resource.setDirectory(directory.getAbsolutePath());
        return resource;
    }

}
