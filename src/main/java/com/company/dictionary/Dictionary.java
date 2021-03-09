package com.company.dictionary;

import java.util.HashMap;
import java.util.Map;

public class Dictionary {
    public volatile Map<String, String> words = new HashMap<>();
    private final String name;
    private final String path;

    public Dictionary(String langFrom, String langTo) {
        this.name = langFrom + "_to_" + langTo;
        this.path = Config.BASE_PATH + "dictionaries\\" +
                langFrom + "_to_" + langTo + ".txt";
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
