package com.recover.deleted.messages.chat.recovery.utils

import java.io.File
import java.util.Comparator
import kotlin.toString

class fileComparator : Comparator<File> {
    override fun compare(o1: File?, o2: File?): Int {
        return o2?.lastModified().toString().compareTo(o1?.lastModified().toString())
    }
}