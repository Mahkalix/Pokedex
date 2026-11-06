package com.example.collectioncard.model;

import java.text.Normalizer;
import java.util.Locale;

public class Pokemon {
    private String name;
    private final String url;

    public Pokemon(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        if (name == null || name.isEmpty()) return "";
        return name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    }

    public void setName(String name) { this.name = name; }
    public String getUrl() { return url; }

    public int getNumber() {
        if (url == null) return -1;
        String path = url.replaceAll("/+$", "");
        try { return Integer.parseInt(path.substring(path.lastIndexOf('/') + 1)); }
        catch (NumberFormatException e) { return -1; }
    }

    public boolean matches(String query) {
        String normalized = normalize(query);
        if (normalized.isEmpty()) return true;
        if (normalize(getName()).contains(normalized)) return true;
        String number = normalized.startsWith("#") ? normalized.substring(1) : normalized;
        if (!number.matches("[0-9]+")) return false;
        try { return Integer.parseInt(number) == getNumber(); }
        catch (NumberFormatException e) { return false; }
    }

    private static String normalize(String value) {
        return Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }
}
