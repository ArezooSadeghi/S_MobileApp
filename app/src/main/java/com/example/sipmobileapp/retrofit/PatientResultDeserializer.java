package com.example.sipmobileapp.retrofit;

import com.example.sipmobileapp.model.PatientResult;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PatientResultDeserializer implements JsonDeserializer<PatientResult> {
    @Override
    public PatientResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject bodyObject = json.getAsJsonObject();
        Gson gson = new Gson();
        PatientResult patientResult = gson.fromJson(bodyObject.toString(), PatientResult.class);

        return patientResult;
    }
}
