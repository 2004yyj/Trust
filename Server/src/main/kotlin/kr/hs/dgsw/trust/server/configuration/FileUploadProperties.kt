package kr.hs.dgsw.trust.server.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "file.upload")
class FileUploadProperties {
    lateinit var location : String
}