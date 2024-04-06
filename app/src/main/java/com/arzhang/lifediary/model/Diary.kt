package com.arzhang.lifediary.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

// Realm DB model for diary item
class Diary : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    // id of each authenticated user to load their related diaries
    var ownerId: String = ""
    var title: String = ""
    var description: String = ""
    var images: RealmList<String> = realmListOf()
    // we don't need to specify date explicitly.
    var date: RealmInstant = RealmInstant.from(System.currentTimeMillis(),0)
}