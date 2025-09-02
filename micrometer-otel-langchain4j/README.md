# Quarkus AI Service - Observability basics

This is an app based on the [Quarkus AI Service - Scopes and memory](https://github.com/cescoffier/langchain4j-deep-dive/tree/main/3-quarkus-ai-services/2-quarkus-ai-service-scopes) from the **langchain4j-deep-dive** training project.

## Requirements for running this app
By default this app uses OpenAI's gpt-4o model.

To use this demo you need an active Open AI account and set the `QUARKUS_LANGCHAIN4J_OPENAI_API_KEY` environment variable to the value of your OpenAI key.

You can obtain the key here: https://platform.openai.com/settings/organization/api-keys

And provision some funds here. You can make plenty of calls with 1$: https://platform.openai.com/settings/organization/billing/overview 

## Running this app
1. Run `./mvnw clean quarkus:dev`
2. Once the app is up, hit the `d` key in the Quarkus console, or open a browser to http://localhost:8080/q/dev-ui/
3. Find the `SmallRye OpenAPI` tile and click the `Swagger UI` link (http://localhost:8080/q/dev-ui/io.quarkus.quarkus-smallrye-openapi/swagger-ui)
4. Expand the `/short` entry and click `Try it out`
5. Enter `My name is Clement` in the `Request body` section and click `Execute`
6. You should see a response in the `Response body` section
7. Try it again with `What is my name?` in the `Request body`
8. You should see some kind of message indicating it doesn't know who Clement is
9. Expand the `/long` entry and click `Try it out`, then follow the same steps, except this time when you ask `What is my name?` it should remember
10. When done, hit the `q` key in the Quarkus console

