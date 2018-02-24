package com.norg.brewhelper

open class Phase(var name: String = "Phase", var duration: Int = 0, var description: String = "Do something") {
    var phases: MutableList<TimedPhase> = ArrayList()

}


