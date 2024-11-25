package com.recover.deleted.messages.chat.recovery.model

import android.os.Parcel
import android.os.Parcelable

class StatusModel() : Parcelable {
    var id: Int? = null                       // Unique ID for each status (optional)
    var filepath: String? = null              // Path or URI of the status
    var type: String? = null                  // Type of status: "image" or "video"
    var selected: Boolean = false             // Whether the status is selected

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        filepath = parcel.readString()
        type = parcel.readString()
        selected = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(filepath)
        parcel.writeString(type)
        parcel.writeByte(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StatusModel> {
        override fun createFromParcel(parcel: Parcel): StatusModel {
            return StatusModel(parcel)
        }

        override fun newArray(size: Int): Array<StatusModel?> {
            return arrayOfNulls(size)
        }
    }
}