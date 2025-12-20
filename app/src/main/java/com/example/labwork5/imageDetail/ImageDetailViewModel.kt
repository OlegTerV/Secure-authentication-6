package com.example.labwork5.imageDetail

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateMapOf
import androidx.exifinterface.media.ExifInterface

class ImageDetailViewModel {
    companion object {
        fun getBitmapFormUri(uriImage: Uri, context: Context): Bitmap {
            return MediaStore.Images.Media.getBitmap(context.contentResolver, uriImage)
        }

        fun getExifData(uriImage: Uri, context: Context): MutableMap<String, String> {
            val metadata =
                mutableStateMapOf("Date" to "", "Lat" to "", "Lon" to "", "Device" to "", "Model" to "")

            val resolver = context.contentResolver
            try {
                resolver.openInputStream(uriImage).use { stream ->
                    //скатал со stackOverflow
                    val exifInterface = ExifInterface(stream!!)
                    metadata["Date"] =
                        exifInterface.getAttribute(ExifInterface.TAG_DATETIME) ?: ""
                    //metadata["Lat"] = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE) ?: ""
                    //metadata["Lon"] = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) ?: ""
                    metadata["Lat"] = exifInterface.getLatLong()?.get(0).toString()
                    metadata["Lon"] = exifInterface.getLatLong()?.get(1).toString()
                    metadata["Device"] =
                        exifInterface.getAttribute(ExifInterface.TAG_COPYRIGHT) ?: ""
                    metadata["Model"] = exifInterface.getAttribute(ExifInterface.TAG_MODEL) ?: ""
                }
            } catch (e: Exception) {
                println("Ошибка чтения EXIF тегов: $e")
            }
            return metadata
        }
    }
}