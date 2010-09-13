/*
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

"use strict";
var fluid_1_2 = fluid || {};
var flc_cas = flc_cas || {};

(function ($, fluid) {

    /*
    *----------Main CAS User Experience component
    *----------Controls form interactions and the presentation of messages
    */
    flc_cas.casUI = function (container, options) {

        var that,
            initialize, bindEvents;

        that = fluid.initView("flc_cas.casUI", container, options);

        initialize = function () {
            bindEvents();
        };//end:function

        bindEvents = function () {
            window.console && window.console.log('stop1');
        };//end:function

        initialize();
        return that; 

    };//function:end

    fluid.defaults("flc_cas.casUI", {
        errorOpenSpeed: 500,
        errorCloseSpeed: 50,
        selectors: {}
    });

})(jQuery,fluid_1_2)
