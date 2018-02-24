package com.norg.brewhelper.model

open class Phase(val id: Long, var name: String = "Phase", var duration: Int = 0, var description: String = "Do something") {
    var phases: MutableList<TimedPhase> = ArrayList()

}


