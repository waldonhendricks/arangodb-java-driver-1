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

package at.orz.arangodb.entity;

import java.io.Serializable;
import java.util.Map;


/**
 * @author tamtam180 - kirscheless at gmail.com
 *
 */
public class CollectionEntity extends BaseEntity {
	
	String name;
	
	long id;
	
	CollectionType type;
	
	CollectionStatus status;
	
	Boolean waitForSync;

	Boolean isSystem;
	
	Boolean isVolatile;
	
	long journalSize;
	
	long count;
	
	long revision;
	
	Figures figures;

	Map<String, Object> createOptions;
	
	/**
	 * @since 1.4.0
	 */
	long checksum;

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}

	public CollectionType getType() {
		return type;
	}

	public CollectionStatus getStatus() {
		return status;
	}

	public long getChecksum() {
		return checksum;
	}

	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	public void setIsVolatile(Boolean isVolatile) {
		this.isVolatile = isVolatile;
	}

	public void setChecksum(long checksum) {
		this.checksum = checksum;
	}

	public Boolean getWaitForSync() {
		return waitForSync;
	}
	
	public Boolean getIsSystem() {
		return isSystem;
	}
	
	public Boolean getIsVolatile() {
		return isVolatile;
	}

	public long getJournalSize() {
		return journalSize;
	}

	public long getCount() {
		return count;
	}

	public Figures getFigures() {
		return figures;
	}

	public Map<String, Object> getCreateOptions() {
		return createOptions;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setType(CollectionType type) {
		this.type = type;
	}

	public void setStatus(CollectionStatus status) {
		this.status = status;
	}

	public void setWaitForSync(Boolean waitForSync) {
		this.waitForSync = waitForSync;
	}

	public void setSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}
	
	public void setVolatile(Boolean isVolatile) {
		this.isVolatile = isVolatile;
	}
	
	public void setJournalSize(long journalSize) {
		this.journalSize = journalSize;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public void setFigures(Figures figures) {
		this.figures = figures;
	}
	
	public void setCreateOptions(Map<String, Object> createOptions) {
		this.createOptions = createOptions;
	}

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}
	
	public static class Figures implements Serializable {
		
		long aliveCount;
		long aliveSize;
		long deadCount;
		long deadSize;
		long deadDeletion;
		long datafileCount;
		long datafileFileSize;
		long journalsCount;
		long journalsFileSize;
		
		public long getAliveCount() {
			return aliveCount;
		}
		public long getAliveSize() {
			return aliveSize;
		}
		public long getDeadCount() {
			return deadCount;
		}
		public long getDeadSize() {
			return deadSize;
		}
		public long getDeadDeletion() {
			return deadDeletion;
		}
		public long getDatafileCount() {
			return datafileCount;
		}
		public long getDatafileFileSize() {
			return datafileFileSize;
		}
		public long getJournalsCount() {
			return journalsCount;
		}
		public long getJournalsFileSize() {
			return journalsFileSize;
		}
		public void setAliveCount(long aliveCount) {
			this.aliveCount = aliveCount;
		}
		public void setAliveSize(long aliveSize) {
			this.aliveSize = aliveSize;
		}
		public void setDeadCount(long deadCount) {
			this.deadCount = deadCount;
		}
		public void setDeadSize(long deadSize) {
			this.deadSize = deadSize;
		}
		public void setDeadDeletion(long deadDeletion) {
			this.deadDeletion = deadDeletion;
		}
		public void setDatafileCount(long datafileCount) {
			this.datafileCount = datafileCount;
		}
		public void setDatafileFileSize(long datafileFileSize) {
			this.datafileFileSize = datafileFileSize;
		}
		public void setJournalsCount(long journalsCount) {
			this.journalsCount = journalsCount;
		}
		public void setJournalsFileSize(long journalsFileSize) {
			this.journalsFileSize = journalsFileSize;
		}
		
	}


}
