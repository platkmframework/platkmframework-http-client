/**
 * ****************************************************************************
 *  Copyright(c) 2023 the original author Eduardo Iglesias Taylor.
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
package org.platkmframework.httpclient.error;

/**
 *  custom http error
 */
public class HttpClientAttemptError extends Exception {

    /**
     * Atributo serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * HTTP status
     */
    private int status = -1;

    /**
     * description: constructor
     * @param message: error message
     */
    public HttpClientAttemptError(String message) {
        super(message);
    }

    /**
     * description: constructor
     * @param message: error message
     * @param status: HTTP status
     */
    public HttpClientAttemptError(String message, int status) {
        super(message);
        this.status = status;
    }

    /**
     * description: return the status
     * @return status: HTTP status
     */
    public int getStatus() {
        return status;
    }

    /**
     * description: set the status
     * @param status : HTTP status
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
