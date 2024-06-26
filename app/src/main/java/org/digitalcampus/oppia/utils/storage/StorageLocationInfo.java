package org.digitalcampus.oppia.utils.storage;

import android.content.Context;

import org.digitalcampus.mobile.learning.R;
import org.digitalcampus.oppia.activity.PrefsActivity;
import org.digitalcampus.oppia.utils.TextUtilsJava;

public class StorageLocationInfo {

    public final String type;
    public final String path;
    public final boolean readonly;
    public final boolean removable;
    public final int number;

    StorageLocationInfo(String type, String path, boolean readonly, boolean removable, int number) {
        this.type = type;
        this.path = path;
        this.readonly = readonly;
        this.removable = removable;
        this.number = number;
    }

    public String getDisplayName(Context ctx) {
        return ctx.getString(TextUtilsJava.equals(type, PrefsActivity.STORAGE_OPTION_INTERNAL)
                ? R.string.prefStorageOptionInternal : R.string.prefStorageOptionExternal);
    }
}