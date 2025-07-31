package com.kodlabs.doktorumyanimda.model.ckys.calisma;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CkysCalismaBilgisiResponse implements Serializable {
    @SerializedName("GetCalismaBilgisiResult")
    private CalismaBilgisiResult calismaBilgisiResult;
}