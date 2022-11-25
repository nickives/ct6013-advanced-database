package uk.edu.glos.s1909632.ct6013.backend.services;

import jakarta.annotation.PreDestroy;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import uk.edu.glos.s1909632.ct6013.backend.DbChoice;
import uk.edu.glos.s1909632.ct6013.backend.persistence.EntityFactory;
import uk.edu.glos.s1909632.ct6013.backend.persistence.mongo.MongoEntityFactory;
import uk.edu.glos.s1909632.ct6013.backend.persistence.oracle.OracleEntityFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Stateless(name = "DbChoiceSessionEJB")
public class DbChoiceSessionBean {
    @PersistenceContext
    EntityManager em;

    @EJB
    MongoClientProviderSessionBean mongoClientProvider;

    @Context UriInfo uriInfo;

    public DbChoiceSessionBean() {
    }

    public EntityFactory getEntityFactory() {
        AtomicReference<DbChoice> dbChoice = new AtomicReference<>(DbChoice.ORACLE);
        List<String> dbChoiceParam = uriInfo
                .getQueryParameters()
                .get("db");
        if (dbChoiceParam != null) {
            dbChoiceParam.forEach(c -> {
                if ("mongo".equals(c)) {
                    dbChoice.set(DbChoice.MONGO);
                } else if ("oracle".equals(c)) {
                    dbChoice.set(DbChoice.ORACLE);
                }
            });
        }
        if (dbChoice.get().equals(DbChoice.MONGO)) {
            return new MongoEntityFactory(mongoClientProvider.getDatabase());
        }
        return new OracleEntityFactory(em);
    }

    @PreDestroy
    public void closeDb() {
        em.close();
    }
}
