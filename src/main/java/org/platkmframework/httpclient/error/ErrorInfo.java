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
package org.platkmframework.httpclient.error;

import java.io.Serializable;

/**
 * ErrorInfo
 */
public class ErrorInfo implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Atributo status
     */
    private int status;

    /**
     * Atributo message
     */
    private String message;

    /**
     * Constructor ErrorInfo
     */
    public ErrorInfo() {
    }

    /**
     * Constructor ErrorInfo
     * @param status status
     * @param message message
     */
    public ErrorInfo(int status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    /**
     * getStatus
     * @return int
     */
    public int getStatus() {
        return status;
    }

    /**
     * setStatus
     * @param status status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * getMessage
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * setMessage
     * @param message message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
