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

var highlightcolor="#fc3";
var ns6=document.getElementById&&!document.all;
var previous='';
var eventobj;

// SET FOCUS TO FIRST ELEMENT AND HIDE/SHOW ELEMENTS IF JAVASCRIPT ENABLED


// REGULAR EXPRESSION TO HIGHLIGHT ONLY FORM ELEMENTS
	var intended=/INPUT|TEXTAREA|SELECT|OPTION/

// FUNCTION TO CHECK WHETHER ELEMENT CLICKED IS FORM ELEMENT
	function checkel(which){
		if (which.style && intended.test(which.tagName)){return true}
		else return false
	}

// FUNCTION TO HIGHLIGHT FORM ELEMENT
	function highlight(e){
		if(!ns6){
			eventobj=event.srcElement
			if (previous!=''){
				if (checkel(previous))
				previous.style.backgroundColor=''
				previous=eventobj
				if (checkel(eventobj)) eventobj.style.backgroundColor=highlightcolor
			}
			else {
				if (checkel(eventobj)) eventobj.style.backgroundColor=highlightcolor
				previous=eventobj
			}
		}
	}

