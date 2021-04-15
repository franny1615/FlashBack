package com.example.flashback.Converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
// Source for GSON : https://github.com/google/gson/blob/master/UserGuide.md/
// Guide for Converter : https://mobikul.com/insert-custom-list-and-get-that-list-in-room-database-using-typeconverter/

public class DeckCardIDListConverter implements Serializable {

    @TypeConverter
    public String fromListToStringOfCardIDs(List<Long> cardIDs)
    {
        String stringOfCardIDs = "";

        if (cardIDs != null && !cardIDs.isEmpty()){
            Gson gson = new Gson();
            Type type = new TypeToken<List<Long>>()
                {}.getType();
            stringOfCardIDs = gson.toJson(cardIDs, type);
        }

        return stringOfCardIDs;
    }

    @TypeConverter
    public List<Long> fromStringToListOfCardIDs(String cardIDsString)
    {
        List<Long> cardIDsList = new ArrayList<>();

        if(cardIDsString != null && !cardIDsString.isEmpty())
        {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Long>>()
                {}.getType();
            cardIDsList = gson.fromJson(cardIDsString, type);

        }

        return cardIDsList;
    }
}
