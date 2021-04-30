package com.example.sipmobileapp.retrofit;

import com.example.sipmobileapp.model.AttachResult;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class AttachResultDeserializer implements JsonDeserializer<AttachResult> {
    @Override
    public AttachResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject bodyObject = json.getAsJsonObject();
        Gson gson = new Gson();
        AttachResult attachResult = gson.fromJson(bodyObject.toString(), AttachResult.class);

        return attachResult;
    }
}
