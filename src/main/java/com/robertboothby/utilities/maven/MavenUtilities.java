package com.robertboothby.utilities.maven;

import org.apache.maven.project.MavenProject;

import java.io.File;

public class MavenUtilities {

    /**
     * Utility method that takes a directory and if it is not absolute resolves it against the Maven project base directory.
     * @param directory The directory to resolve.
     * @param project The Maven project whose base directory to use.
     * @return the resolved directory.
     * @see <a href="https://maven.apache.org/plugin-developers/common-bugs.html#Resolving_Relative_Paths">Resolving Relative Paths</a>
     */
    public static File getAbsoluteBuildDirectory(File directory, MavenProject project) {
        if(!directory.isAbsolute()){
            directory = new File(project.getBasedir(), directory.getPath());
        }
        return directory;
    }
}
