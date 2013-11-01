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

package at.orz.arangodb;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Map.Entry;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import at.orz.arangodb.ArangoException;
import at.orz.arangodb.entity.AdminLogEntity;
import at.orz.arangodb.entity.AdminStatusEntity;
import at.orz.arangodb.entity.ArangoUnixTime;
import at.orz.arangodb.entity.ArangoVersion;
import at.orz.arangodb.entity.ConnectionStatisticsEntity;
import at.orz.arangodb.entity.DefaultEntity;
import at.orz.arangodb.util.ReflectionUtils;

/**
 * @author tamtam180 - kirscheless at gmail.com
 *
 */
public class ArangoDriverAdminTest extends BaseTest {

	public ArangoDriverAdminTest(ArangoConfigure configure, ArangoDriver driver) {
		super(configure, driver);
	}

	@Test
	public void test_version() throws ArangoException {
		
		ArangoVersion version = driver.getVersion();
		assertThat(version.getServer(), is("arango"));
		assertThat(version.getVersion(), is("1.4.0"));
		
	}
	
	@Test
	public void test_time() throws ArangoException {
		
		ArangoUnixTime time = driver.getTime();
		assertThat(time.getSecond(), is(not(0)));
		assertThat(time.getMillisecond(), is(not(0L)));
		assertThat(time.getMicrosecond(), is(not(0L)));
		
		System.out.println("unixtime=" + time.getSecond());
		System.out.println("unixtime_millis=" + time.getMillisecond());
		System.out.println("unixtime_micros=" + time.getMicrosecond());
		
	}
	
	@Test
	public void test_log_all() throws ArangoException {
		
		AdminLogEntity entity = driver.getServerLog(
				null, null, null, null, null, null, null);
		
		assertThat(entity, is(notNullValue()));
		assertThat(entity.getTotalAmount(), is(not(0)));
		assertThat(entity.getLogs().size(), is(entity.getTotalAmount()));
		
		// debug
		for (AdminLogEntity.LogEntry log : entity.getLogs()) {
			System.out.printf("%d\t%d\t%tF %<tT\t%s%n", log.getLid(), log.getLevel(), log.getTimestamp(), log.getText());
		}
		
	}

	@Test
	public void test_log_text() throws ArangoException {
		
		AdminLogEntity entity = driver.getServerLog(
				null, null, null, null, null, null, "Fun");
		
		assertThat(entity, is(notNullValue()));
		// debug
		for (AdminLogEntity.LogEntry log : entity.getLogs()) {
			System.out.printf("%d\t%d\t%tF %<tT\t%s%n", log.getLid(), log.getLevel(), log.getTimestamp(), log.getText());
		}
		
	}

	// TODO テスト増やす
	
	@Test
	public void test_status() throws ArangoException {
		
		AdminStatusEntity status = driver.getServerStatus();
		
		// debug
		System.out.println(status.getMinorPageFaults());
		System.out.println(status.getMajorPageFaults());
		System.out.println(status.getUserTime());
		System.out.println(status.getSystemTime());
		System.out.println(status.getNumberThreads());
		System.out.println(status.getResidentSize());
		System.out.println(status.getVirtualSize());

	}

	@Test
	public void test_connection_statistics() throws ArangoException {
		
		// Array
		ConnectionStatisticsEntity cs = driver.getConnectionStatistics(null, null);
		System.out.println(new GsonBuilder() .setPrettyPrinting().serializeSpecialFloatingPointValues().create().toJson(cs));

		// One
		cs = driver.getConnectionStatistics(null, 0);
		System.out.println(new GsonBuilder() .setPrettyPrinting().serializeSpecialFloatingPointValues().create().toJson(cs));

	}
	
	@Test
	public void test_flush_modules() throws ArangoException {
		
		DefaultEntity entity = driver.flushModules();
		assertThat(entity.getStatusCode(), is(200));
		assertThat(entity.isError(), is(false));
		
	}

	@Test
	public void test_reload_routing() throws ArangoException {
		
		DefaultEntity entity = driver.reloadRouting();
		assertThat(entity.getStatusCode(), is(200));
		assertThat(entity.isError(), is(false));
		
	}

}
