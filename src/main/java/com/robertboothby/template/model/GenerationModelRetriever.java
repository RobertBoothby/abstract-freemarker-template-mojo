package com.robertboothby.template.model;

import java.util.List;

/**
 * Implementatios of this interface take responsibility for supplying models and template names to the generator.
 */
public interface GenerationModelRetriever {

    /**
     * Retrieve the generation models for generation.
     * @return the generation models for generation.
     */
    List<GenerationModel> getGenerationModels();
}
