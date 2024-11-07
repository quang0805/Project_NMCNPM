package org.project.food_management.request;

import lombok.Data;
import org.project.food_management.model.OrderStatus;

@Data
public class CreateOrderRequest {
    private Long tableId;
    private Long customerId;


}
