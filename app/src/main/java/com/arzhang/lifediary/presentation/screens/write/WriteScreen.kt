package com.arzhang.lifediary.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.arzhang.lifediary.model.Diary

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    selectedDiary: Diary?,
    onBackPressed: () -> Unit,
    onDeleteConfirmed: () -> Unit,
    pagerState: PagerState
) {
    Scaffold(
        topBar = {
                 WriteTopBar(selectedDiary = selectedDiary,onBackPressed = onBackPressed, onDeleteConfirmed = onDeleteConfirmed )
        },
        content = {
            WriteContent(
                paddingValues = it,
                pagerState = pagerState,
                title = "",
                onTitleChanged = {},
                description = "",
                onDescriptionChanged = {}
            )
        }
    )
}
