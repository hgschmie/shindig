/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.apache.shindig.social.core.config;

import org.apache.shindig.social.core.model.AccountImpl;
import org.apache.shindig.social.core.model.ActivityImpl;
import org.apache.shindig.social.core.model.AddressImpl;
import org.apache.shindig.social.core.model.BodyTypeImpl;
import org.apache.shindig.social.core.model.ListFieldImpl;
import org.apache.shindig.social.core.model.MediaItemImpl;
import org.apache.shindig.social.core.model.MessageImpl;
import org.apache.shindig.social.core.model.NameImpl;
import org.apache.shindig.social.core.model.OrganizationImpl;
import org.apache.shindig.social.core.model.PersonImpl;
import org.apache.shindig.social.core.model.UrlImpl;
import org.apache.shindig.social.opensocial.model.Account;
import org.apache.shindig.social.opensocial.model.Activity;
import org.apache.shindig.social.opensocial.model.Address;
import org.apache.shindig.social.opensocial.model.BodyType;
import org.apache.shindig.social.opensocial.model.ListField;
import org.apache.shindig.social.opensocial.model.MediaItem;
import org.apache.shindig.social.opensocial.model.Message;
import org.apache.shindig.social.opensocial.model.Name;
import org.apache.shindig.social.opensocial.model.Organization;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.model.Url;

import com.google.inject.AbstractModule;

/**
 * Provides social api component injection. Implementor may want to replace this module if they need
 * to replace some of the internals of the Social API, like for instance the JSON to Bean to JSON
 * converter Beans, however in general this should not be required, as most default implementations
 * have been specified with the Guice @ImplementedBy annotation.
 */
public class DefaultOpenSocialObjectsModule extends AbstractModule {

  /** {@inheritDoc} */
  @Override
  protected void configure() {
    bind(Account.class).to(AccountImpl.class);
    bind(Activity.class).to(ActivityImpl.class);
    bind(Address.class).to(AddressImpl.class);
    bind(BodyType.class).to(BodyTypeImpl.class);
    bind(ListField.class).to(ListFieldImpl.class);
    bind(MediaItem.class).to(MediaItemImpl.class);
    bind(Message.class).to(MessageImpl.class);
    bind(Name.class).to(NameImpl.class);
    bind(Organization.class).to(OrganizationImpl.class);
    bind(Person.class).to(PersonImpl.class);
    bind(Url.class).to(UrlImpl.class);
  }
}
