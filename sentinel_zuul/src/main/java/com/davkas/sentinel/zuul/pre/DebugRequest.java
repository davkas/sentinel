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
package com.davkas.sentinel.zuul.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.Debug;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author Mikey Cohen
 * Date: 3/12/12
 * Time: 1:51 PM
 */
public class DebugRequest extends ZuulFilter {


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    public boolean shouldFilter() {
        return true;
    }

    public Object run() {
        HttpServletRequest req = RequestContext.getCurrentContext().getRequest();

        Debug.addRequestDebug("REQUEST:: " + req.getScheme() + " " + req.getRemoteAddr() + ":" + req.getRemotePort());

        Debug.addRequestDebug("REQUEST:: > " + req.getMethod() + " " + req.getRequestURI() + " " + req.getProtocol());
        final StringBuffer requestURL = req.getRequestURL();
        System.out.println(requestURL);
        final Enumeration headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = (String) headerNames.nextElement();
            String value = req.getHeader(name);
            Debug.addRequestDebug("REQUEST:: > " + name + ":" + value);
        }

        final RequestContext ctx = RequestContext.getCurrentContext();
        if (!ctx.isChunkedRequestBody()) {
            InputStream inp = null;
            try {
                inp = ctx.getRequest().getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String body = null;
            if (inp != null) {
                body = inp.toString();
                Debug.addRequestDebug("REQUEST:: > " + body);

            }
        }
        return null;
    }
}
