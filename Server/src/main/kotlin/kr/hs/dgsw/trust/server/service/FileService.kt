package kr.hs.dgsw.trust.server.service

import javassist.NotFoundException
import kr.hs.dgsw.trust.server.configuration.FileUploadProperties
import kr.hs.dgsw.trust.server.repository.CommentRepository
import kr.hs.dgsw.trust.server.repository.PostRepository
import org.springframework.boot.configurationprocessor.json.JSONArray
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
class FileService(
    fileUploadProperties: FileUploadProperties,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) {

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

    fun updatePostFile(postId: Int, deleteFileList: Array<String>?, updateFileList: ArrayList<MultipartFile>?): ArrayList<String> {
        try {
            val post = postRepository.findById(postId).orElseThrow()
            val pathList = ArrayList<String>()
            val imageJsonArray = JSONArray(post.imageList)
            var i = 0
            while (i < imageJsonArray.length()) {
                pathList.add(imageJsonArray[i] as String)
                i++
            }
            deleteFileList?.forEach {
                val replaced = it.replace("\"", "")

                if (isFileExist(replaced)) {
                    deleteFileByName(replaced)
                    pathList.remove(replaced)
                }
            }
            updateFileList?.forEach {
                if (!it.originalFilename.isNullOrEmpty()) {
                    val fileName = saveFile(it)
                    pathList.add(fileName)
                }
            }
            return pathList
        } catch (e: Exception) {
            e.printStackTrace()
            throw NotFoundException("글을 찾을 수 없습니다.")
        }
    }

    fun updateCommentFile(commentId: Int, deleteFileList: Array<String>?, updateFileList: ArrayList<MultipartFile>?): ArrayList<String> {
        try {
            val comment = commentRepository.findById(commentId).orElseThrow()
            val pathList = ArrayList<String>()
            val imageJsonArray = JSONArray(comment.imageList)
            var i = 0
            while (i < imageJsonArray.length()) {
                pathList.add(imageJsonArray[i] as String)
                i++
            }
            deleteFileList?.forEach {
                if (isFileExist(it)) {
                    deleteFileByName(it)
                    pathList.remove(it)
                }
            }
            updateFileList?.forEach {
                if (!it.originalFilename.isNullOrEmpty()) {
                    val fileName = saveFile(it)
                    pathList.add(fileName)
                }
            }
            return pathList
        } catch (e: Exception) {
            throw NotFoundException("글을 찾을 수 없습니다.")
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