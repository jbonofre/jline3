/*
 * Copyright (c) 2002-2020, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * https://opensource.org/licenses/BSD-3-Clause
 */
package org.jline.groovy;

import java.nio.charset.StandardCharsets
import java.nio.file.Path;

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.json.JsonParserType

public class Utils {

    private Utils() {}

    static String toString(Object object) {
        object != null ? object.toString() : 'null'
    }

    static Object toObject(String json) {
        def slurper = new JsonSlurper(type: JsonParserType.LAX)
        slurper.parseText(json)
    }

    static Map<String,Object> toMap(Object object) {
        def out = [:]
        if (object instanceof Closure) {
            out['closure'] = object.getClass().getName()
        } else {
            out = object != null ? object.properties : null
        }
        out
    }

    static String toJson(Object object) {
        String json = object instanceof String ? object : JsonOutput.toJson(object)
        (((json.startsWith("{") && json.endsWith("}"))
            || (json.startsWith("[") && json.endsWith("]"))) && json.length() > 5) ? JsonOutput.prettyPrint(json) : json
    }

    static void persist(Path file, Object object) {
        file.toFile().write(JsonOutput.toJson(object))
    }

}
