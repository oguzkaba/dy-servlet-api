package com.kodlabs.doktorumyanimda.model.peak;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DemandVerify {
    private int status;
    private int fee;
    private int peakDay;

    public DemandVerify(int status) {
        this.status = status;
    }
}
