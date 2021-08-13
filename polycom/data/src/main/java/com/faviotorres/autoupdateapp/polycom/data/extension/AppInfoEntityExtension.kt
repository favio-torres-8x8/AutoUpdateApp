package com.faviotorres.autoupdateapp.polycom.data.extension

import com.faviotorres.autoupdateapp.polycom.api.model.AppInfoEntity
import com.faviotorres.autoupdateapp.polycom.domain.model.AppInfo

fun AppInfoEntity.toDomain(): AppInfo =
    AppInfo(versionCode, versionName, description)