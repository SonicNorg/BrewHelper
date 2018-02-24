package com.norg.brewhelper.model

class TimedPhase(var parent: Phase,
                 var phase: Phase, var delay: Int, var from: From, val alarm: Boolean): Phase(0)