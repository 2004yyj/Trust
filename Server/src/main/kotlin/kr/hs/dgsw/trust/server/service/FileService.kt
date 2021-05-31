package kr.hs.dgsw.trust.server.service

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.configuration.FileUploadProperties
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*
import javax.annotation.PostConstruct

@Service
class FileService(fileUploadProperties: FileUploadProperties) {

    private var location = Paths.get(fileUploadProperties.location)
        .toAbsolutePath().normalize()

    @PostConstruct
    fun init() {
        try {
            Files.createDirectories(location)
        } catch (e : IOException) {
            e.printStackTrace()
        }
    }

    fun deleteFileByName(fileName: String) : String {
        val location = location.resolve(fileName).normalize()
        try {
            Files.delete(location)
        } catch (e : IOException) {
            throw FileNotFoundException("파일을 찾을 수 없습니다.")
        }
        return fileName
    }

    fun saveFile(multipartFile: MultipartFile) : String {
        val uuid = UUID.randomUUID().toString()
        val fileName = "${uuid}_${multipartFile.originalFilename}"
        val location = location.resolve(fileName).normalize()
        try {
            Files.copy(multipartFile.inputStream, location, StandardCopyOption.REPLACE_EXISTING)
        } catch (e : IOException) {
            e.printStackTrace()
        }

        return fileName
    }

    fun loadFile(fileName: String): UrlResource {
        try {
            val file = location.resolve(fileName).normalize()
            val resource = UrlResource(file.toUri())

            return if (resource.exists() || resource.isReadable) {
                resource
            } else {
                throw FileNotFoundException("파일을 찾을 수 없습니다.")
            }
        } catch (e : MalformedURLException) {
            throw FileNotFoundException("파일을 찾을 수 없습니다.")
        }
    }

    fun isFileExist(fileName: String): Boolean {
        val file = location.resolve(fileName).normalize()
        val resource = UrlResource(file.toUri())
        return resource.exists() || resource.isReadable
    }
}