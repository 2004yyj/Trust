package kr.hs.dgsw.trust.function

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

/**
 * Uri를 이용해서
 * contentResolver 쿼리를 실행하는 익스텐션 함수
 */

fun Uri.asMultipart(contentResolver: ContentResolver): MultipartBody.Part? {
    return contentResolver.query(this, null, null, null, null)?.let {
        if (it.moveToNext()) {
            val fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            val requestBody = object : RequestBody() {
                override fun contentType(): MediaType? {
                    return contentResolver.getType(this@asMultipart)?.toMediaType()
                }
                override fun writeTo(sink: BufferedSink) {
                    sink.writeAll(contentResolver.openInputStream(this@asMultipart)?.source()!!)
                }
            }
            it.close()
            MultipartBody.Part.createFormData("profileImage", fileName, requestBody)
        } else {
            it.close()
            null
        }
    }
}