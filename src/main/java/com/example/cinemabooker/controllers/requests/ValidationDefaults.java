package com.example.cinemabooker.controllers.requests;

public final class ValidationDefaults {
    private ValidationDefaults() {}

    public static final String ID_PATTERN = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}";
    public static final String NAME_PATTERN = "[A-ZŻŹĆŃŚŁ][a-zążźćńąśłęó]{2,}"; //żaneta, ścibor, etc
    public static final String SURNAME_PATTERN = "[A-ZZŻŹĆŃŚŁ][a-zążźćńąśłęó]{2,}+([-][A-ZZŻŹĆŃŚŁ][a-zążźćńąśłęó]+)?";
}
