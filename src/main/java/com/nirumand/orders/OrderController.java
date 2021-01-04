package com.nirumand.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    ZeebeClientService clientService;

    @Inject
    ObjectMapper objectMapper;

    @POST
    public String create(Order order) throws JsonProcessingException {
        return clientService.startInstance(order.getOrderId(), order);
    }
}
