package com.example.labwork5.addRecord

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface
import com.example.labwork5.imageDetail.ImageDetailViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


class EditImageDetailViewModel {

    fun validateData(key: String, data: String) {
        when (key) {
            "Date" -> TODO()
            "Lat" -> TODO()
            "Lon" -> TODO()
            "Device" -> TODO()
            "Model" -> TODO()
        }
    }

    fun saveNewData(newData: MutableMap<String, String>, context: Context, uriImage: Uri, saveChangesAndGoBack: (String) -> Unit) {
        val resolver = context.contentResolver

        //скатал у DeepSeek
        val tempFile = File.createTempFile("image_", ".jpg", context.cacheDir)

        //скатал у DeepSeek/Gemini
        resolver.openInputStream(uriImage)?.use { inputStream ->

            // 3. Декодируем исходный поток в растровое изображение (Bitmap)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            FileOutputStream(tempFile).use { outputStream ->
                //inputStream.copyTo(outputStream)
                originalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // Сжатие в JPEG с качеством 90%
            }
            originalBitmap.recycle()
        }

        try {
            val exifInterface = ExifInterface(tempFile.absolutePath)//скатал у DeepSeek
            exifInterface.setAttribute(ExifInterface.TAG_DATETIME, newData["Date"])
            //exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, newData["Lat"])
            //exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, newData["Lon"])
            exifInterface.setAttribute(ExifInterface.TAG_COPYRIGHT, newData["Device"])
            exifInterface.setAttribute(ExifInterface.TAG_MODEL, newData["Model"])
            exifInterface.setLatLong(newData["Lat"]!!.toDouble(), newData["Lon"]!!.toDouble())
            exifInterface.saveAttributes()
        } catch (e: Exception) {
            println("Ошибка сохранения EXIF тегов: $e")
        }
        val newImage: Bitmap = ImageDetailViewModel.getBitmapFormUri(tempFile.toUri(), context)
        val newImageUri = saveImage(context, newImage)

        resolver.openInputStream(tempFile.toUri())?.use{ inputStream ->
            resolver.openOutputStream(newImageUri!!).use{ outputStream ->
                inputStream.copyTo(outputStream!!)
            }
        }

        val encodedPhotoUri = URLEncoder.encode(newImageUri.toString(), StandardCharsets.UTF_8.toString())
        saveChangesAndGoBack(encodedPhotoUri)
        /*
        try {
            resolver.openInputStream(uriImage).use { stream ->
                //скатал со stackOverflow
                val exifInterface = ExifInterface(stream)

                exifInterface.setAttribute(ExifInterface.TAG_DATETIME, newData["Date"])
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, newData["Lat"])
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, newData["Lon"])
                exifInterface.setAttribute(ExifInterface.TAG_COPYRIGHT, newData["Device"])
                exifInterface.setAttribute(ExifInterface.TAG_MODEL, newData["Model"])
                exifInterface.saveAttributes()
            }
            saveChangesAndGoBack(uriImage.toString())
        } catch (e: Exception) {
            println("Ошибка сохранения EXIF тегов: $e")
        }*/
    }

    //метод скатал со stackOverflow
    fun saveImage(context: Context, imageBitmap: Bitmap): Uri? {
        val values = ContentValues().apply{
            put(MediaStore.MediaColumns.DISPLAY_NAME, "new image.jpeg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        }

        var newUri: Uri? = null
        try {
            newUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            context.contentResolver.openOutputStream(newUri!!).use { stream ->
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream!!)
            }
            return newUri
        } catch (e: IOException) {
            newUri?.let { orphanUri ->
                context.contentResolver.delete(orphanUri, null, null)
            }
            println("Ошибка сохранения изображения: $e")
        }

        return newUri
    }

}