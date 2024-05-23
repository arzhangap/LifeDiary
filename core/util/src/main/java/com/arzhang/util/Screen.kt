package com.arzhang.util

import com.arzhang.util.Constants.WRITE_SCREEN_ARGUMENT_KEY

sealed class Screen(val route: String) {
    object Authentication : Screen(route = "authentication_screen")
    object Home : Screen(route = "home_screen")
    object Write :
        Screen(route = "write_screen?$WRITE_SCREEN_ARGUMENT_KEY={$WRITE_SCREEN_ARGUMENT_KEY}")

    fun passDiaryId(diaryId: String) =
        "write_screen?diaryId=$diaryId"
    // diaryId represent the key. the string inside {} represent value
}