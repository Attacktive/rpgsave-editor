package com.github.attacktive.file

object FileUtils {
	fun readFileFromResources(fileName: String): ByteArray = FileUtils::class.java.getResource(fileName)!!.readBytes()
}
