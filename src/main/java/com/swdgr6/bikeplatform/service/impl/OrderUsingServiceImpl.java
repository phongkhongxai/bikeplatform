package com.swdgr6.bikeplatform.service.impl;

import com.swdgr6.bikeplatform.model.entity.OrderUsing;
import com.swdgr6.bikeplatform.model.payload.dto.OrderUsingDTO;
import com.swdgr6.bikeplatform.repository.OrderUsingRepository;
import com.swdgr6.bikeplatform.service.OrderUsingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderUsingServiceImpl implements OrderUsingService {
    @Autowired
    private OrderUsingRepository orderUsingRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public OrderUsingDTO updateOrderUsing(OrderUsingDTO orderUsingDTO) {
        OrderUsing orderUsing = orderUsingRepository.findById(orderUsingDTO.getId()).orElseThrow();
        orderUsing.setDateUsing(orderUsingDTO.getDateUsing());
        orderUsing.setDateUpdateUsing(orderUsingDTO.getDateUpdateUsing());
        orderUsing.setPrice(orderUsingDTO.getPrice());
        orderUsing.setRating(orderUsingDTO.getRating());
        orderUsing.setFeedback(orderUsingDTO.getFeedback());
        orderUsing.setStatus(orderUsingDTO.getStatus());
        OrderUsing orderUpdate = orderUsingRepository.save(orderUsing);
        return modelMapper.map(orderUpdate, OrderUsingDTO.class);
    }
}
