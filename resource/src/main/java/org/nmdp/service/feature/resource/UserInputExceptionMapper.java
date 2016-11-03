/*

    feature-resource  Feature resources.
    Copyright (c) 2014-2015 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.gnu.org/licenses/lgpl.html

*/
package org.nmdp.service.feature.resource;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import java.util.HashMap;

/**
 * User input exception mapper.
 */
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
