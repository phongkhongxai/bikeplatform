package com.swdgr6.bikeplatform.model.payload.requestModel;

import lombok.Data;

import java.util.Set;

@Data
public class AnyIdsRequest {
    private Set<Long> anyTypeIds;
}
