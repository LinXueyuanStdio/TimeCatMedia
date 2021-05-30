package com.xyoye.stream_component.utils.server

import com.xyoye.common_component.utils.IOUtils
import com.xyoye.common_component.utils.getFileExtension
import fi.iki.elonen.NanoHTTPD
import java.io.InputStream
import java.net.URLEncoder
import kotlin.random.Random

class FTPPlayServer private constructor() : NanoHTTPD(randomPort()) {

    private var sourceInputStream: InputStream? = null
    private var sourceContentType: String? = null
    private var sourceLength: Long = 0

    private object Holder {
        val instance = FTPPlayServer()
    }

    companion object {
        //随机端口
        private fun randomPort() = Random.nextInt(30000, 40000)

        @JvmStatic
        fun getInstance() = Holder.instance
    }

    override fun serve(session: IHTTPSession?): Response {
        //请求可解析，资源存在
        if (session != null && sourceInputStream != null) {
            return newChunkedResponse(
                Response.Status.OK,
                sourceContentType,
                sourceInputStream!!
            )
        }
        return super.serve(session)
    }

    fun getInputStreamUrl(name: String): String {
        val encodeFileName = URLEncoder.encode(name, "utf-8")
        return  "http://127.0.0.1:$listeningPort/$encodeFileName"
    }

    fun setPlaySource(
        name: String,
        length: Long,
        inputStream: InputStream
    ) {
        if (sourceInputStream != null) {
            IOUtils.closeIO(sourceInputStream)
            sourceInputStream = null
        }

        sourceContentType = getContentType(name)
        sourceLength = length
        sourceInputStream = inputStream
    }

    fun closeIO() {
        if (sourceInputStream != null) {
            IOUtils.closeIO(sourceInputStream)
            sourceInputStream = null
        }
    }

    private fun getContentType(sourceName: String): String {
        if (sourceName.isEmpty()) {
            return "video/*"
        }
        val extension = getFileExtension(sourceName)
        return "video/$extension"
    }
}