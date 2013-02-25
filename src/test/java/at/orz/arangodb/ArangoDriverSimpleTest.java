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

package at.orz.arangodb;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import at.orz.arangodb.ArangoException;
import at.orz.arangodb.CursorResultSet;
import at.orz.arangodb.entity.DocumentEntity;
import at.orz.arangodb.entity.IndexType;
import at.orz.arangodb.entity.ScalarExampleEntity;
import at.orz.arangodb.entity.SimpleByResultEntity;
import at.orz.arangodb.example.Example1;
import at.orz.arangodb.util.MapBuilder;

/**
 * @author tamtam180 - kirscheless at gmail.com
 *
 */
public class ArangoDriverSimpleTest extends BaseTest {

	private String collectionName = "unit_test_simple_test";

	@Before
	public void setup() throws ArangoException {

		// index破棄のために一度削除する
		try {
			driver.deleteCollection(collectionName);
		} catch (ArangoException e) {}
		// Collectionを作る
		try {
			driver.createCollection(collectionName);
		} catch (ArangoException e) {}
		driver.truncateCollection(collectionName);
		
		// テストデータを作る
		for (int i = 0; i < 100; i++) {
			TestComplexEntity01 value = new TestComplexEntity01(
					"user_" + (i % 10), 
					"desc" + (i % 10), 
					i);
			driver.createDocument(collectionName, value, null, null);
		}

	}

	@Test
	public void test_simple_all() throws ArangoException {
		
		CursorResultSet<TestComplexEntity01> rs = driver.executeSimpleAllWithResultSet(collectionName, 0, 0, TestComplexEntity01.class);
		int count = 0;
		while (rs.hasNext()) {
			TestComplexEntity01 entity = rs.next();
			count++;
		}
		rs.close();
		
		assertThat(count, is(100));
		
	}
	
	@Test
	public void test_example_by() throws ArangoException {
		
		CursorResultSet<TestComplexEntity01> rs = driver.executeSimpleByExampleWithResusltSet(
				collectionName, 
				new MapBuilder().put("user", "user_6").get(),
				0, 0, TestComplexEntity01.class);
		int count = 0;
		while (rs.hasNext()) {
			TestComplexEntity01 entity = rs.next();
			count++;
			
			assertThat(entity.getUser(), is("user_6"));
		}
		rs.close();
		
		assertThat(count, is(10));
		
	}
	
	@Test
	public void test_first_example() throws ArangoException {
		
		ScalarExampleEntity<TestComplexEntity01> entity = driver.executeSimpleFirstExample(
				collectionName, 
				new MapBuilder()
					.put("user", "user_5")
					.put("desc", "desc5")
					.get(),
				TestComplexEntity01.class);
		
		DocumentEntity<TestComplexEntity01> doc = entity.getDocument();
		
		assertThat(entity.getStatusCode(), is(200));
		assertThat(doc.getDocumentRevision(), is(not(0L)));
		assertThat(doc.getDocumentHandle(), is(collectionName + "/" + doc.getDocumentKey()));
		assertThat(doc.getDocumentKey(), is(notNullValue()));
		assertThat(doc.getEntity(), is(notNullValue()));
		assertThat(doc.getEntity().getUser(), is("user_5"));
		assertThat(doc.getEntity().getDesc(), is("desc5"));
		
	}

	@Test
	public void test_any() throws ArangoException {
		
		ScalarExampleEntity<TestComplexEntity01> entity = driver.executeSimpleAny(
				collectionName, 
				TestComplexEntity01.class);
		
		for (int i = 0; i < 30; i++) {
			DocumentEntity<TestComplexEntity01> doc = entity.getDocument();
			
			assertThat(entity.getStatusCode(), is(200));
			assertThat(doc.getDocumentRevision(), is(not(0L)));
			assertThat(doc.getDocumentHandle(), is(collectionName + "/" + doc.getDocumentKey()));
			assertThat(doc.getDocumentKey(), is(notNullValue()));
			assertThat(doc.getEntity(), is(notNullValue()));
			assertThat(doc.getEntity().getUser(), is(notNullValue()));
			assertThat(doc.getEntity().getDesc(), is(notNullValue()));
			assertThat(doc.getEntity().getAge(), is(notNullValue()));
		}
	}

	@Test
	public void test_range_no_skiplist() throws ArangoException {
		
		// skip listが無いのでエラーになる
		try {
			CursorResultSet<TestComplexEntity01> rs = driver.executeSimpleRangeWithResultSet(
					collectionName, "age", 5, 30, null, 0, 0, TestComplexEntity01.class);
			fail("例外が発生しないとだめ");
		} catch (ArangoException e) {
			assertThat(e.getErrorNumber(), is(500));
			assertThat(e.getCode(), is(500));
		}
		
	}

	@Test
	public void test_range() throws ArangoException {
		
		// create skip-list
		driver.createIndex(collectionName, IndexType.SKIPLIST, false, "age");
		
		{
			CursorResultSet<TestComplexEntity01> rs = driver.executeSimpleRangeWithResultSet(
					collectionName, "age", 5, 30, null, 0, 0, TestComplexEntity01.class);
			
			int count = 0;
			while (rs.hasNext()) {
				TestComplexEntity01 entity = rs.next();
				count++;
				assertThat(entity, is(notNullValue()));
			}
			rs.close();
			assertThat(count, is(25));
		}
		
		{
			CursorResultSet<TestComplexEntity01> rs = driver.executeSimpleRangeWithResultSet(
					collectionName, "age", 5, 30, true, 0, 0, TestComplexEntity01.class);
			
			int count = 0;
			while (rs.hasNext()) {
				TestComplexEntity01 entity = rs.next();
				count++;
				assertThat(entity, is(notNullValue()));
			}
			rs.close();
			assertThat(count, is(26));
		}
		
		
	}
	
	@Test
	public void test_remove_by_example() throws ArangoException {
		
		SimpleByResultEntity entity = driver.executeSimpleRemoveByExample(
				collectionName, 
				new MapBuilder().put("user", "user_3").get(), 
				null, null);
		
		assertThat(entity.getCode(), is(200));
		assertThat(entity.getCount(), is(10));
		assertThat(entity.getDeleted(), is(10));
		assertThat(entity.getReplaced(), is(0));
		assertThat(entity.getUpdated(), is(0));
		
	}

	@Test
	public void test_remove_by_example_with_limit() throws ArangoException {
		
		SimpleByResultEntity entity = driver.executeSimpleRemoveByExample(
				collectionName, 
				new MapBuilder().put("user", "user_3").get(), 
				null, 5);
		
		assertThat(entity.getCode(), is(200));
		assertThat(entity.getCount(), is(5));
		assertThat(entity.getDeleted(), is(5));
		assertThat(entity.getReplaced(), is(0));
		assertThat(entity.getUpdated(), is(0));
		
	}

	
}
