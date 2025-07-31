package com.kodlabs.doktorumyanimda.model.ckys.tahsil;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Type;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TahsilBilgisiResult implements Serializable {
    @SerializedName("TahsilSonuc")
    @JsonAdapter(TahsilSonucArrayDeserializer.class)
    private TahsilSonuc[] list;

    public class TahsilSonucArrayDeserializer implements JsonDeserializer<TahsilSonuc[]>{

        @Override
        public TahsilSonuc[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if(jsonElement.isJsonNull()){
                return null;
            }
            if(jsonElement.isJsonArray()){
                return jsonDeserializationContext.deserialize(jsonElement, TahsilSonuc[].class);
            }
            TahsilSonuc[] array = new TahsilSonuc[1];
            array[0] = new Gson().fromJson(jsonElement, TahsilSonuc.class);
            return array;
        }
    }
}
