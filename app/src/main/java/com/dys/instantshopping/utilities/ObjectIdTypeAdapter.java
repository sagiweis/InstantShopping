package com.dys.instantshopping.utilities;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * Created by Sagi on 11/06/2016.
*/
public class ObjectIdTypeAdapter extends TypeAdapter<ObjectId> {
    @Override
    public void write(final JsonWriter out, final ObjectId value) throws IOException {
        /*out.beginObject()
                .name("$oid")
                .value(value.toString())
                .endObject();*/
        out.value(value.toString());
    }

    @Override
    public ObjectId read(final JsonReader in) throws IOException {
        /*in.beginObject();
        assert "$oid".equals(in.nextName());
        String objectId = in.nextString();
        in.endObject();
        return new ObjectId(objectId);*/
        return new ObjectId(in.nextString());
    }
}