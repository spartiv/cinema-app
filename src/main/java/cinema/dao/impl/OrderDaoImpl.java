package cinema.dao.impl;

import cinema.dao.OrderDao;
import cinema.exception.DataProcessingException;
import cinema.lib.Dao;
import cinema.model.Order;
import cinema.model.User;
import cinema.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

@Dao
public class OrderDaoImpl implements OrderDao {
    @Override
    public Order add(Order order) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
            return order;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't insert an order: " + order, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Order> getHistoryByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Order> query = session.createQuery("select o from Order o "
                    + "  join fetch o.user u "
                    + "  join fetch o.ticketList t "
                    + "  join fetch t.movieSession ms"
                    + "  join fetch ms.movie "
                    + "  join fetch ms.cinemaHall "
                    + " where o.user = :user",Order.class);
            query.setParameter("user",user);
            return query.getResultList();
        } catch (Exception e) {
            throw new
                    DataProcessingException("Can't get orders history by the given user" + user,e);
        }
    }
}
