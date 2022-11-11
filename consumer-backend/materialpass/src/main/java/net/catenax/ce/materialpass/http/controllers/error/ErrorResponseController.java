package net.catenax.ce.materialpass.http.controllers.error;

import net.catenax.ce.materialpass.http.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import tools.httpTools;
import tools.logTools;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class ErrorResponseController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    @ResponseBody
    public Response handleError(HttpServletRequest httpRequest) {
        ErrorAttributeOptions options = ErrorAttributeOptions
                .defaults()
                .including(ErrorAttributeOptions.Include.MESSAGE)
                ;
        ServletWebRequest servletWebRequest = new ServletWebRequest(httpRequest);
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(servletWebRequest, options);
        Response response = new Response().mapError(errorAttributes);
        String httpInfo = httpTools.getHttpInfo(httpRequest, response.getStatus());
        logTools.printError(httpInfo + " " + response.errorString());
        return response;
    }
}
