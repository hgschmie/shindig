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
package org.apache.shindig.social.opensocial;

import org.apache.shindig.social.ResponseItem;
import org.apache.shindig.social.opensocial.model.Activity;

import java.util.List;

public interface ActivitiesService {
  /**
   * Returns a list of activities that correspond to the passed in person ids.
   * @param ids The ids of the people to fetch activities for.
   * @return a response item with the list of activities.
   */
  public ResponseItem<List<Activity>> getActivities(List<String> ids);

  /**
   * Creates the passed in activity for the given user. Once createActivity is
   * called, getActivities will be able to return the Activity.
   * @param personId The id of the person to create the activity for.
   * @param activity The activity to create.
   * @return a response item containing any errors
   */
  public ResponseItem createActivity(String personId, Activity activity);
}