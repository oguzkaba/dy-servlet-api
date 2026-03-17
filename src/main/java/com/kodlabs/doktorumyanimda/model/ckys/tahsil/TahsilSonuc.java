package com.kodlabs.doktorumyanimda.model.ckys.tahsil;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TahsilSonuc implements Serializable {
    @SerializedName("HataKodu")
    private int hataKodu;
    @SerializedName("HataMesaj")
    private String hataMesaji;
    @SerializedName("IhtisasTescilNo")
    private String ihtisasTecilNo;
    @SerializedName("IhtisasTescilTarihi")
    private String ihtisasTescilTarihi;
    @SerializedName("DiplomaTescilNo")
    private String diplomaTescilNo;
    @SerializedName("Tckn")
    private String tckn;
    @SerializedName("Ad")
    private String ad;
    @SerializedName("Soyad")
    private String soyad;
    @SerializedName("DogumTarihi")
    private String dogumTarihi;
    @SerializedName("TahsilTuru")
    private String tahsilTuru;
    @SerializedName("BransKodu")
    private String bransKodu;
    @SerializedName("BransAdi")
    private String bransAdi;
    @SerializedName("YandalKodu")
    private String yandalKodu;
    @SerializedName("YandalAdi")
    private String yandalAdi;
}
