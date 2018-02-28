package com.norg.brewhelper.model

import java.io.Serializable

open class Phase(var id: Long, var name: String = "", var duration: Int = 0, var description: String = ""): Serializable {
    var phases: MutableList<TimedPhase> = ArrayList()
    override fun toString(): String {
        return "Phase(id=$id, name='$name', description='$description', phases.size=${phases.size})"
    }
}


