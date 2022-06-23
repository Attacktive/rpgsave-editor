package com.github.attacktive

import com.github.attacktive.file.FileUtils
import com.github.diogoduailibe.lzstring4j.LZString
import java.nio.charset.StandardCharsets

fun main(vararg args: String) {
	val defaultFileName = "file2.rpgsave"
	val argument = args.getOrNull(0)
	val fileName = if (argument.isNullOrBlank()) { defaultFileName } else { argument }

	val bytes = FileUtils.readFileFromResources("/${fileName}")
	val base64 = String(bytes, StandardCharsets.UTF_8)
	val decompressed = LZString.decompressFromBase64(base64)
	println(decompressed)
}
