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
