package com.swdgr6.bikeplatform.model.payload.responeModel;

import com.swdgr6.bikeplatform.model.payload.dto.OilProductPackageDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OilProductPackageResponse {
    private List<OilProductPackageDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
