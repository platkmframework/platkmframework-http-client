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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * RestInfo
 */
public class RestInfo implements Serializable {

    /**
     */
    private static final long serialVersionUID = -7288856423066417291L;

    /**
     * Atributo url
     */
    private String url;

    /**
     * Atributo header
     */
    private Map<String, String> header;

    /**
     * Constructor RestInfo
     */
    public RestInfo() {
        super();
        header = new HashMap<>();
    }

    /**
     * create
     * @return RestInfo
     */
    public static RestInfo create() {
        return new RestInfo();
    }

    /**
     * url
     * @param url url
     * @return RestInfo
     */
    public RestInfo url(String url) {
        this.url = url;
        return this;
    }

    /**
     * header
     * @param key key
     * @param value value
     * @return RestInfo
     */
    public RestInfo header(String key, String value) {
        this.header.put(key, value);
        return this;
    }

    /**
     * getUrl
     * @return String
     */
    public String getUrl() {
        return url;
    }

    /**
     * getHeader
     * @return Map
     */
    public Map<String, String> getHeader() {
        return header;
    }
}
