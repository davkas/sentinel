/*
 * Copyright 2013 Netflix, Inc.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package com.netflix.zuul;

import com.davkas.sentinel.zuul.pre.DebugFilter;
import com.davkas.sentinel.zuul.pre.DebugRequest;
import com.davkas.sentinel.zuul.pre.PreDecorationFilter;
import com.netflix.zuul.filters.FilterRegistry;
import com.netflix.zuul.monitoring.MonitoringHelper;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartServer implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(StartServer.class);

    public void contextInitialized(ServletContextEvent sce) {
        logger.info("starting server");

        // mocks monitoring infrastructure as we don't need it for this simple app
        MonitoringHelper.initMocks();

        // initializes a few java filter examples
        initJavaFilters();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("stopping server");
    }

    private void initJavaFilters() {
        final FilterRegistry r = FilterRegistry.instance();

        r.put("debugFilter",new DebugFilter());
        r.put("DebugRequest",new DebugRequest());
        r.put("PreDecorationFilter",new PreDecorationFilter());
    }

}
