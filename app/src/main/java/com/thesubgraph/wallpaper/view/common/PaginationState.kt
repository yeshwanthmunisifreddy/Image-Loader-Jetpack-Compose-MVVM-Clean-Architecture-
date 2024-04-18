package com.thesubgraph.wallpaper.view.common

data class PaginationState(
    val page: Int = 0,
    val reachedEnd: Boolean = false,
    val pageSize: Int = 20
)
