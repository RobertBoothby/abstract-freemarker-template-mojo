package com.robertboothby.template.model;

/**
 * This class encapsulates the information about generating one output from the generator.
 */
public class GenerationModel {

    private final String template;
    private final Object model;
    private final String outputName;

    /**
     * Constructs an instance of GeneratedModel representing an output from the generator.
     * @param template The template to use in generation.
     * @param model The model to use with the template.
     * @param outputName The name of the generated output.
     */
    public GenerationModel(String template, Object model, String outputName) {
        this.template = template;
        this.model = model;
        this.outputName = outputName;
    }

    /**
     * The template to use in generation.
     * @return The template to use in generation.
     */
    public String getTemplate() {
        return template;
    }

    /**
     * The model to use with the template.
     * @return The model to use with the template
     */
    public Object getModel() {
        return model;
    }

    /**
     * The name of the generated output. Typically a relative file name.
     * @return The name of the generated output.
     */
    public String getOutputName() {
        return outputName;
    }

    @Override
    public String toString() {
        return "GenerationModel{" +
                "template='" + template + '\'' +
                ", model=" + model +
                ", outputName='" + outputName + '\'' +
                '}';
    }
}
