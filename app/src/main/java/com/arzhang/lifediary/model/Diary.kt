package com.arzhang.lifediary.model

import com.arzhang.lifediary.util.toRealmInstant
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.Instant

// Realm DB model for diary item
open class Diary : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    // id of each authenticated user to load their related diaries
    var ownerId: String = ""
    // enum class is not supported so we use string
    var mood: String = Mood.Neutral.name
    var title: String = ""
    var description: String = ""
    var images: RealmList<String> = realmListOf()
    // we don't need to specify date explicitly.
    var date: RealmInstant = Instant.now().toRealmInstant()
}
