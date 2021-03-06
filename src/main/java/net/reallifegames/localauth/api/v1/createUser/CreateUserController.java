/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Tyler Bucher
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.reallifegames.localauth.api.v1.createUser;

import io.javalin.http.Context;
import net.reallifegames.localauth.*;
import net.reallifegames.localauth.api.v1.ApiController;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Create user api controller, which listens for post information about creating users. This is a secure api endpoint.
 *
 * @author Tyler Bucher
 */
public class CreateUserController {

    /**
     * Default create user success response.
     */
    private static final CreateUserResponse successResponse = new CreateUserResponse(ApiController.apiResponse, "success");

    /**
     * Default create user error response.
     */
    private static final CreateUserResponse errorResponse = new CreateUserResponse(ApiController.apiResponse, "error");

    /**
     * Attempts to create a new user from the post data.
     *
     * @param context the REST request context to modify.
     */
    public static void postNewUser(@Nonnull final Context context) throws IOException {
        postNewUser(context, SecurityDbModule.getInstance(), LocalAuth.getDbModule());
    }

    /**
     * Attempts to create a new user from the post data.
     *
     * @param context        the REST request context to modify.
     * @param securityModule the module instance to use.
     * @param dbModule       the module instance to use.
     * @throws IOException if the object could not be marshaled.
     */
    public static void postNewUser(@Nonnull final Context context,
                                   @Nonnull final SecurityModule securityModule,
                                   @Nonnull final DbModule dbModule) throws IOException {
        // Set the response type
        final CreateUserRequest userRequest;
        try {
            userRequest = LocalAuth.objectMapper.readValue(context.body(), CreateUserRequest.class);
        } catch (IOException e) {
            ApiController.LOGGER.debug("Api login controller request marshall error", e);
            context.status(400);
            context.result("Bad Request");
            return;
        }
        if (!ApiController.isUserAdminWithWebContext(context, securityModule)) {
            return;
        }
        if (!userRequest.isDataValid()) {
            context.status(406);
            context.result("Not Acceptable");
            return;
        }
        final CreateUserResponse userResponse = userRequest.userExists(dbModule) ? errorResponse : successResponse;
        if (userResponse.equals(successResponse)) {
            context.status(userRequest.createUser(dbModule) ? 200 : 500);
        } else {
            context.status(409);
        }
        // Write json result
        ApiController.jsonContextResponse(userResponse, context);
    }
}
