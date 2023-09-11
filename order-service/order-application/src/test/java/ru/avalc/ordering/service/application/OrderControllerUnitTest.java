package ru.avalc.ordering.service.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.avalc.ordering.application.dto.create.CreateOrderCommand;
import ru.avalc.ordering.application.dto.create.CreateOrderResponse;
import ru.avalc.ordering.application.dto.track.TrackOrderQuery;
import ru.avalc.ordering.application.dto.track.TrackOrderResponse;
import ru.avalc.ordering.domain.exception.OrderDomainException;
import ru.avalc.ordering.domain.exception.OrderNotFoundException;
import ru.avalc.ordering.domain.valueobject.TrackingID;
import ru.avalc.ordering.service.application.rest.OrderController;
import ru.avalc.ordering.service.domain.ports.input.service.OrderApplicationService;
import ru.avalc.ordering.system.domain.valueobject.OrderStatus;
import ru.avalc.ordering.tests.OrderingTest;

import javax.validation.ValidationException;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Alexei Valchuk, 11.09.2023, email: a.valchukav@gmail.com
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerUnitTest extends OrderingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateOrderResponse createOrderResponse;
    private TrackOrderResponse trackOrderResponse;
    private OrderDomainException orderDomainException;
    private OrderNotFoundException orderNotFoundException;
    private final TrackingID trackingID = new TrackingID(UUID.randomUUID());

    @BeforeAll
    public void init() {
        createOrderResponse = CreateOrderResponse.builder()
                .orderTrackingID(trackingID.getValue())
                .orderStatus(OrderStatus.PENDING)
                .message("Created")
                .build();

        trackOrderResponse = TrackOrderResponse.builder()
                .orderTrackingID(trackingID.getValue())
                .orderStatus(OrderStatus.PAID)
                .build();

        orderDomainException = new OrderDomainException("OrderDomainException message");
        orderNotFoundException = new OrderNotFoundException("OrderNotFoundException message");
    }

    @Test
    public void testCreateOrder() throws Exception {
        given(orderApplicationService.createOrder(any(CreateOrderCommand.class))).willReturn(createOrderResponse);

        ResultActions response = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderCommand)));

        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderTrackingID", is(createOrderResponse.getOrderTrackingID().toString())))
                .andExpect(jsonPath("$.orderStatus", is(createOrderResponse.getOrderStatus().toString())))
                .andExpect(jsonPath("$.message", is(createOrderResponse.getMessage())));
    }

    @Test
    public void testCreateOrderWhenOrderDomainExceptionIsThrown() throws Exception {
        given(orderApplicationService.createOrder(any(CreateOrderCommand.class))).willThrow(orderDomainException);

        ResultActions response = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderCommand)));

        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(orderDomainException.getMessage())));
    }

    @Test
    public void testCreateOrderWhenOrderNotFoundExceptionIsThrown() throws Exception {
        given(orderApplicationService.createOrder(any(CreateOrderCommand.class))).willThrow(orderNotFoundException);

        ResultActions response = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderCommand)));

        response
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(orderNotFoundException.getMessage())));
    }

    @Test
    public void testCreateOrderWhenValidationExceptionIsThrown() throws Exception {
        given(orderApplicationService.createOrder(any(CreateOrderCommand.class))).willThrow(new ValidationException());

        ResultActions response = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderCommand)));

        response
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateOrderWhenExceptionIsThrown() throws Exception {
        given(orderApplicationService.createOrder(any(CreateOrderCommand.class))).willThrow(new RuntimeException());

        ResultActions response = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createOrderCommand)));

        response
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testTrackOrder() throws Exception {
        given(orderApplicationService.trackOrder(any(TrackOrderQuery.class))).willReturn(trackOrderResponse);

        ResultActions response = mockMvc.perform(get("/orders/{id}", trackingID.getValue().toString()));

        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderTrackingID", is(trackOrderResponse.getOrderTrackingID().toString())))
                .andExpect(jsonPath("$.orderStatus", is(trackOrderResponse.getOrderStatus().toString())));
    }

    @Test
    public void testTrackOrderWhenOrderDomainExceptionIsThrown() throws Exception {
        given(orderApplicationService.trackOrder(any(TrackOrderQuery.class))).willThrow(orderDomainException);

        ResultActions response = mockMvc.perform(get("/orders/{id}", trackingID.getValue().toString()));

        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(orderDomainException.getMessage())));
    }

    @Test
    public void testTrackOrderWhenOrderNotFoundExceptionIsThrown() throws Exception {
        given(orderApplicationService.trackOrder(any(TrackOrderQuery.class))).willThrow(orderNotFoundException);

        ResultActions response = mockMvc.perform(get("/orders/{id}", trackingID.getValue().toString()));

        response
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(orderNotFoundException.getMessage())));
    }

    @Test
    public void testTrackOrderWhenExceptionIsThrown() throws Exception {
        given(orderApplicationService.trackOrder(any(TrackOrderQuery.class))).willThrow(new RuntimeException());

        ResultActions response = mockMvc.perform(get("/orders/{id}", trackingID.getValue().toString()));

        response
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testTrackOrderWhenValidationExceptionIsThrown() throws Exception {
        given(orderApplicationService.trackOrder(any(TrackOrderQuery.class))).willThrow(new ValidationException());

        ResultActions response = mockMvc.perform(get("/orders/{id}", trackingID.getValue().toString()));

        response
                .andExpect(status().isBadRequest());
    }
}
