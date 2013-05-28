/*
 * Copyright (C) 2010 Michael Baranczak.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pocketgorilla.stripesem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletContext;

/** Provides access to EntityManager and EntityManagerFactory instances, and injection of
 * those instances into classes with JPA annotations. Normally, there should be exactly one
 * instance of this class per web context. If you just need injection on ActionBeans, you
 * won't deal with this class directly; but if you need access to persistence from somewhere
 * besides ActionBeans, you can retrieve a JPAProvider through the getProviderForApplication
 * method.
 *
 * @author Mike Baranczak
 */
public class JPAProvider {

    public static final String PARAM_PERSISTENCE_UNIT_NAME = "stripes-em.persistence.unit.name";

    private EntityManagerFactory emf;
    private final ThreadLocal<EntityManager> threadLocalEM = new ThreadLocal<EntityManager>();
    private boolean manageTransactions;

    private Injector emInjector = new Injector(PersistenceContext.class);

    private Injector.Provider<EntityManager> emProvider =
            new Injector.Provider<EntityManager>() {
                @Override
                public EntityManager get() {
                    return getEntityManager(true);
                }
            };

    private Injector emfInjector = new Injector(PersistenceUnit.class);

    private Injector.Provider<EntityManagerFactory> emfProvider =
            new Injector.Provider<EntityManagerFactory>() {
                @Override
                public EntityManagerFactory get() {
                    return getEntityManagerFactory();
                }
            };


    protected JPAProvider(String persistenceUnitName, boolean manageTransactions) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        if (emf == null) {
            throw new IllegalArgumentException("persistence unit not found: "+persistenceUnitName);
        }
        this.manageTransactions = manageTransactions;
    }

    /** Retrieves the JPAProvider for this application, instantiating it if necessary.
     *
     * @param context the current application context.
     * @return the JPAProvider instance.
     *
     * @throws RuntimeException if the persistence unit name is not specified in web.xml, or if the
     *                          specified unit can't be found.
     */
    public static JPAProvider getProviderForApplication(ServletContext context) {
        // check if provider was already created
        JPAProvider provider = (JPAProvider) context.getAttribute(JPAProvider.class.getName());
        if (provider == null) {
            String puName = context.getInitParameter(PARAM_PERSISTENCE_UNIT_NAME);
            if (puName == null) {
                throw new RuntimeException("missing required context parameter: "+PARAM_PERSISTENCE_UNIT_NAME);
            }
            provider = new JPAProvider(puName, true);
            context.setAttribute(JPAProvider.class.getName(), provider);
        }
        return provider;
    }

    /**
     * @param  create  if true, and the thread-local EntityManager is either null or closed, then a
     *                 new one is created.
     * 
     * @return the thread-local EntityManager.
     */
    public EntityManager getEntityManager(boolean create) {
        EntityManager em = threadLocalEM.get();
        if (((em == null) || (!em.isOpen())) && create) {
            em = emf.createEntityManager();
            threadLocalEM.set(em);
            if (manageTransactions) {
                em.getTransaction().begin();
            }
        }
        return em;
    }

    /** Removes the thread-local EntityManager (which should have previously been closed). */
    void removeEntityManager() {
        threadLocalEM.remove();
    }

    /**
     * @return the configured EntityManagerFactory.
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    /** Injects any members annotated with ${@link javax.persistence.PersistenceContext} or
     * ${@link javax.persistence.PersistenceUnit}.
     *
     * @param ob
     */
    public void inject(Object ob) {
        if (ob != null) {
            try {
                emfInjector.injectAnnotated(ob, emfProvider);
                emInjector.injectAnnotated(ob, emProvider);
            } catch (Exception ex) {
                throw new RuntimeException("Error while injecting", ex);
            }
        }
    }
    
}
