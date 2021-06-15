package kr.hs.dgsw.trust.function

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream


/**
 * Uri를 이용해서
 * contentResolver 쿼리를 실행하는 익스텐션 함수
 */

fun Uri.asMultipart(name: String, cacheDir: File, contentResolver: ContentResolver): MultipartBody.Part? {
    return contentResolver.query(this, null, null, null, null)?.let {
        if (it.moveToNext()) {
            val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver,this))

            val storage: File = cacheDir
            val tempFile = File(storage, displayName)
            tempFile.createNewFile()
            val out = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.close()
            it.close()

            val imageFile = File("${cacheDir}/${displayName}")
            val requestBody = imageFile.asRequestBody(contentResolver.getType(this)?.toMediaType())

            MultipartBody.Part.createFormData(name, displayName, requestBody)
        } else {
            it.close()
            null
        }
    }
}