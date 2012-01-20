/*
 * Copyright (C) 2011 Benoit GUEROUT <bguerout at gmail dot com> and Yves AMSELLEM <amsellem dot yves at gmail dot com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jongo;

import com.jongo.jackson.EntityProcessor;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class Querying {

    EntityProcessor processor;
    DBCollection collection;

    Query query;

    Querying(EntityProcessor processor, DBCollection collection, String query) {
        this.processor = processor;
        this.collection = collection;
        this.query = Query.query(query);
    }

    Querying(EntityProcessor processor, DBCollection collection, String query, Object... parameters) {
        this.processor = processor;
        this.collection = collection;
        this.query = Query.query(query, parameters);
    }

    public Querying on(String fields) {
        this.query = new Query.Builder(query.getQuery()).fields(fields).build();
        return this;
    }

    public <T> T as(Class<T> clazz) {
        return map(processor.createEntityMapper(clazz));
    }

    public <T> T map(DBObjectMapper<T> mapper) {
        DBObject result = collection.findOne(query.toDBObject());
        return result == null ? null : mapper.map(result);
    }
}
