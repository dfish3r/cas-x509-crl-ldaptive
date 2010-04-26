/**
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

package org.jasig.cas.server.session;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

/**
 * Implementation of the TicketRegistry that is backed by a BerkeleyDb.
 * 
 * @author Andres March
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public final class BerkeleyDbSessionStorageImpl extends AbstractSessionStorage implements DisposableBean {

    private EntryBinding ticketBinding;

    private Database ticketDb;

    private Database catalogDb;

    private Environment environment;

    public BerkeleyDbSessionStorageImpl(final List<AccessFactory> accessFactories) throws IOException, DatabaseException {
        this(new FileSystemResource("."), accessFactories);
    }


    public BerkeleyDbSessionStorageImpl(final Resource dbHome, final List<AccessFactory> accessFactories) throws IOException, DatabaseException {
        super(accessFactories);

        final EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setTransactional(true);
        envConfig.setAllowCreate(true);
        envConfig.setTxnNoSync(true);
        envConfig.setTxnWriteNoSync(true);

        final DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(true);
        dbConfig.setAllowCreate(true);

        this.environment = new Environment(dbHome.getFile(), envConfig);
        this.ticketDb = this.environment.openDatabase(null, "ticketDb", dbConfig);
        this.catalogDb = this.environment.openDatabase(null, "catalogDb", dbConfig);
        StoredClassCatalog catalog = new StoredClassCatalog(this.catalogDb);
        this.ticketBinding = new SerialBinding(catalog, Session.class);

        BerkeleyDbSessionImpl.setAccessFactories(accessFactories);

    }

    public Session createSession(final AuthenticationResponse authenticationResponse) {
        final Session session = new BerkeleyDbSessionImpl(authenticationResponse.getAuthentications(), authenticationResponse.getPrincipal());
        BerkeleyDbSessionImpl.setExpirationPolicy(getExpirationPolicy());

        final DatabaseEntry dataEntry = new DatabaseEntry();

        try {
            this.ticketBinding.objectToEntry(session, dataEntry);

            final OperationStatus status = this.ticketDb.put(null, getKeyFromString(session.getId()), dataEntry);

            if (status != OperationStatus.SUCCESS) {
                throw new DatabaseException("Data insertion got status " + status);
            }
        } catch (final DatabaseException e) {
            throw new RuntimeException("Ticket Registry DB failed to add ticket : " + session.getId(), e);
        }

        return session;
    }

    public Session destroySession(final String sessionId) {
        final Session session = findSessionBySessionId(sessionId);

        if (session == null) {
            return null;
        }
        try {
            this.ticketDb.delete(null, getKeyFromString(sessionId));
        } catch (final DatabaseException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        
        return session;
    }

    public Session findSessionBySessionId(final String sessionId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Session updateSession(final Session session) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Session findSessionByAccessId(final String accessId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Set<Session> findSessionsByPrincipal(final String principalName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getCountOfActiveSessions() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void purge() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getCountOfInactiveSessions() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getCountOfUnusedAccesses() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getCountOfUsedAccesses() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void destroy() throws Exception {
        this.catalogDb.close();
        this.ticketDb.close();
        this.environment.close();
    }

    private DatabaseEntry getKeyFromString(final String key) {
        final DatabaseEntry de = new DatabaseEntry();
        StringBinding.stringToEntry(key, de);

        return de;
    }
}
