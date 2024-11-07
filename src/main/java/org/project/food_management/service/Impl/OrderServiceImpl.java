package org.project.food_management.service.Impl;

import org.project.food_management.model.*;
import org.project.food_management.repository.MenuItemRepository;
import org.project.food_management.repository.OrderItemRepository;
import org.project.food_management.repository.OrderRepository;
import org.project.food_management.request.CreateOrderItemRequest;
import org.project.food_management.request.CreateOrderRequest;
import org.project.food_management.service.MenuItemService;
import org.project.food_management.service.OrderService;
import org.project.food_management.service.TablesService;
import org.project.food_management.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class OrderServiceImpl implements OrderService {


    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private MenuItemService menuItemService;
    private TablesService tablesService;
    private UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, MenuItemService menuItemService, TablesService tablesService, UserService userService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.menuItemService = menuItemService;
        this.tablesService = tablesService;
        this.userService = userService;
    }

    @Override
    public Order findOrderByOrderId(Long id) throws Exception {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty()){
            throw new Exception("Order not found with id: " + id);
        }

        return order.get();
    }

    @Override
    public OrderItem findOrderByOrderItemId(Long id) throws Exception {
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(id);
        if(optionalOrderItem.isEmpty()){
            throw new Exception("Order item not found with item id: " + id);
        }
        return optionalOrderItem.get();
    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllOrderByCustomerId(Long customerId) throws Exception {
        return orderRepository.findAllByCustomerId(customerId);
    }

    @Override
    public List<OrderItem> getAllOrderItemByOrderId(Long orderId) throws Exception {

        return orderItemRepository.findOrderItemsByOrderId(orderId);
    }

    @Override
    public OrderItem createOrderItem(CreateOrderItemRequest req) throws Exception {
        OrderItem orderItem = new OrderItem();
        Order order = findOrderByOrderId(req.getOrderId());
        orderItem.setOrder(order);

        MenuItem menuItem = menuItemService.findMenuItemById(req.getMenuItemId());
        orderItem.setMenuItem(menuItem);

        orderItem.setPrice(menuItem.getPrice());
        orderItem.setQuantity(req.getQuantity());
        orderItem.setSpecialInstruction(req.getSpecialInstruction());

        OrderItem savedOderItem = orderItemRepository.save(orderItem);

        order.getOrderItems().add(savedOderItem);
        order.setTotal_amount(order.getTotal_amount() + orderItem.getPrice() * orderItem.getQuantity());
        orderRepository.save(order);


        return orderItemRepository.save(orderItem);
    }


    // Khi nguoi dung dat hang thi creatOrder se duoc tao ra. Chua co item hay totalAmount.
    @Override
    public Order createOrder(CreateOrderRequest req) throws Exception {
        User user = userService.findUserByUserId(req.getCustomerId());
        Tables tables = tablesService.loadTableById(req.getTableId());
        Order order = new Order();
        order.setCustomer(user);
        order.setTable(tables);
        order.setOrder_time(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotal_amount(0L);

        return orderRepository.save(order);
    }
    @Override
    public void deleteOrderItemById(Long id) throws Exception {
        OrderItem orderItem = findOrderByOrderItemId(id);
        Order order = findOrderByOrderId(orderItem.getOrder().getId());
        order.setTotal_amount(order.getTotal_amount() - orderItem.getPrice() * orderItem.getQuantity());
        orderItemRepository.delete(orderItem);
    }

}
