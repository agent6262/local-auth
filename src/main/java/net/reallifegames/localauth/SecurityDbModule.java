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
package net.reallifegames.localauth;

import javax.annotation.Nonnull;

/**
 * A module to handle all security and authentication related functions.
 *
 * @author Tyler Bucher
 */
public final class SecurityDbModule implements SecurityModule {

    /**
     * A static class to hold the singleton.
     */
    private static final class SingletonHolder {

        /**
         * The security module singleton.
         */
        private static final SecurityDbModule INSTANCE = new SecurityDbModule();
    }

    /**
     * @return {@link SecurityDbModule} singleton.
     */
    public static SecurityDbModule getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public boolean isUserAdmin(@Nonnull final String authUsername) {
        return LocalAuth.getDbModule().getAdminStatus(authUsername);
    }

    /**
     * Checks to see if the user is an admin.
     *
     * @param authUsername the attempted admins username.
     * @param dbModule     the module instance to use.
     * @return true if the user is an admin false otherwise.
     */
    public boolean isUserAdmin(@Nonnull final String authUsername, @Nonnull final DbModule dbModule) {
        return dbModule.getAdminStatus(authUsername);
    }
}
