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

# Some sample commands to create certificates with openssl ca command

# Create the intermediate CA certificate
# NOTE: The -extensions v3_ca is the key to creating a CA cert
openssl ca -config openssl.cnf -name rootCA \
  -extensions v3_ca \
  -in cas-test-intermediate-ca.csr \
  -key intermediateCA/private/cakey.pem \
  -out intermediateCA/cacert.pem

# Create a new certificate issued by CAS Test User CA
openssl req -config openssl.cnf -new \
  -out user-valid.csr -key userCA/private/cert.key
openssl ca -config openssl.cnf -name userCA \
  -key userCA/private/cakey.key \
  -in user-valid.csr \
  -out user-valid.crt

# Revoke a certificate issued by CAS Test User CA
openssl ca -config openssl.cnf \
  -revoke userCA/newcerts/0CA7.pem \
  -crl_reason keyCompromise

# Generate a CRL for CAS Test User CA
openssl ca -config openssl.cnf -name userCA \
  -gencrl \
  -out userCA/crl/crl-`cat userCA/crlnumber`.pem
