package com.swdgr6.bikeplatform.model.payload.responeModel;


import com.swdgr6.bikeplatform.model.payload.dto.BrandDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BrandsResponse {
    private List<BrandDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
