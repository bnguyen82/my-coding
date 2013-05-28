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

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** Takes care of closing out the ${@link EntityTransaction} associated
 * with the current HTTP request.
 *
 * @author Mike Baranczak
 */
public class TransactionFilter  implements Filter {


    private static final Log log = LogFactory.getLog(TransactionFilter.class);

    private JPAProvider provider;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        provider = JPAProvider.getProviderForApplication(servletContext);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        if (log.isDebugEnabled())
            log.debug("filtering "+ ((HttpServletRequest)request).getRequestURI());

        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {
            try {
                EntityManager em = provider.getEntityManager(false);
                if (em != null) {
                    EntityTransaction tx = em.getTransaction();
                    if (tx.isActive()) {
                        tx.setRollbackOnly();
                    }
                }
            } finally {
                throw new ServletException(ex);
            }
        } finally {
            doAfter();
        }
    }

    private void doAfter() {
        EntityManager em = provider.getEntityManager(false);
        if ((em != null) && (em.isOpen())) {
            EntityTransaction tx = em.getTransaction();
            if (tx.isActive()) {
                if (tx.getRollbackOnly()) {
                    tx.rollback();
                    log.info("Rolled back persistence transaction.");
                } else {
                    tx.commit();
                    log.debug("Committed persistence transaction.");
                }
            }
            em.close();
            provider.removeEntityManager();
        }
    }

    @Override
    public void destroy() {
        if (provider != null) {
            provider.getEntityManagerFactory().close();
            log.info("closed EntityManagerFactory.");
        }
    }

}
