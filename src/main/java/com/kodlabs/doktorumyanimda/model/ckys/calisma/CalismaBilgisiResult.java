package com.kodlabs.doktorumyanimda.model.ckys.calisma;

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
public class CalismaBilgisiResult implements Serializable {
    @SerializedName("CalismaSonuc")
    @JsonAdapter(CalismaSonucArrayDeserializer.class)
    private CalismaSonuc[] list;

    public class CalismaSonucArrayDeserializer implements JsonDeserializer<CalismaSonuc[]>{

        @Override
        public CalismaSonuc[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if(jsonElement.isJsonNull()){
                return null;
            }
            if(jsonElement.isJsonArray()){
                return jsonDeserializationContext.deserialize(jsonElement, CalismaSonuc[].class);
            }
            CalismaSonuc[] array = new CalismaSonuc[1];
            array[0] = new Gson().fromJson(jsonElement, CalismaSonuc.class);
            return array;
        }
    }
}
