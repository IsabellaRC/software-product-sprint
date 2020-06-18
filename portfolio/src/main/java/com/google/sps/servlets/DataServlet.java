// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
<<<<<<< HEAD
=======
    comments.clear();
>>>>>>> 196078732999d059b01fa4ebc425b38a0cf35895
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

<<<<<<< HEAD
    ArrayList<String> comments = new ArrayList<String>();
    for (Entity entity : results.asIterable()) {
      String text = (String) entity.getProperty("text");
=======
    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String text = (String) entity.getProperty("text");
      long timestamp = (long) entity.getProperty("timestamp");
>>>>>>> 196078732999d059b01fa4ebc425b38a0cf35895
    
      comments.add(text);
    }
    Gson gson = new Gson();

<<<<<<< HEAD
      response.setContentType("application/json;"); 
      response.getWriter().println(gson.toJson(comments));
    }
=======
    response.setContentType("application/json;"); 
    response.getWriter().println(gson.toJson(comments));
  }
>>>>>>> 196078732999d059b01fa4ebc425b38a0cf35895

  private String convertToJson(ArrayList data) {
    Gson gson = new Gson();
    String json = gson.toJson(data);
    return json;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    comments.clear();
    String text = getParameter(request, "text-input", "");
    long timestamp = System.currentTimeMillis();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", text);
    commentEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/index.html"); 
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
<<<<<<< HEAD
   return value == null ? defaultValue : value;
=======
   return request.getParameter(name) == null ? defaultValue : value;
>>>>>>> 196078732999d059b01fa4ebc425b38a0cf35895
  }
}