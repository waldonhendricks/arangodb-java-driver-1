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

package at.orz.arangodb.impl;

import at.orz.arangodb.ArangoConfigure;
import at.orz.arangodb.ArangoException;
import at.orz.arangodb.entity.CollectionEntity;
import at.orz.arangodb.entity.CollectionsEntity;
import at.orz.arangodb.entity.EntityFactory;
import at.orz.arangodb.http.HttpResponseEntity;
import at.orz.arangodb.util.MapBuilder;

/**
 * @author tamtam180 - kirscheless at gmail.com
 *
 */
public class InternalCollectionDriverImpl extends BaseArangoDriverImpl {

	InternalCollectionDriverImpl(ArangoConfigure configure) {
		super(configure);
	}

//	public CollectionEntity createCollection(String name, Boolean waitForSync) throws ArangoException {
//		try {
//			return createCollectionImpl(name, waitForSync);
//		} catch (ArangoException e) {
//			if (HttpManager.is400Error(e) && e.getErrorNumber() == 1207) { // Duplicate
//				if (mode == null || mode == Mode.RETURN_NULL) {
//					return null;
//				}
//				if (mode == Mode.DUP_GET) {
//					// TODO get Document. 別スレッドから消されているかもしれないので取得できるとは限らない。
//					return getCollection(name, Mode.RETURN_NULL);
//				}
//			}
//			throw e;
//		}
//	}
	
	public CollectionEntity createCollection(
			String name, 
			Boolean waitForSync, 
			Integer journalSize, 
			Boolean isSystem, 
			Integer type // TODO
			) throws ArangoException {
		
		HttpResponseEntity res = httpManager.doPost(
				baseUrl + "/_api/collection", 
				null,
				EntityFactory.toJsonString(new MapBuilder()
					.put("name", name)
					.put("waitForSync", waitForSync)
					.put("journalSize", journalSize)
					.put("isSystem", isSystem)
					.put("type", type)
					.get())
					);
		
		return createEntity(res, CollectionEntity.class);
		
	}
	
//	public CollectionEntity getCollection(long id) throws ArangoException {
//		return getCollection(String.valueOf(id));
//	}
	
	public CollectionEntity getCollection(String name) throws ArangoException {
		validateCollectionName(name);
		HttpResponseEntity res = httpManager.doGet(
				baseUrl + "/_api/collection/" + name,
				null);
		try {
			return createEntity(res, CollectionEntity.class);
		} catch (ArangoException e) {
//			if (HttpManager.is404Error(e)) {
//				if (mode == null || mode == Mode.RETURN_NULL) {
//					return null;
//				}
//			}
			throw e;
		}
	}
	
//	public CollectionEntity getCollectionProperties(long id) throws ArangoException {
//		return getCollectionProperties(String.valueOf(id));
//	}
	public CollectionEntity getCollectionProperties(String name) throws ArangoException {
		validateCollectionName(name);
		HttpResponseEntity res = httpManager.doGet(
				baseUrl + "/_api/collection/" + name + "/properties",
				null);
		try {
			return createEntity(res, CollectionEntity.class);
		} catch (ArangoException e) {
//			if (HttpManager.is404Error(e)) {
//				if (mode == null || mode == Mode.RETURN_NULL) {
//					return null;
//				}
//			}
			throw e;
		}
	}
	
//	public CollectionEntity getCollectionCount(long id) throws ArangoException {
//		return getCollectionCount(String.valueOf(id));
//	}
	public CollectionEntity getCollectionCount(String name) throws ArangoException {
		validateCollectionName(name);
		HttpResponseEntity res = httpManager.doGet(
				baseUrl + "/_api/collection/" + name + "/count",
				null);
		try {
			return createEntity(res, CollectionEntity.class);
		} catch (ArangoException e) {
//			if (HttpManager.is404Error(e)) {
//				if (mode == null || mode == Mode.RETURN_NULL) {
//					return null;
//				}
//			}
			throw e;
		}

	}
	
//	public CollectionEntity getCollectionFigures(long id) throws ArangoException {
//		return getCollectionFigures(String.valueOf(id));
//	}
	public CollectionEntity getCollectionFigures(String name) throws ArangoException {
		
		validateCollectionName(name);
		HttpResponseEntity res = httpManager.doGet(
				baseUrl + "/_api/collection/" + name + "/figures",
				null);

		try {
			return createEntity(res, CollectionEntity.class);
		} catch (ArangoException e) {
//			if (HttpManager.is404Error(e)) {
//				if (mode == null || mode == Mode.RETURN_NULL) {
//					return null;
//				}
//			}
			throw e;
		}

	}
	
