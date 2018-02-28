package com.norg.brewhelper.model

class TimedPhase(var parent: Phase,
                 phase: Phase, var delay: Int = 0, var start: From = From.PARENT_START, var alarm: Boolean = true) : Phase(phase.id, phase.name, phase.duration, phase.description)