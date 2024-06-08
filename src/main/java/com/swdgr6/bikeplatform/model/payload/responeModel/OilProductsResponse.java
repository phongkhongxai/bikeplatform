package com.swdgr6.bikeplatform.model.payload.responeModel;


import com.swdgr6.bikeplatform.model.payload.dto.BrandDto;
import com.swdgr6.bikeplatform.model.payload.dto.OilProductDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OilProductsResponse {
    private List<OilProductDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
