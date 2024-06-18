package com.swdgr6.bikeplatform.model.payload.responeModel;

import com.swdgr6.bikeplatform.model.payload.dto.OrderDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OrdersResponse {
    private List<OrderDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
