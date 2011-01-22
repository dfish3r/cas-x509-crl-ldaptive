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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page session="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.jasig.cas.server.CasVersion" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<spring:theme code="mobile.custom.css.file" var="mobileCss" text="" />
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
	    <title>CAS &#8211; Central Authentication Service</title>
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/fluid/fss-layout.css" />" />
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/cas.css" />" />
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/menubutton.css" />" />
        <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="black" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link rel="icon" href="<c:url value="/favicon.ico" />" type="image/x-icon" />
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.6/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<c:url value="/js/jquery.keyboard-a11y-1.3.0.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/fluid-1.3.0.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/cas.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/menubutton.js" />"></script>
	</head>
	<body id="cas" class="fl-theme-iphone">
        <div class="flc-screenNavigator-view-container">
            <div class="fl-screenNavigator-view">
                <div id="header" class="flc-screenNavigator-navbar fl-navbar fl-table">
                    <div class="logo">
                        <span>CAS Logo</span>
                    </div>
                    <h1 id="app-name" class="fl-table-cell"><span>Central Authentication System</span></h1>
                    <h2 id="app-name-description" class="fl-table-cell"><span>Providing single secure sign-on for your on-line applications</span></h2>
                </div>
                <div id="content" class="fl-container-flex fl-col-mixed">
                    <tiles:insertAttribute name="content" />

                </div>
                <div id="footer">
                    <div>
                        <p>
                            Copyright &copy; 2005 - 2011 Jasig, Inc. All rights reserved.
                        </p>
                        <p>
                            Powered by <a href="http://www.jasig.org/cas">Jasig Central Authentication Service <%=CasVersion.getVersion()%></a>
                        </p>
                    </div>
                    <a title="go to Jasig home page" href="http://www.jasig.org" id="jasig-logo"><img alt="Jasig Home Page" src="<c:url value="/images/logo_jasig.png" />" width="86" height="62" id="logo" /></a>
                </div>
            </div>
         </div>
         <script type="text/javascript">
             $(document).ready(function(){
             window.console && window.console.log($, fluid);
             var cas_behavior = flc_cas.casUI($('body'), {});
             });
        </script>
    </body>
</html>
