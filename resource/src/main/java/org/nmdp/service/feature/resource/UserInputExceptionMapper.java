package org.nmdp.service.feature.resource;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;

@Provider
public class UserInputExceptionMapper implements ExceptionMapper<UserInputException> {

    @Override
    public Response toResponse(final UserInputException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new HashMap<String, Object>(){{
                    put("message", exception.getMessage());
                    put("status", 400);
                }})
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}