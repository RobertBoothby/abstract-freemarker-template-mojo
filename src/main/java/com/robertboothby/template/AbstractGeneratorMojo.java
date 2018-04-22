package com.robertboothby.template;

import com.robertboothby.template.model.GenerationModel;
import com.robertboothby.template.model.GenerationModelRetriever;
import com.robertboothby.utilities.lambda.FunctionResult;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import static com.robertboothby.utilities.maven.MavenUtilities.getAbsoluteBuildDirectory;

public abstract class AbstractGeneratorMojo
        extends AbstractMojo {

    /**
     * Root location of the generated files.
     */
    @Parameter(
            defaultValue = "${project.build.directory}/generated-sources/java",
            required = true
    )
    private File outputDirectory;

    /**
     * How to treat the output directory, either as a SOURCE root, a TEST_SOURCE root, a RESOURCE root, A TEST_RESOURCE
     * root or as having a significance of NONE.
     */
    @Parameter (
            defaultValue ="SOURCE",
            required = true
    )
    private OutputDirectoryType outputDirectoryType;

    @Parameter(
            defaultValue = "${project}",
            readonly = true,
            required = true
    )
    protected MavenProject project;

    private Configuration configuration;

    /**
     * Get the generation model retriever to be used by the Mojo.
     * @return the generation model retriever.
     * @throws MojoExecutionException if there is a problem creating the MojoExecutionException.
     */
    protected abstract GenerationModelRetriever getGenerationModelRetriever() throws MojoExecutionException;

    /**
     * Get the instance of the {@link TemplateLoader} to use. By default this method returns a {@link ClassTemplateLoader}
     * configured to use the Generator Mojo's classloader and with base package path congfigured to "/";
     * @return the template loader to use for the Freemarker configuration.
     */
    protected TemplateLoader getTemplateLoader(){
        return new ClassTemplateLoader(this.getClass().getClassLoader(), "/");
    }

    /**
     * Get access to the underlying Maven project.
     * @return the underlying Maven project.
     */
    protected final MavenProject getProject() {
        return project;
    }


    public void execute()
            throws MojoExecutionException {
        //First normalise the output directory.
        outputDirectory = getAbsoluteBuildDirectory(this.outputDirectory, this.project);

        //Setup the Freemarker configuration.
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setTemplateLoader(getTemplateLoader());

        //Generate the files.
        List<FunctionResult<String>> generationFunctionResults =
                getGenerationModelRetriever()
                        .getGenerationModels()
                        .parallelStream()
                        .map(this::generate)
                        .collect(Collectors.toList());

        //Check for any failures during generation.
        String failureDetails = generationFunctionResults
                .stream()
                .filter(FunctionResult::isExceptional)
                .map(FunctionResult::toString)
                .collect(Collectors.joining("\n"));

        if(failureDetails != null && failureDetails.length() > 0){
            throw new MojoExecutionException("Problems in generation:\n\n" + failureDetails);
        }

        //Write out the results of the generation and check for any failures during the write.
        failureDetails = generationFunctionResults
                .stream()
                .map(this::createDirectoryIfNeeded) //Has to be serial as we are creating directories in the filing system.
                .map(this::writeGenerationResult)
                .filter(FunctionResult::isExceptional)
                .map(FunctionResult::toString)
                .collect(Collectors.joining("\n"));

        if(failureDetails != null && failureDetails.length() > 0){
            throw new MojoExecutionException("Problems in writing output:\n\n" + failureDetails);
        }

        //Add the output directory to the project as configured.
        outputDirectoryType.addToProject(this.project, this.outputDirectory);
    }

    private FunctionResult<String> createDirectoryIfNeeded(FunctionResult<String> functionResult) {
        File f = new File(outputDirectory, functionResult.getResultIdentifier());
        if (!f.getParentFile().exists() && !f.getParentFile().mkdirs()) {
            return new FunctionResult<>(functionResult.getResultIdentifier(), new IOException("Could not create directory " + f.getParent()));
        }
        return functionResult;
    }

    private FunctionResult<String> generate(GenerationModel generationModel) {
        try (StringWriter w = new StringWriter()) {
            configuration.getTemplate(generationModel.getTemplate()).process(generationModel.getModel(), w);
            return new FunctionResult<>(generationModel.getOutputName(), w.toString());
        } catch (Exception e) {
            return new FunctionResult<>(generationModel.getOutputName(), e);
        }
    }

    private FunctionResult<String> writeGenerationResult(FunctionResult<String> functionResult) {
        File f = new File(outputDirectory, functionResult.getResultIdentifier());
        try (FileWriter w = new FileWriter(f)) {
            w.write(functionResult.getResult().orElseThrow(() -> new RuntimeException("Processing result that is a failure: " + functionResult.toString())));
        } catch (Exception e) {
            return new FunctionResult<>(functionResult.getResultIdentifier(), e);
        }
        // return original result to signal success and possibly allow further processing in the future.
        return functionResult;
    }
}
