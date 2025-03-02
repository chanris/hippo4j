/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hippo4j.threadpool.dynamic.mode.config.parser;

import java.io.IOException;
import java.util.*;

/**
 * Config parser handler.
 */
public final class ConfigParserHandler {

    private static final List<ConfigParser> PARSERS = new ArrayList<>();

    private ConfigParserHandler() {
        ServiceLoader<ConfigParser> loader = ServiceLoader.load(ConfigParser.class);
        for (ConfigParser configParser : loader) {
            PARSERS.add(configParser);
        }
        PARSERS.add(new PropertiesConfigParser());
        PARSERS.add(new YamlConfigParser());
        PARSERS.add(new JsonConfigParser());
    }

    public Map<Object, Object> parseConfig(String content, String type) throws IOException {
        return parseConfig(content, ConfigFileTypeEnum.of(type));
    }

    public Map<Object, Object> parseConfig(String content, ConfigFileTypeEnum type) throws IOException {
        for (ConfigParser parser : PARSERS) {
            if (parser.supports(type)) {
                return parser.doParse(content);
            }
        }
        return Collections.emptyMap();
    }

    public static ConfigParserHandler getInstance() {
        return ConfigParserHandlerHolder.INSTANCE;
    }

    /**
     * Config Parser Handler Holder
     */
    private static class ConfigParserHandlerHolder {

        private static final ConfigParserHandler INSTANCE = new ConfigParserHandler();
    }
}
