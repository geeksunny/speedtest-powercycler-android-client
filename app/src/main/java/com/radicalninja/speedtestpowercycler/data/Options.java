package com.radicalninja.speedtestpowercycler.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Options extends JsonResponse<Options> {

    private Map<String, List<Item>> items;

    public Options(JSONObject raw) {
        super(raw);
    }

    public Map<String, List<Item>> getItems() {
        return items;
    }

    @Override
    protected void parse(JSONObject json) {
        items = new HashMap<>(json.length());
        for (final Iterator<String> keyIter = json.keys(); keyIter.hasNext(); ) {
            final String key = keyIter.next();
            final JSONArray itemsArray = json.optJSONArray(key);
            if (null != itemsArray) {
                final List<Item> newItems = new ArrayList<>(itemsArray.length());
                for (int i = 0; i < itemsArray.length(); i++) {
                    try {
                        final JSONObject itemJson = itemsArray.getJSONObject(i);
                        final Item item = parseItem(itemJson);
                        newItems.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                items.put(key, newItems);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected Item parseItem(final JSONObject json) {
        final String type = json.optString("type");
        switch (type.toLowerCase()) {
            case "boolean":
                return new Item(json, Boolean.class);
            case "number":
                // TODO: Add support for Double/Long values
                return new Item(json, Integer.class);
            case "string":
                return new Item(json, String.class);
            default:
                return null;
        }
    }

    public static class Item<T> extends JsonResponse<Item<T>> {
        private final Class<T> type;

        private String key, typeString;
        private String name, description;
        private T defaultValue;

        protected Item(JSONObject raw, Class<T> type) {
            super(raw);
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public Class<T> getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public T getDefaultValue() {
            return defaultValue;
        }

        @Override
        protected void parse(JSONObject json) {
            typeString = json.optString("type");
            key = json.optString("key");
            name = json.optString("name");
            description = json.optString("description");
            try {
                //noinspection unchecked
                defaultValue = (T) json.opt("default");
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

}
