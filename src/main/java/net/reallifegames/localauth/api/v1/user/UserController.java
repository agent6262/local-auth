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
package net.reallifegames.localauth.api.v1.user;

import io.javalin.http.Context;
import net.reallifegames.localauth.*;
import net.reallifegames.localauth.api.v1.ApiController;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Base users api controller, which dispatches information about users in this application. This is a secure api
 * endpoint.
 *
 * @author Tyler Bucher
 */
public class UserController {

    /**
     * Returns a specific user to the client.
     *
     * @param context the REST request context to modify.
     * @throws IOException if the object could not be marshaled.
     */
    public static void getUser(@Nonnull final Context context) throws IOException {
        getUser(context, SecurityDbModule.getInstance(), LocalAuth.getDbModule());
    }

    /**
     * Returns a specific user to the client.
     *
     * @param context        the REST request context to modify.
     * @param securityModule the module instance to use.
     * @param dbModule       the module instance to use.
     * @throws IOException if the object could not be marshaled.
     */
    public static void getUser(@Nonnull final Context context, @Nonnull final SecurityModule securityModule, @Nonnull DbModule dbModule) throws IOException {
        // Set response type and status code
        final UserRequest userRequest = new UserRequest(context.pathParam(":username", String.class).get());
        // Check if user is an admin
        if (!ApiController.isUserAdminWithWebContext(context, securityModule)) {
            return;
        }
        final UserResponse userResponse = userRequest.getUserResponse(dbModule);
        if (userResponse == null) {
            ApiController.LOGGER.debug("Api user controller db error");
            context.status(500);
            context.result("Internal Server Error");
            return;
        }
        context.status(200);
        // Set response payload
        ApiController.jsonContextResponse(userResponse, context);
    }
}
