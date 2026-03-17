package com.kodlabs.doktorumyanimda.model.ckys.calisma;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalismaSonuc implements Serializable {
    @SerializedName("HataKodu")
    private int hataKodu;
    @SerializedName("HataMesaj")
    private String hataMesaji;
    @SerializedName("KurumKodu")
    private int kurumKodu;
    @SerializedName("KurumAdi")
    private String kurumAdi;
    @SerializedName("KurumTuru")
    private String kurumTuru;
    @SerializedName("KurumTipi")
    private String kurumTipi;
}
