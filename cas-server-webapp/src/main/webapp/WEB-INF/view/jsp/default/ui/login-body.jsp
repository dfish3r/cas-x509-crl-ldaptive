<%--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.

--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<form:form id="fm1" cssClass="fl-col-fixed-300 fl-force-left" method="post" commandName="${commandName}" htmlEscape="true">
    <form:errors path="*" cssClass="errors" id="status" element="div" />
    <div class="box fl-panel" id="login">
        <!-- Congratulations on bringing CAS online!  The default authentication handler authenticates where usernames equal passwords: go ahead, try it out.  -->
        <h2>Enter your NetID and Password</h2>
        <div class="row fl-controls-left">
            <label for="userName" class="fl-label">
                <span class="accesskey">U</span>sername:
            </label>
            <form:input cssClass="required" cssErrorClass="error" id="userName" size="30" tabindex="1" accesskey="u" path="userName" autocomplete="false" htmlEscape="true" />
        </div>
        <div class="row fl-controls-left">
            <label for="password" class="fl-label">
                <span class="accesskey">P</span>assword:
            </label>
            <form:password cssClass="required" cssErrorClass="error" id="password" size="30" tabindex="2" path="password"  accesskey="p" htmlEscape="true" autocomplete="false" />
        </div>
        <div class="row check">
            <input id="warn" name="warn" value="true" tabindex="3" accesskey="w" type="checkbox">
            <label for="warn">
                <span class="accesskey">W</span>arn me before logging me into other sites.
            </label>
        </div>
        <div class="row btn-row">
            <input type="hidden" name="lt" value="${flowExecutionKey}" />
            <input type="hidden" name="_eventId" value="submit" />

            <input class="btn-submit" name="submit" accesskey="l" value="Sign In" tabindex="4" type="submit"><input class="btn-reset" name="reset" accesskey="c" value="CLEAR" tabindex="5" type="reset">
        </div>
        <div class="row help-links">
            <ul>
                <li>
                    <a href="#"><span>Forgot Password</span></a>
                </li>
                <li>
                    <a href="#"><span>Create Account</span></a>
                </li>
            </ul>
        </div>
    </div>
</form:form>
<div id="sidebar" class="fl-col-flex">
    <p class="fl-panel fl-note fl-bevel-white fl-font-size-80">
        For security reasons, please Log Out and Exit your web browser when you are done accessing services that require authentication!
    </p>
    <div id="list-languages" class="fl-widget menubutton" role="menu">
        <%
            final String queryString = request.getQueryString() == null ? "" : request.getQueryString().replaceAll("&locale=([A-Za-z][A-Za-z]_)?[A-Za-z][A-Za-z]|^locale=([A-Za-z][A-Za-z]_)?[A-Za-z][A-Za-z]", "");
            final java.util.Map<String,String> languages = new java.util.HashMap<String,String>();

           languages.put("en", "English");
           languages.put("es", "Spanish");
           languages.put("fr", "French");
           languages.put("ru", "Russian");
           languages.put("nl", "Nederlands");
           languages.put("sv", "Svenskt");
           languages.put("it", "Italiano");
           languages.put("ur", "Urdu");
           languages.put("zh_CN", "Chinese (Simplified)");
           languages.put("de", "Deutsch");
           languages.put("ja", "Japanese");
           languages.put("hr", "Croatian");
           languages.put("cs", "Czech");
           languages.put("sl", "Slovenian");
           languages.put("pl", "Polish");
           languages.put("ca", "Catalan");
           languages.put("mk", "Macedonian");

            request.setAttribute("languages", languages);
        %>
        <c:set var="query" value="<%=queryString%>" />
        <c:set var="xquery" value="${fn:escapeXml(query)}" />
        <c:set var="currentLanguage" value="${param['locale']}"  />
        <c:set var="loginUrl" value="login?${xquery}${not empty xquery ? '&' : ''}locale=" />

        <div class="fl-widget-titlebar widget-titlebar">
            <a title="" href="javascript:" role="menuitem" tabindex="0"><h3>Languages:</h3> <span id="current_language"><c:out value="${languages[currentLanguage]}" default="English" /> </span></a>
        </div>
        <div class="fl-widget-content widget-content">
            <ul class="fl-listmenu widget-listmenu" role="presentation">
                <c:forEach items="${languages}" var="language">
                    <li role="presentation">
                        <c:choose>
                            <c:when test="${language.key eq currentLanguage}">
                                <a href="${loginUrl}${language.key}" title="English" tabindex="0" role="menuitem" class="menuitem-selected"><span>${language.value}</span></a>
                            </c:when>
                            <c:otherwise>
                                <a href="${loginUrl}${language.key}" title="English" tabindex="0" role="menuitem" class="menuitem-"><span>${language.value}</span></a>
                            </c:otherwise>
                        </c:choose>
                    </li>
                 </c:forEach>
            </ul>
        </div>
    </div>
    <script type="text/javascript">
        $(document).ready(function(){
            up.menubutton("#list-languages", {
                configs: {
                    settings: {
                        padding: 8
                    }
                }
            });
        });
    </script>
</div>
