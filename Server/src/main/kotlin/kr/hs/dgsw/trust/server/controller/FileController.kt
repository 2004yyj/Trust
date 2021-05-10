package kr.hs.dgsw.trust.server.controller

import kr.hs.dgsw.trust.server.data.response.JsonResponse
import kr.hs.dgsw.trust.server.service.FileService
import org.apache.coyote.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths
import javax.servlet.http.HttpServletResponse

@RestController
class FileController() {

    @Autowired
    private lateinit var fileService: FileService

    @GetMapping("/image/{fileName}")
    fun image(@PathVariable fileName: String, response: HttpServletResponse) : ResponseEntity<Resource> {
        lateinit var resource : Resource
        val header = HttpHeaders()
        try {
            resource = fileService.loadFile(fileName)
            val path = Paths.get(resource.file.toURI())
            header.add("Content-Type", Files.probeContentType(path))
        } catch (e : FileNotFoundException) {
            throw FileNotFoundException(e.message)
        }
        return ResponseEntity<Resource>(resource, header, HttpStatus.OK)
    }

    @ExceptionHandler(value = [FileNotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handler(error: FileNotFoundException): HashMap<String, Any?> {
        return JsonResponse().returnResponse("404", error.message.toString(), null)
    }

}