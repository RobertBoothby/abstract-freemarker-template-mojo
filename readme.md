<h1>Abstract Freemarker Template Mojo</h1>
This project contains an Abstract Mojo that makes it easier to create Maven plugins and Mojos that make use of Freemarker
to generate files.
<h2>Usage</h2>
Simply create a new maven projec that uses the `com.robertboothby:maven-plugin-parent` pom as a parent and this project
as a dependency. Next create your own Mojo that extends the AbstractGeneratorMojo and provides an instance of
GenerationModelRetriever.    