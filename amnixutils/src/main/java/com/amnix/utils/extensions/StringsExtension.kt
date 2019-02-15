package com.amnix.utils.extensions

import android.util.Base64
import java.io.File
import java.io.FileOutputStream
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.security.MessageDigest

inline fun String.append(other: String) = this + other

fun String.isPhone(): Boolean {
    val p = "^1([34578])\\d{9}\$".toRegex()
    return matches(p)
}

fun String.isEmail(): Boolean {
    val p = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(p)
}

fun String.isNumeric(): Boolean {
    val p = "^[0-9]+$".toRegex()
    return matches(p)
}

fun String.md5() = encrypt(this, "MD5")

fun String.sha1() = encrypt(this, "SHA-1")

fun String.isIdcard(): Boolean {
    val p18 =
        "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]\$".toRegex()
    val p15 = "^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]\$".toRegex()
    return matches(p18) || matches(p15)
}

fun String.toFile() = File(this)

fun String.encodeToUrl(charSet: String = "UTF-8"): String = URLEncoder.encode(this, charSet)

fun String.decodeToUrl(charSet: String = "UTF-8"): String = URLDecoder.decode(this, charSet)

fun String.encodeToBase64(charSet: String = "UTF-8"): String = Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)

fun String.decodeToBase64(charSet: String = "UTF-8"): String =
    String(Base64.decode(this.toByteArray(), Base64.NO_WRAP), Charset.defaultCharset())

fun String.encodeToBinary(): String {
    val stringBuilder = StringBuilder()
    toCharArray().forEach {
        stringBuilder.append(Integer.toBinaryString(it.toInt()))
        stringBuilder.append(" ")
    }
    return stringBuilder.toString()
}

fun String.deCodeToBinary(): String {
    val stringBuilder = StringBuilder()
    split(" ").forEach {
        stringBuilder.append(Integer.parseInt(it.replace(" ", ""), 2))
    }
    return stringBuilder.toString()
}

fun String.saveToFile(file: File) = FileOutputStream(file).bufferedWriter().use {
    it.write(this)
    it.flush()
    it.close()
}

private fun encrypt(string: String?, type: String): String {
    val bytes = MessageDigest.getInstance(type).digest(string!!.toByteArray())
    return bytes2Hex(bytes)
}

internal fun bytes2Hex(bts: ByteArray): String {
    var des = ""
    var tmp: String
    for (i in bts.indices) {
        tmp = Integer.toHexString(bts[i].toInt() and 0xFF)
        if (tmp.length == 1) {
            des += "0"
        }
        des += tmp
    }
    return des
}