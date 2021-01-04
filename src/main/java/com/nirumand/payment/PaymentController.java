package com.nirumand.payment;

import com.nirumand.orders.ZeebeClientService;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {
    private static final Logger LOG = Logger.getLogger(PaymentController.class);

    @Inject
    ZeebeClientService clientService;

    @Path("/{order-id}")
    @POST
    public void receivePayment(@PathParam("order-id") String orderId) {
        LOG.infof("payment received for order {}", orderId);
        clientService.publishMessage(orderId, "payment-received");
    }
}
