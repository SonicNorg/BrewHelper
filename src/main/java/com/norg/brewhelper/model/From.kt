package com.norg.brewhelper.model

enum class From(val parent: Boolean) {
    PARENT_START(true), PARENT_END(true), PREV_START(false), PREV_END(false)
}