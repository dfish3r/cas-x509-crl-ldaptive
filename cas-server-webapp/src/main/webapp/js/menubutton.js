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
var up;
up = up || {};

(function ($, fluid) {
    /**
    * FLUID COMPONENT: MENUBUTTON
    ---------------------------------*/
    up.menubutton = function (container, options) {
        // Private members.
        var that, cacheResources, initUI, initListeners, manageListIndexClickHandler, manageListIndexKeyDownHandler, openMenu, closeMenu, initialize, menu;
        
        // Public members.
        that = fluid.initView("up.menubutton", container, options);
        
        /**
         * Public. Utility function. Checks passed array's length
         * property. If the length of the array is 0 the array is
         * empty. Returns 'true' if the array is empty.
         * @param {Object} arr
         */
        that.isEmptyArray = function (arr) {
            return ((arr.length > 0) ? false : true);
        };//end:function.
        
        /**
         * Private. Cache common DOM & object references.
         */
        cacheResources = function () {
            that.menu = that.options.configs.state;
            menu = that.menu;
            menu.button = that.container;
            menu.buttonSelector = ("#" + menu.button.attr("id"));
            menu.titlebar = that.locate("titlebar");
            menu.anchor = null;
            menu.content = that.locate("content");
            menu.listmenu = that.locate("listmenu");
            menu.listanchors = that.locate("listmenu").find("a");
            menu.firstAnchor = that.locate("listmenu").find("a:first");
        };//end:function.
        
        /**
         * Private. Initialize the UI for the menubutton component.
         * DOM manipulation and width calculations are handled by this
         * function.
         */
        initUI = function () {
            var label, anchor_width, anchor_height, title;
            
            // Declare & initialize.
            label = 'Languages : <span>' + menu.titlebar.find('span').text() + '</span>';
            title = 'Languages';
            
            // Apply DOM manipulation.
            menu.button.css({"position": "relative"});
            menu.titlebar.css({"padding": that.options.configs.settings.padding});
            menu.titlebar.html('<a id="' + menu.button.attr("id") + that.options.configs.ids.buttonAnchorId + '" href="javascript:;" title="' + title + '" tabindex="0" role="menuitem">' + label + '</a>');
            
            // Calculate button width.
            menu.anchor = menu.titlebar.find("a:first");
            menu.anchor.attr("tabindex", 0);
            anchor_width = (menu.anchor.outerWidth()) + (that.options.configs.settings.padding * 2);
            menu.button.width(anchor_width);
            
            // Calculate content min-width.
            menu.content.css({
                "width": "100%",
                "position": "absolute"
            });
            
            // Calculate listmenu overflow thershold.
            anchor_height = menu.listmenu.find("a:first").outerHeight();
            menu.listmenu.attr("id", menu.button.attr("id") + that.options.configs.ids.listMenuId);
            if (menu.listanchors.length > that.options.configs.settings.maxNumItems) {
                menu.listmenu.css({
                    "height": (anchor_height * that.options.configs.settings.maxNumItems),
                    "overflow-x": "hidden",
                    "overflow-y": "scroll"
                });
            }
            
            // Set 'tabindex' and 'index' attributes.
            menu.listanchors.each(function (idx, obj) {
                var a = $(obj);
                a.attr("tabindex", -1).attr("index", idx);
            });
            menu.firstAnchor.attr("tabindex", 0);
        };//end:function.
        
        /**
         * Private. Initialize listeners for the menubutton component.
         */
        initListeners = function () {
            /**
             * Binds the 'hover' event to the menu.titlebar.
             * Toggles hover class on the menu.button.
             * Removes default scrolling behavior when keying up and down when focus is on the menu dropdown
             */
            $(menu.content).keydown(function(e) { return e.keyCode != 38 && e.keyCode != 40; });
            menu.titlebar.hover(function () {
                //menu.button.toggleClass(that.options.configs.classes.hover);
            });//end:function.
            
            /**
             * Binds the 'focus' event to the menu.anchor.
             * Adds a 'focus' class to the menu.button.
             */
            menu.anchor.bind("focus", function () {
                menu.button.addClass(that.options.configs.classes.focus);
            });//end:function.
            
            /**
             * Binds the 'blur' event to the menu.anchor.
             * Removes a 'focus' class from the menu.button.
             */
            menu.anchor.bind("blur", function () {
                menu.button.removeClass(that.options.configs.classes.focus);
            });//end:function.
            
            /**
             * Binds the 'click' event to the menu.titlebar.
             * Opens and closes menu.listmenu.
             */
            menu.titlebar.bind("click", function () {
                if (!menu.isOpen) {
                    openMenu(menu);
                } else {
                    closeMenu(menu);
                }
            });//end:function.
            
            /**
             * Binds the 'click' event to anchor tags
             * found within menu.listmenu.
             * @param {Object} e
            */
            menu.listanchors.bind("click", function (e) {
                if (menu.isOpen) {
                    manageListIndexClickHandler($(this), menu);
                }//end:if.
            });//end:function.
            
            /**
             * Binds the 'keydown' event to menu.listmenu.
             * @param {Object} e
             */
            menu.listmenu.bind("keydown", function (e) {
                if (menu.isOpen) {
                    if (e.keyCode === 40 || e.keyCode === 38) {
                        manageListIndexKeyDownHandler(e, $(this), menu);
                    }//end:if.
                }//end:if.
            });//end:function.
            
            /**
             * Binds 'click' event listener to the document. Detects a
             * 'click' that occurs outside of the component.
             * @param {Object} e
             */
            $(document).bind("click", function (e) {
                if (that.isEmptyArray($(e.target).parents(menu.buttonSelector))) {
                    if (menu.isOpen) {
                        closeMenu(menu);
                        menu.button.removeClass(that.options.configs.classes.focus);
                    }
                }
            });//end:function.
        };//end:function.
        
        /**
         * Private. Managers the tabindex of menu.listmenu after a
         * click event.
         * @param {Object} target
         * @param {Object} menu
         */
        manageListIndexClickHandler = function (target, menu) {
            var previous;
            menu.listIndex = target.attr("index");
            previous = menu.listmenu.find("a[tabindex='0']");
            previous.removeClass(that.options.configs.classes.selected).attr("tabindex", -1);
            target.addClass(that.options.configs.classes.selected).attr("tabindex", 0).focus();
            menu.titlebar.click();
            menu.anchor.focus();
        };//end:function.
        
        /**
         * Private. Managers the tabindex of menu.listmenu after a
         * keydown event. Only listens for the 'up' and 'down' arrow
         * keys.
         * @param {Object} event
         * @param {Object} target
         * @param {Object} menu
         */
        manageListIndexKeyDownHandler = function (event, target, menu) {
            var a, previous;
            a = menu.listanchors;
            previous = menu.listmenu.find("a[tabindex='0']");
            
            // Arrow down.
            if (event.keyCode === 40) {
                menu.listIndex++;
            }
            // Arrow up.
            if (event.keyCode === 38) {
                menu.listIndex--;
            }
            
            // Set contraints.
            if (menu.listIndex < 0) {
                menu.listIndex = a.length - 1;
            } else if (menu.listIndex > (a.length - 1)) {
                menu.listIndex = 0;
            }
            
            // Apply.
            previous.removeClass(that.options.configs.classes.selected).attr("tabindex", -1);
            $(a[menu.listIndex]).addClass(that.options.configs.classes.selected).attr("tabindex", 0).focus();
        };//end:function.
        
        /**
         * Private. Opens hidden menu.
         * @param {Object} menu
         */
        openMenu = function (menu) {
            menu.isOpen = true;
            menu.button.addClass(that.options.configs.classes.active);
            menu.content.fadeIn("fast");
            menu.anchor.attr("tabindex", -1);
            menu.listmenu.find("a[index='" + menu.listIndex + "']").focus();
        };//end:function.
        
        /**
         * Private. Closes open menu.
         * @param {Object} menu
         */
        closeMenu = function (menu) {
            menu.isOpen = false;
            menu.button.removeClass(that.options.configs.classes.active);
            menu.anchor.attr("tabindex", 0);
            menu.content.hide();
        };//end:function.
        
        /**
         * Private. Entry point for the menubutton component.
         */
        initialize = function () {
            cacheResources();
            initUI();
            initListeners();
        };//end:function.
        
        initialize();
        return that;
    };//end:component.
    
    /**
     * DEFAULTS: MENUBUTTON
     ---------------------------------*/
    fluid.defaults("up.menubutton", {
        selectors: {
            titlebar: ".widget-titlebar",
            content: ".widget-content",
            listmenu: ".widget-listmenu"
        },
        configs: {
            state: {
                isOpen: false,
                button: null,
                buttonSelector: null,
                titlebar: null,
                anchor: null,
                content: null,
                listmenu: null,
                listanchors: null,
                firstAnchor: null,
                listIndex: 0
            },
            ids: {
                buttonAnchorId: "ButtonAnchor",
                listMenuId: "ListMenu"
            },
            classes: {
                hover: "menubutton-hover",
                focus: "menubutton-focus",
                active: "menubutton-active",
                selected: "menuitem-selected"
            },
            settings: {
                padding: 5,
                maxNumItems: 20
            }
        }
    });
})(jQuery, fluid);