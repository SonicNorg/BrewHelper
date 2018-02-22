package com.norg.brewhelper

class TimedPhase(var parent: Phase,
                 var phase: Phase, var minutes: Int, var from: From): Phase()