package com.tobiasgoeschel.workshops.repows.lambda.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.tobiasgoeschel.workshops.repows.application.config.MoneyModule;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

public class Mapper {
    public static ObjectMapper create () {
        ObjectMapper objectmapper = new ObjectMapper();
        objectmapper.setVisibility(FIELD, ANY);
        objectmapper.registerModule(new ParameterNamesModule());
        objectmapper.registerModule(new MoneyModule());
        return objectmapper;
    }
}
