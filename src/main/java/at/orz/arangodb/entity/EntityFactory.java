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

import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import at.orz.arangodb.entity.CollectionEntity.Figures;
import at.orz.arangodb.http.JsonSequenceEntity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * @author tamtam180 - kirscheless at gmail.com
 *
 */
public class EntityFactory {

	private static Gson gson;
	private static Gson gsonNull;
	private static GsonBuilder getBuilder() {
		return new GsonBuilder()
			.registerTypeAdapter(CollectionStatus.class, new CollectionStatusTypeAdapter())
			.registerTypeAdapter(CollectionEntity.class, new EntityDeserializers.CollectionEntityDeserializer())
			.registerTypeAdapter(DocumentEntity.class, new EntityDeserializers.DocumentEntityDeserializer())
			.registerTypeAdapter(DocumentsEntity.class, new EntityDeserializers.DocumentsEntityDeserializer())
			.registerTypeAdapter(ArangoVersion.class, new EntityDeserializers.VersionDeserializer())
			.registerTypeAdapter(ArangoUnixTime.class, new EntityDeserializers.ArangoUnixTimeDeserializer())
			.registerTypeAdapter(DefaultEntity.class, new EntityDeserializers.DefaultEntityDeserializer())
			.registerTypeAdapter(Figures.class, new EntityDeserializers.FiguresDeserializer())
			.registerTypeAdapter(CursorEntity.class, new EntityDeserializers.CursorEntityDeserializer())
			.registerTypeAdapter(IndexEntity.class, new EntityDeserializers.IndexEntityDeserializer())
			.registerTypeAdapter(IndexesEntity.class, new EntityDeserializers.IndexesEntityDeserializer())
			.registerTypeAdapter(EdgeEntity.class, new EntityDeserializers.EdgeEntityDeserializer())
			.registerTypeAdapter(EdgesEntity.class, new EntityDeserializers.EdgesEntityDeserializer())
			.registerTypeAdapter(ScalarExampleEntity.class, new EntityDeserializers.ScalarExampleEntityDeserializer())
			.registerTypeAdapter(SimpleByResultEntity.class, new EntityDeserializers.SimpleByResultEntityDeserializer())
			.registerTypeAdapter(AdminLogEntity.class, new EntityDeserializers.AdminLogEntryEntityDeserializer())
			.registerTypeAdapter(StatisticsEntity.class, new EntityDeserializers.StatisticsEntityDeserializer())
			.registerTypeAdapter(StatisticsDescriptionEntity.class, new EntityDeserializers.StatisticsDescriptionEntityDeserializer())
			.registerTypeAdapter(ExplainEntity.class, new EntityDeserializers.ExplainEntityDeserializer())
			.registerTypeAdapter(UserEntity.class, new EntityDeserializers.UserEntityDeserializer())
			.registerTypeAdapter(ImportResultEntity.class, new EntityDeserializers.ImportResultEntityDeserializer())
			.registerTypeAdapter(DatabaseEntity.class, new EntityDeserializers.DatabaseEntityDeserializer())
			.registerTypeAdapter(StringsResultEntity.class, new EntityDeserializers.StringsResultEntityDeserializer())
			.registerTypeAdapter(BooleanResultEntity.class, new EntityDeserializers.BooleanResultEntityDeserializer())
			.registerTypeAdapter(Endpoint.class, new EntityDeserializers.EndpointDeserializer())
			;
	}
	static {
		gson = getBuilder().create();
		gsonNull = getBuilder().serializeNulls().create();
	}

	public static <T> CursorEntity<T> createResult(CursorEntity<T> entity, Class<T> clazz) {
		if (entity._array == null) {
			entity.results = Collections.emptyList();
		} else if (entity._array.isJsonNull() || entity._array.size() == 0) {
			entity.results = Collections.emptyList();
			entity._array = null;
		} else {
			ArrayList<T> list = new ArrayList<T>(entity._array.size());
			for (JsonElement elem : entity._array) {
				list.add(gson.fromJson(elem, clazz));
			}
			entity.results = list;
			entity._array = null;
		}
		return entity;
	}

	public static <T> CursorEntity<DocumentEntity<T>> createDocumentResult(CursorEntity<DocumentEntity<T>> entity, Class<T> clazz) {
		if (entity._array == null) {
			entity.results = Collections.emptyList();
		} else if (entity._array.isJsonNull() || entity._array.size() == 0) {
			entity.results = Collections.emptyList();
			entity._array = null;
		} else {
			ArrayList<DocumentEntity<T>> list = new ArrayList<DocumentEntity<T>>(entity._array.size());
			for (JsonElement elem : entity._array) {
				DocumentEntity<T> doc = gson.fromJson(elem, DocumentEntity.class);
				if (doc != null) {
					doc.setEntity(gson.fromJson(elem, clazz));
				}
				list.add(doc);
			}
			entity.results = list;
			entity._array = null;
		}
		return entity;
	}

	public static <T> T createEntity(String jsonText, Type type) {
		return gson.fromJson(jsonText, type);
	}
	
	public static <T> String toJsonString(T obj) {
		return toJsonString(obj, false);
	}
	
	public static <T> JsonSequenceEntity toJsonSequenceEntity(Iterator<T> itr) {
		return new JsonSequenceEntity(itr, gson);
	}
	
	public static <T> String toImportHeaderValues(Collection<? extends Collection<?>> headerValues) {
		StringWriter writer = new StringWriter();
		for (Collection<?> array : headerValues) {
			gson.toJson(array, writer);
			writer.write('\n');
		}
		writer.flush();
		return writer.toString();
	}

	public static <T> String toJsonString(T obj, boolean includeNullValue) {
		return includeNullValue ? gsonNull.toJson(obj) : gson.toJson(obj);
	}

	public static <T> EdgesEntity<T> createEdges(String jsonText, Class<T> clazz) {
		EdgesEntity<T> edges = createEntity(jsonText, EdgesEntity.class);
		edges.edges = createEdges(edges._edges, clazz);
		edges._edges = null;
		return edges;
	}
	private static <T> List<EdgeEntity<T>> createEdges(JsonArray array, Class<T> clazz) {
		
		if (array == null) {
			return null;
		}
		
		ArrayList<EdgeEntity<T>> edges = new ArrayList<EdgeEntity<T>>(array.size());
		for (JsonElement elem: array) {
			EdgeEntity<T> edge = gson.fromJson(elem, EdgeEntity.class);
			if (clazz != null) {
				edge.attributes = gson.fromJson(elem, clazz);
			}
			edges.add(edge);
		}
		
		return edges;
	}
	
	public static <T> ScalarExampleEntity<T> createScalarExampleEntity(ScalarExampleEntity<T> entity, Class<T> clazz) {
		
		if (entity._documentJson != null) {
			DocumentEntity<T> document = gson.fromJson(entity._documentJson, DocumentEntity.class);
			if (document != null) {
				document.setEntity(gson.fromJson(entity._documentJson, clazz));
				entity.setDocument(document);
				entity._documentJson = null;
			}
		}
		
		return entity;
	}
	
}
