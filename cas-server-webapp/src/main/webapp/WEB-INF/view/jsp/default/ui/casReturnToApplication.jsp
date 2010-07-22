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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:include page="includes/top.jsp" />
<c:set var="redirectTime" value="2" />
<c:set var="postTime" value="2000" />
<c:if test="${loginResponse != null and fn:length(loginResponse.authenticationWarnings) >0}">
    <c:set var="redirectTime" value="7" />
    <c:set var="postTime" value="7000" />
</c:if>

<html>
    <head>
        <title>Redirecting back to your application...</title>
    </head>
    <c:choose>
        <c:when test="${accessResponseResult.operationToPerform eq 'POST'}">
            <body onload="window.setTimeout('document.postForm.submit()',${postTime});">
        </c:when>
        <c:when test="${accessResponseResult.operationToPerform eq 'REDIRECT'}">
            <body onload="window.setTimeout('doRedirect()', ${redirectTime});">
            <script type="text/javascript">
                function doRedirect() {
                    window.location.href = "${accessResponseResult.url}";
                }
            </script>
        </c:when>
    </c:choose>
        <div id="msg" class="success">
            <h2>You're Logged In!</h2>
            <p>We are now redirecting you back to your application.</p>
        </div>

        <c:if test="${loginResponse != null and not empty loginResponse.authenticationWarnings}">
        <div class="info">
            <c:forEach items="${loginResponse.authenticationWarnings}" var="warning">
                <spring:message code="${warning.code}" arguments="${warning.arguments}" text="${warning.message}" />
            </c:forEach>
        </div>
        </c:if>

        <c:if test="${accessResponseResult.operationToPerform == 'POST'}">
            <form action="${accessResponseResult.url}" method="POST" name="postForm">
                <c:forEach items="${accessResponseResult.parameters}" var="entry">
                    <c:forEach items="${entry.value}" var="value">
                        <input type="hidden" name="${entry.key}" value="${fn:escapeXml(value)}" />
                    </c:forEach>
                </c:forEach>
            </form>
        </c:if>
    </body>
            <%
            // TODO display logged out services.
            %>
</html>