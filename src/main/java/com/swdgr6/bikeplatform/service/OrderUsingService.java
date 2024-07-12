package com.swdgr6.bikeplatform.service;



import com.swdgr6.bikeplatform.model.payload.dto.OrderUsingDto;
import com.swdgr6.bikeplatform.model.payload.requestModel.OrderUsingUpdatedRequest;
import com.swdgr6.bikeplatform.model.payload.responeModel.OrderUsingsResponse;

public interface OrderUsingService {
    OrderUsingDto createOrderUsing(OrderUsingDto orderUsingDto);
    OrderUsingDto updateOrderUsingFromBikePoint(Long id, double price);
    OrderUsingDto updateOrderUsingFromBPReject(Long id);
    OrderUsingDto updateOrderUsingFromUser(Long id);
    OrderUsingDto updateOrderUsingForUserRating(Long id, OrderUsingUpdatedRequest orderUsingUpdatedRequest);
    String deleteOrderUsing(Long id);
    OrderUsingDto getOrderUsingById(Long id);
    OrderUsingsResponse getAllOrderUsing(int pageNo, int pageSize, String sortBy, String sortDir);
    OrderUsingsResponse getOrderUsingsOfBikePoint(Long bikePointId, int pageNo, int pageSize, String sortBy, String sortDir);
    OrderUsingsResponse getOrderUsingsOfBikePointIsConfirm(Long bikePointId, int pageNo, int pageSize, String sortBy, String sortDir);

    OrderUsingsResponse getOrderUsingsOfOrder(Long orderId, int pageNo, int pageSize, String sortBy, String sortDir);
    OrderUsingsResponse getOrderUsingsByStatus(String status,int pageNo, int pageSize, String sortBy, String sortDir);
    OrderUsingsResponse getOrderUsingsByStatusWithBikePoint(Long bikePointId,String status, int pageNo, int pageSize, String sortBy, String sortDir);
    OrderUsingsResponse getOrderUsingsByStatusWithUser(Long uid,String status, int pageNo, int pageSize, String sortBy, String sortDir);

    OrderUsingsResponse getOrderUsingsByUser(Long uid, int pageNo, int pageSize, String sortBy, String sortDir);



}
