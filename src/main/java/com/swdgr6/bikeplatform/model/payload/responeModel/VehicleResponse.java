package com.swdgr6.bikeplatform.model.payload.responeModel;

import com.swdgr6.bikeplatform.model.payload.dto.VehicleDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VehicleResponse {
    private List<VehicleDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
