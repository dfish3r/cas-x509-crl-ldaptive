/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.cas.web;

import org.jasig.cas.server.session.SessionStorage;
import org.perf4j.log4j.GraphingStatisticsAppender;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.3.5
 */
public final class StatisticsController extends AbstractController {

    private static final int NUMBER_OF_MILLISECONDS_IN_A_DAY = 86400000;

    private static final int NUMBER_OF_MILLISECONDS_IN_AN_HOUR = 3600000;

    private static final int NUMBER_OF_MILLISECONDS_IN_A_MINUTE = 60000;

    private static final int NUMBER_OF_MILLISECONDS_IN_A_SECOND = 1000;

    private final SessionStorage sessionStorage;

    private final Date upTimeStartDate = new Date();

    private String casTicketSuffix;

    public StatisticsController(final SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    public void setCasTicketSuffix(final String casTicketSuffix) {
        this.casTicketSuffix = casTicketSuffix;
    }

    @Override
    protected ModelAndView handleRequestInternal(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws Exception {
        final ModelAndView modelAndView = new ModelAndView("viewStatisticsView");
        modelAndView.addObject("startTime", this.upTimeStartDate);
        final double difference = System.currentTimeMillis() - this.upTimeStartDate.getTime();

        modelAndView.addObject("upTime", calculateUptime(difference, new LinkedList<Integer>(Arrays.asList(NUMBER_OF_MILLISECONDS_IN_A_DAY, NUMBER_OF_MILLISECONDS_IN_AN_HOUR, NUMBER_OF_MILLISECONDS_IN_A_MINUTE, NUMBER_OF_MILLISECONDS_IN_A_SECOND, 1)), new LinkedList<String>(Arrays.asList("day","hour","minute","second","millisecond"))));
        modelAndView.addObject("totalMemory", Runtime.getRuntime().totalMemory() / 1024 / 1024);
        modelAndView.addObject("maxMemory", Runtime.getRuntime().maxMemory() / 1024 / 1024);
        modelAndView.addObject("freeMemory", Runtime.getRuntime().freeMemory() / 1024 / 1024);
        modelAndView.addObject("availableProcessors", Runtime.getRuntime().availableProcessors());
        modelAndView.addObject("serverHostName", httpServletRequest.getServerName());
        modelAndView.addObject("serverIpAddress", httpServletRequest.getLocalAddr());
        modelAndView.addObject("casTicketSuffix", this.casTicketSuffix);

        final Collection<GraphingStatisticsAppender> appenders = GraphingStatisticsAppender.getAllGraphingStatisticsAppenders();

        modelAndView.addObject("unexpiredTgts", this.sessionStorage.getCountOfActiveSessions());
        modelAndView.addObject("unexpiredSts", this.sessionStorage.getCountOfUnusedAccesses());
        modelAndView.addObject("expiredTgts", this.sessionStorage.getCountOfInactiveSessions());
        modelAndView.addObject("expiredSts", this.sessionStorage.getCountOfUsedAccesses());
        modelAndView.addObject("pageTitle", modelAndView.getViewName());
        modelAndView.addObject("graphingStatisticAppenders", appenders);

        return modelAndView;
    }

    protected String calculateUptime(final double difference, final Queue<Integer> calculations, final Queue<String> labels) {
        if (calculations.isEmpty()) {
            return "";
        }

        final int value = calculations.remove();
        final double time = Math.floor(difference / value);
        final double newDifference = difference - (time * value);
        final String currentLabel = labels.remove();
        final String label = time == 0 || time > 1 ? currentLabel + "s" : currentLabel;

        return Integer.toString(new Double(time).intValue()) + " "+ label + " " + calculateUptime(newDifference, calculations, labels);
        
    }
}
