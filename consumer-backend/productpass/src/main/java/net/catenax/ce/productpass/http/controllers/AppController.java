package net.catenax.ce.productpass.http.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.catenax.ce.productpass.models.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.dateTimeTools;
import tools.httpTools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Tag(name = "default")
public class AppController {

    private @Autowired HttpServletRequest httpRequest;
    private @Autowired HttpServletResponse httpResponse;


    @GetMapping("/")
    @Hidden                     // hides this endpoint from api documentation - swagger-ui
    public Response index(){
        Response response = httpTools.getResponse();
        response.message = "Welcome to the Catena-X Consumer Backend!";
        return response;
    }


    @GetMapping("/health")
    @Operation(summary = "Returns the backend health status", responses = {
            @ApiResponse(description = "Gets the application health", responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)))
    })
    public Response health(){
        Response response = httpTools.getResponse(
                "RUNNING",
                200
        );
        response.data = dateTimeTools.getDateTimeFormatted(null);
        return response;
    }
}
