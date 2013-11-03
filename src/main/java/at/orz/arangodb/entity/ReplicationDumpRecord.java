/*
 * Copyright (C) 2012,2013 tamtam180
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.orz.arangodb.entity;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author tamtam180 - kirscheless at gmail.com
 *
 */
public class ReplicationDumpRecord<T> implements Serializable {

	long tick;
	Type type;
	String key;
	long rev;
	DocumentEntity<T> data;
	
	public static enum Type {
		DOCUMENT_UPSERT(2300),
		EDGE_UPSERT(2301),
		DELETION(2302);
		private final int type;
		private Type(int type) {
			this.type = type;
		}
		public int getType() {
			return type;
		}
		public static Type valueOf(int type) {
			switch (type) {
			case 2300: return DOCUMENT_UPSERT;
			case 2301: return EDGE_UPSERT;
			case 2302: return DELETION;
			}
			return null;
		}
	}
	
	public long getTick() {
		return tick;
	}
	public Type getType() {
		return type;
	}
	public String getKey() {
		return key;
	}
	public long getRev() {
		return rev;
	}
	public DocumentEntity<T> getData() {
		return data;
	}
	public void setTick(long tick) {
		this.tick = tick;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setRev(long rev) {
		this.rev = rev;
	}
	public void setData(DocumentEntity<T> data) {
		this.data = data;
	}
	
}