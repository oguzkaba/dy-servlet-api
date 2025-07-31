package com.kodlabs.doktorumyanimda.model.ckys.tahsil;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CkysTahilBilgisiResponse implements Serializable {
    @SerializedName("GetTahsilBilgisiResult")
    private TahsilBilgisiResult tahsilBilgisiResult;
}