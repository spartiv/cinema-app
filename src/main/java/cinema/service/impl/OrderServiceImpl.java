package cinema.service.impl;

import cinema.dao.OrderDao;
import cinema.lib.Dao;
import cinema.lib.Inject;
import cinema.model.Order;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import cinema.service.OrderService;
import cinema.service.ShoppingCartService;
import java.util.List;

@Dao
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        if (shoppingCart.getTickets().isEmpty()) {
            throw new RuntimeException("Sorry, this shopping cart hasn't got any tickets");
        }
        Order order = new Order();
        User user = shoppingCart.getUser();
        List<Ticket> tickets = shoppingCart.getTickets();
        order.setUser(user);
        order.setTicketList(tickets);
        shoppingCartService.clearShoppingCart(shoppingCart);
        return orderDao.add(order);
    }

    @Override
    public List<Order> getOrdersHistory(User user) {
        return orderDao.getHistoryByUser(user);
    }
}
