package com.norg.brewhelper.model

enum class From(val parent: Boolean, val friendly: String) {
    PARENT_START(true, "От начала родителя"),
    PARENT_END(true, "До окончания родителя"),
    PREV_START(false, "От начала предыдущей"),
    PREV_END(false, "Хрень какая-то");
}