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


var editInnerHTML = "";
var deleteInnerHTML = "";
var currentRow = null;

function swapButtonsForConfirm(rowId, serviceId) {

    resetOldValue();
    var editCell = $("#edit"+rowId);
    var deleteCell = $("#delete"+rowId);

    var row = $("#row" + rowId);
    row.removeClass("over");
    row.addClass("highlightBottom");

    editInnerHTML = editCell.html();
    deleteInnerHTML = deleteCell.html();
    currentRow = rowId;

    editCell.html("Really?");
    deleteCell.html("<a id=\"yes\" href=\"deleteRegisteredService.html?id=" + serviceId + "\">Yes</a> <a id=\"no\" href=\"#\" onclick=\"resetOldValue();return false;\">No</a>");
}

function resetOldValue() {
    if (currentRow != null) {
        var curRow = $("#row"+currentRow);
        curRow.removeClass("over");
        curRow.removeClass("highlightBottom");
        var editCell = $("#edit"+currentRow);
        var deleteCell = $("#delete"+currentRow);

        editCell.html(editInnerHTML);
        deleteCell.html(deleteInnerHTML);

        editInnerHTML = null;
        deleteInnerHTML = null;
        currentRow = null;
    }
}

$(document).ready(function(){
    //focus username field
    $("input:visible:enabled:first").focus();
    //flash error box
    $('#status').animate({ backgroundColor: 'rgb(187,0,0)' }, 30).animate({ backgroundColor: 'rgb(255,238,221)' }, 500);

    //flash success box
    $('#msg').animate({ backgroundColor: 'rgb(51,204,0)' }, 30).animate({ backgroundColor: 'rgb(221,255,170)' }, 500);
});
