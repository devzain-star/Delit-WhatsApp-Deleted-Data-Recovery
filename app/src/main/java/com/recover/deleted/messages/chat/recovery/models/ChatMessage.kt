package com.recover.deleted.messages.chat.recovery.models

import android.graphics.drawable.Icon
import android.os.Parcel
import android.os.Parcelable

data class ChatMessage(
    var ticker: String? = null,
    var name: String? = null,
    var text: String? = null,
    var date: String? = null,
    var time: Long = 0,
    var id: Int = 0,
    var count: Int = 0,
    var icon: Icon? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        ticker = parcel.readString(),
        name = parcel.readString(),
        text = parcel.readString(),
        date = parcel.readString(),
        time = parcel.readLong(),
        id = parcel.readInt(),
        count = parcel.readInt(),
        icon = parcel.readParcelable(Icon::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ticker)
        parcel.writeString(name)
        parcel.writeString(text)
        parcel.writeString(date)
        parcel.writeLong(time)
        parcel.writeInt(id)
        parcel.writeInt(count)
        parcel.writeParcelable(icon, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatMessage> {
        override fun createFromParcel(parcel: Parcel): ChatMessage {
            return ChatMessage(parcel)
        }

        override fun newArray(size: Int): Array<ChatMessage?> {
            return arrayOfNulls(size)
        }
    }
}