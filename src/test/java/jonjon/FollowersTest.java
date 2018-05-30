/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jonjon;

import jonjon.entities.Person;
import jonjon.repos.PersonRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(Neo4jDataAutoConfiguration.class)
public class FollowersTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PersonRepository personRepository;

	@Before
	public void deleteAllBeforeTests() throws Exception {
		LoggingSystem.get(ClassLoader.getSystemClassLoader())
				.setLogLevel(LoggingSystem.ROOT_LOGGER_NAME, LogLevel.ERROR);
		personRepository.deleteAll();
	}


	@Test
	public void shouldCreateEntity() throws Exception {

		final int PERSONS_AMOUNT = 20;

		MvcResult location = mockMvc.perform(
				post("/people").content(
						"{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}"))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", containsString("people/")))
				.andReturn()
				;
		
		Long starterId = null;

		String l = location.getResponse().getHeader("Location");
		Pattern p = Pattern.compile("(\\D*)(\\d+)");
		Matcher m = p.matcher(l);
		if (m.find()) {
			starterId = Long.valueOf(m.group(2));
			System.out.println(m.group(2)+"===========================================================================");
		}


//Long id = personRepository.findAll().iterator().next().getId();
		for (int i=1; i<PERSONS_AMOUNT; i++) {
			String firstName = RandomStringUtils.randomAlphabetic(10);
			String lastName = RandomStringUtils.randomAlphabetic(10);
			Long id=0L;
			MockHttpServletRequestBuilder content = post("/people")
					.content("{\"firstName\": \""+firstName+"\", \"lastName\":\""+lastName+"\"," +
							"\"following\":[\"people/"+ starterId+ "\"]}");

			JsonPathResultMatchers jsonPathResultMatchers;
			MvcResult resultActions = mockMvc.perform(content)
					.andExpect(status().isCreated())
					.andExpect(header().string("Location", containsString("people/")))
					.andReturn();

			/*String link = resultActions.getResponse().getHeader("Location");
			Pattern pattern = Pattern.compile("(\\D*)(\\d+)");
			Matcher matcher = pattern.matcher(link);
			if (matcher.find()) {
				id = Long.valueOf(matcher.group(2));
				//System.out.println(matcher.group(2));
			}

			// = jsonPath("$._embedded.people[0].firstName");

			mockMvc.perform(patch("/people/"+id+"/addFollowing").content(String.valueOf(starterId)))
					.andExpect(status().isCreated())
//					.andExpect(header().string("Location", containsString("people/")))
			;*/

		}
		long startTime = System.currentTimeMillis();
		mockMvc.perform(get("/people/" + starterId + "/getFollowers")); //how long does this operation goes
		long endTime = System.currentTimeMillis();
		System.out.println( (endTime-startTime) );
	}

	@Test
	public void getFollowers() throws Exception {
		List<Person> followers;
		String id = "2233";

		MvcResult mvcResult = mockMvc.perform(get("/people/" + id + "/getFollowers"))
				.andReturn();

		mvcResult.getResponse();
	}


	@Test
	public void shouldQueryEntity() throws Exception {

		mockMvc.perform(post("/people").content(
				"{ \"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
						status().isCreated());

		mockMvc.perform(
				get("/people/search/findByLastName?name={name}", "Baggins")).andExpect(
						status().isOk()).andExpect(
								jsonPath("$._embedded.people[0].firstName").value(
										"Frodo"));
	}


	@Test
	public void shouldPartiallyUpdateEntity() throws Exception {

		MvcResult mvcResult = mockMvc.perform(post("/people").content(
				"{\"firstName\": \"Frodo\", \"lastName\":\"Baggins\"}")).andExpect(
						status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(
				patch(location).content("{\"firstName\": \"Bilbo Jr.\"}")).andExpect(
						status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.firstName").value("Bilbo Jr.")).andExpect(
						jsonPath("$.lastName").value("Baggins"));
	}

}