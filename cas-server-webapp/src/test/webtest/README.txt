====
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
====

See http://www.ja-sig.org/wiki/display/CAS/CAS+Functional+Tests for the descrition of these tests.

DEPENDENCIES :
- Canoo Webtest 2.5
- The application proxyCallBackTest should be deployed twice an on a trusted Application Server
 over HTTPS

CONFIGURATION FILES : 
- properties\canoo.properties
- properties\local.properties : proxyCallBackURL1 and proxyCallBackURL2 should be mapped to
 proxyCallBackTest applications.

USAGE : 
- launch build.xml with ANT

