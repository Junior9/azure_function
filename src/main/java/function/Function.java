package function;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.BlobTrigger;
import com.microsoft.azure.functions.annotation.EventGridTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {



    @FunctionName("test")
    public HttpResponseMessage test(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        return request.createResponseBuilder(HttpStatus.OK).body("Azure functions").build();
    }


    @FunctionName("blobMonitor")
    public void blobMonitor(
        @BlobTrigger(name = "file",
                    dataType = "binary",
                    path = "samples-workitems/{name}",
                    connection = "AzureWebJobsStorage") byte[] content,
        @BindingName("name") String filename,
        final ExecutionContext context
    ) {
        context.getLogger().info("Name: " + filename + ", Size: " + content.length + " bytes");
    }


    @FunctionName("blobMonitor2")
    public void blobMonitor1(
        @BlobTrigger(name = "file",
                    dataType = "binary",
                    path = "monitor/",
                    connection = "AzureWebJobsStorage") byte[] content,
        @BindingName("name") String filename,
        final ExecutionContext context
    ) {
        context.getLogger().info("Name: " + filename + ", Size: " + content.length + " bytes");
    }


    @FunctionName("eventGridMonitor")
    public void logEvent(
        @EventGridTrigger(name = "event") String content,
        final ExecutionContext context
    ) {
        context.getLogger().info(content);
    }

    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }
}
