/*
 * Copyright 2013 the original author or authors.
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

package org.springframework.data.rest.core.support;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.domain.jpa.JpaRepositoryConfig;
import org.springframework.data.rest.core.domain.jpa.Person;
import org.springframework.data.rest.core.domain.jpa.PersonRepository;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JpaRepositoryConfig.class)
public class DomainObjectMergerTests {

	@Autowired
	PersonRepository personRepository;

	@Autowired
	GenericApplicationContext context;

	@Test
	public void mergeNewValue() {
		Repositories repositories = new Repositories(context.getBeanFactory());
		ConversionService conversionService = new DefaultConversionService();

		Resource<Person> incoming = new Resource<Person>(new Person("Bilbo", "Baggins"));
		Person existingDomainObject = new Person("Frodo", "Baggins");

		DomainObjectMerger merger = new DomainObjectMerger(repositories,
				conversionService);
		merger.merge(incoming.getContent(), existingDomainObject);

		assertThat(existingDomainObject.getFirstName(),
				equalTo(incoming.getContent().getFirstName()));
		assertThat(existingDomainObject.getLastName(),
				equalTo(incoming.getContent().getLastName()));
	}

	/**
	 * @see DATAREST-130
	 */
	@Test
	public void mergeNullValue() {
		Repositories repositories = new Repositories(context.getBeanFactory());
		ConversionService conversionService = new DefaultConversionService();

		Resource<Person> incoming = new Resource<Person>(new Person(null, null));
		Person existingDomainObject = new Person("Frodo", "Baggins");

		DomainObjectMerger merger = new DomainObjectMerger(repositories,
				conversionService);
		merger.merge(incoming.getContent(), existingDomainObject);

		assertThat(existingDomainObject.getFirstName(),
				equalTo(incoming.getContent().getFirstName()));
		assertThat(existingDomainObject.getLastName(),
				equalTo(incoming.getContent().getLastName()));
	}

}