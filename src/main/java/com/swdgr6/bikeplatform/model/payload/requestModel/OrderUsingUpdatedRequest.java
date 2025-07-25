package com.swdgr6.bikeplatform.model.payload.requestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderUsingUpdatedRequest {
    private int rating;
    private String feedback;
}
