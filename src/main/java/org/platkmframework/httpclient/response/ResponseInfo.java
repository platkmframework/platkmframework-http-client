/*******************************************************************************
 * Copyright(c) 2023 the original author Eduardo Iglesias Taylor.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	 https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * 	Eduardo Iglesias Taylor - initial API and implementation
 *******************************************************************************/
package org.platkmframework.httpclient.response;

import java.io.Serializable;

import org.apache.http.Header;

/**
 * description:  response info data object
 */
public class ResponseInfo implements Serializable{
 
	private static final long serialVersionUID = 1L;
	
	private int status; 
	private Object data;
	private String json;
	private String mimeType;
	private String fileName; 
	private boolean attachment;
	private boolean text;
	private String reasonPhrase;
	
	private Header[] header;
	  
	/**
	 * description: constructor
	 */
	public ResponseInfo() {
		super();  
	}
	
	/**
	 * description: constructor
	 * @param status
	 * @param data
	 */
	public ResponseInfo(int status, Object data) {
		this();
		this.status = status; 
		this.data = data;
	}
  
	/**
	 * description: get data
	 * @return response data
	 */
	public Object getData() {
		return data;
	}
	
	/**
	 * description: set data
	 * @param data
	 * @return this class
	 */
	public ResponseInfo  setData(Object data) {
		this.data = data;
		return this;
	}

	/**
	 * description:  http status
	 * @return status number
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * description:  status number
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * description:  mime type
	 * @return
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * description:  set mime
	 * @param mimeType
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * description: file name
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * description: set filename
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isAttachment() {
		return attachment;
	}

	/**
	 * description:  set as attachment file
	 * @param attachment
	 */
	public void setAttachment(boolean attachment) {
		this.attachment = attachment;
	}

	/**
	 * wether file exist
	 * @return
	 */
	public boolean isText() {
		return text;
	}

	/**
	 * description: set text
	 * @param text
	 */
	public void setText(boolean text) {
		this.text = text;
	}

	/**
	 * get header
	 * @return
	 */
	public Header[] getHeader() {
		return header;
	}

	/**
	 * set header
	 * @param header
	 */
	public void setHeader(Header[] header) {
		this.header = header;
	}

	/**
	 * description: reason phrase
	 * @return
	 */
	public String getReasonPhrase() {
		return reasonPhrase;
	}

	/**
	 * description:  set reason
	 * @param reasonPhrase
	 */
	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
	 
}