	public CollectionsEntity getCollections() throws ArangoException {

		HttpResponseEntity res = httpManager.doGet(
				baseUrl + "/_api/collection",
				null);
		
		return createEntity(res, CollectionsEntity.class);
		
	}
	
//	public CollectionEntity loadCollection(long id) throws ArangoException {
//		return loadCollection(String.valueOf(id));
//	}
	public CollectionEntity loadCollection(String name) throws ArangoException {
		
		validateCollectionName(name);
		HttpResponseEntity res = httpManager.doPut(
				baseUrl + "/_api/collection/" + name + "/load", 
				null, 
				null);
		
		try {
			return createEntity(res, CollectionEntity.class);
		} catch (ArangoException e) {
//			if (HttpManager.is404Error(e)) {
//				if (mode == null || mode == Mode.RETURN_NULL) {
//					return null;
//				}
//			}
			throw e;
		}
		
	}

//	public CollectionEntity unloadCollection(long id) throws ArangoException {
//		return unloadCollection(String.valueOf(id));
//	}
	public CollectionEntity unloadCollection(String name) throws ArangoException {
		
		validateCollectionName(name);
		HttpResponseEntity res = httpManager.doPut(
				baseUrl + "/_api/collection/" + name + "/unload",
				null, 
				null);
		
		try {
			return createEntity(res, CollectionEntity.class);
		} catch (ArangoException e) {
//			if (HttpManager.is404Error(e)) {
//				if (mode == null || mode == Mode.RETURN_NULL) {
//					return null;
//				}
//			}
			throw e;
		}
		
	}
	
//	public CollectionEntity truncateCollection(long id) throws ArangoException {
//		return truncateCollection(String.valueOf(id));
//	}
	public CollectionEntity truncateCollection(String name) throws ArangoException {
		
		validateCollectionName(name);
		HttpResponseEntity res = httpManager.doPut(
				baseUrl + "/_api/collection/" + name + "/truncate", 
				null, null);
		
		try {
			return createEntity(res, CollectionEntity.class);
		} catch (ArangoException e) {
//			if (HttpManager.is404Error(e)) {
//				if (mode == null || mode == Mode.RETURN_NULL) {
//					return null;
//				}
//			}
			throw e;
		}
		
	}
	
//	public CollectionEntity setCollectionProperties(long id, boolean newWaitForSync) throws ArangoException {
//		return setCollectionProperties(String.valueOf(id), newWaitForSync);
//	}
	public CollectionEntity setCollectionProperties(String name, boolean newWaitForSync) throws ArangoException {
		
		validateCollectionName(name);
		HttpResponseEntity res = httpManager.doPut(
				baseUrl + "/_api/collection/" + name + "/properties",
				null,
				EntityFactory.toJsonString(
						new MapBuilder("waitForSync", newWaitForSync).get()
				)
		);
		
		try {
			return createEntity(res, CollectionEntity.class);
		} catch (ArangoException e) {
//			if (HttpManager.is404Error(e)) {
//				if (mode == null || mode == Mode.RETURN_NULL) {
//					return null;
//				}
//			}
			throw e;
		}
		
	}
	
//	public CollectionEntity renameCollection(long id, String newName) throws ArangoException {
//		return renameCollection(String.valueOf(id), newName);
//	}
	public CollectionEntity renameCollection(String name, String newName) throws ArangoException {
		
		validateCollectionName(newName);
		HttpResponseEntity res = httpManager.doPut(
				baseUrl + "/_api/collection/" + name + "/rename", 
				null,
				EntityFactory.toJsonString(
						new MapBuilder("name", newName).get()
				)
		);
		
		try {
			return createEntity(res, CollectionEntity.class);
		} catch (ArangoException e) {
//			if (HttpManager.is404Error(e)) {
//				if (mode == null || mode == Mode.RETURN_NULL) {
//					return null;
//				}
//			} else if (HttpManager.is400Error(e) && e.getEntity().getErrorNumber() == 1207) { // DuplicateError
//				if (mode == null || mode == Mode.RETURN_NULL) {
//					return null;
//				}
//			}
			throw e;
		}
		
	}
	
//	public CollectionEntity deleteCollection(long id) throws ArangoException {
//		return deleteCollection(String.valueOf(id));
//	}
	public CollectionEntity deleteCollection(String name) throws ArangoException {
		
		validateCollectionName(name);
		HttpResponseEntity res = httpManager.doDelete(
				baseUrl + "/_api/collection/" + name,
				null);
		
		try {
			return createEntity(res, CollectionEntity.class);
		} catch (ArangoException e) {
//			if (e.getCode() == HttpStatus.SC_NOT_FOUND) {
//				if (mode == null || mode == Mode.RETURN_NULL) {
//					return null;
//				}
//			}
			throw e;
		}
		
	}

	
}