package com.yjh.base.server;

import com.yjh.base.site.entities.BFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * check JPA is ok.
 *
 * Created by yjh on 15-10-4.
 */
@WebServlet(
        name="entityServlet",
        urlPatterns = "/entities",
        loadOnStartup = 1
)
public class EntityServlet extends HttpServlet {
    private final Random random;
    private EntityManagerFactory factory;

    public EntityServlet() {
        try {
            this.random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.factory = Persistence.createEntityManagerFactory("BaseEntityMapping");
    }

    @Override
    public void destroy() {
        super.destroy();
        this.factory.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager manager = null;
        EntityTransaction transaction = null;
        try {
            manager = this.factory.createEntityManager();
            transaction = manager.getTransaction();
            transaction.begin();

            CriteriaBuilder builder = manager.getCriteriaBuilder();
            CriteriaQuery<BFile> ql = builder.createQuery(BFile.class);
            req.setAttribute("files", manager.createQuery(ql.select(ql.from(BFile.class))).getResultList());

            transaction.commit();

            req.getRequestDispatcher("/WEB-INF/b/entities.jsp").forward(req, resp);
        } catch (Exception e) {
            if(transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if(manager != null && manager.isOpen()) {
                manager.close();
            }
        }


    }
}
