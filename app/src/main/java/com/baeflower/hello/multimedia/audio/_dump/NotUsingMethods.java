package com.baeflower.hello.multimedia.audio._dump;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sol on 2015-05-13.
 */
public class NotUsingMethods {


    class Mp3Filter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3"));
        }
    }

    public List<String> getArtists(Context context) {
        List<String> list = new ArrayList<>();
        String[] cursorColumns = new String[] {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST
        };
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists. EXTERNAL_CONTENT_URI, cursorColumns, null, null, null);

        if (cursor == null) {
            return list;
        }

        if (cursor.moveToFirst()) {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            do
            {
                String artist = cursor.getString(artistColumn);
                list.add(artist);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
