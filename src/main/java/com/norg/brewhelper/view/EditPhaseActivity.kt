package com.norg.brewhelper.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import com.norg.brewhelper.NullPhaseException
import com.norg.brewhelper.R
import com.norg.brewhelper.model.TimedPhase

import kotlinx.android.synthetic.main.activity_edit_phase.*
import kotlinx.android.synthetic.main.phase_list_item_content.*

class EditPhaseActivity : AppCompatActivity() {
    private lateinit var phase: TimedPhase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_phase)
        setSupportActionBar(toolbar)

        phase = intent.extras?.get("Phase") as TimedPhase? ?: throw NullPhaseException()

        mapView()
        initFabs()
    }

    private fun mapView() {
        phaseName.setText(phase.name)
        description.setText(phase.description)
    }

    private fun initFabs() {
//        fabSave.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
        fabAdd.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
