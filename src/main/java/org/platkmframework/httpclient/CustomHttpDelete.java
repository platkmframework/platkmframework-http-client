/**
 * ****************************************************************************
 *  Copyright(c) 2024 the original author Eduardo Iglesias Taylor.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	 https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Contributors:
 *  	Eduardo Iglesias Taylor - initial API and implementation
 * *****************************************************************************
 */
package org.platkmframework.httpclient;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

/**
 * CustomHttpDelete
 */
public class CustomHttpDelete extends HttpEntityEnclosingRequestBase {

    /**
     * Atributo METHOD_NAME
     */
    public final static String METHOD_NAME = "DELETE";

    /**
     * Constructor CustomHttpDelete
     */
    public CustomHttpDelete() {
        super();
    }

    /**
     * Constructor CustomHttpDelete
     * @param uri uri
     */
    public CustomHttpDelete(final URI uri) {
        super();
        setURI(uri);
    }

    /**
     * CustomHttpDelete
     * @param uri
     */
    public CustomHttpDelete(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    /**
     * getMethod
     * @return String
     */
    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
