/*
 * Copyright (C) 2012 tamtam180
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

package at.orz.avocadodb;

import java.util.Iterator;
import java.util.NoSuchElementException;

import at.orz.avocadodb.entity.CursorEntity;

/**
 * @author tamtam180 - kirscheless at gmail.com
 *
 */
public class CursorResultSet<T> implements Iterable<T> {

	private transient AvocadoDriver driver;
	private transient Class<T> clazz;
	private transient CursorEntity<T> entity;
	private transient int pos;
	
	CursorResultSet(AvocadoDriver driver, Class<T> clazz, CursorEntity<T> entity) {
		this.driver = driver;
		this.clazz = clazz;
		this.entity = entity;
		this.pos = 0;
	}
	
	private void updateEntity() throws AvocadoException {
		long cursorId = entity.getCursorId();
		this.entity = driver.continueQuery(cursorId, this.clazz);
		this.pos = 0;
	}
	
	public Iterator<T> iterator() {
		return new CursorIterator();
	}
	
	public class CursorIterator implements Iterator<T> {

		public boolean hasNext() {
			if (entity == null) {
				return false;
			}
			if (pos < entity.size()) {
				return true;
			}
			if (entity.hasMore()) {
				return true;
			}
			return false;
		}

		public T next() {
			if (hasNext()) {
				if (pos >= entity.size()) {
					try {
						updateEntity();
					} catch (AvocadoException e) {
						throw new IllegalStateException(e);
					}
				}
				return entity.get(pos++);
			}
			throw new NoSuchElementException();
		}

		public void remove() {
			throw new UnsupportedOperationException("remove does not support!!");
		}
		
	}
	
}
