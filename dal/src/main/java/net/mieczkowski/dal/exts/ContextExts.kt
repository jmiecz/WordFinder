package net.mieczkowski.dal.exts

import android.content.Context
import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by Josh Mieczkowski on 4/19/2019.
 */
fun Context.readFromRaw(@RawRes rawID: Int): String =
    resources.openRawResource(rawID).use { inputStream ->
        BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
            reader.readText()
        }
    }