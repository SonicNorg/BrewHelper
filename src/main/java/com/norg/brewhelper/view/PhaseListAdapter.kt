package com.norg.brewhelper.view

import android.app.Activity
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import com.norg.brewhelper.R
import com.norg.brewhelper.R.id.phasesList
import com.norg.brewhelper.model.From
import com.norg.brewhelper.model.TimedPhase
import kotlinx.android.synthetic.main.phase_list_item.view.*
import kotlinx.android.synthetic.main.phase_list_item_content.view.*

class PhaseListAdapter(private var ctx: Context, var phases: MutableList<TimedPhase>, val parentActivity: Activity) : BaseAdapter() {
    private var lInflater = ctx.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val logTag: String = this::class.java.simpleName

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: lInflater.inflate(R.layout.phase_list_item, parent, false)
        val phase = phases[position]

        view.phaseName.setText(phase.name)
        view.duration.setText(if (phase.duration != 0) phase.duration.toString() else "")
        view.delay.setText(if (phase.delay != 0) phase.delay.toString() else "")
        view.description.setText(phase.description)
        view.alarm.isChecked = phase.alarm

        view.start.adapter = ArrayAdapter<String>(ctx, R.layout.spinner_dropdown_item, From.values().map { it.friendly })
        view.start.setSelection(phase.start.ordinal)

        val content = view.findViewById<ViewGroup>(R.id.phaseListItemContent)
        for(i in 0 until content.childCount) {
            content.getChildAt(i).onFocusChangeListener = View.OnFocusChangeListener({ v: View, b: Boolean ->
                Log.d(logTag, "Focus changed, this view=$view new view=$v focus=$b")
                with(phases[position]) {
                    name = view.phaseName.text.toString()
                    duration = if (view.duration.text.toString().isNotBlank()) Integer.valueOf(view.duration.text.toString()) else 0
                    delay = if (view.delay.text.toString().isNotBlank()) Integer.valueOf(view.delay.text.toString()) else 0
                    description = view.description.text.toString()
                    alarm = view.alarm.isChecked
                    start = From.values()[view.start.selectedItemPosition]
                }
            })
        }
        view.start.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //do nothing
            }

            override fun onItemSelected(parent: AdapterView<*>?, v: View?, p: Int, i: Long) {
                phases[position].start = From.values()[p]
            }

        }
        view.alarm.setOnClickListener({
            phases[position].alarm = view.alarm.isChecked
        })

        view.edit.setOnClickListener({
            ActivityCompat.startActivityForResult(parentActivity,
                    Intent(ctx, EditPhaseActivity::class.java).putExtra("Phase", phase),0, null)
        })

        view.remove.setOnClickListener({
            phases.removeAt(position)
            (view.rootView).findViewById<ListView>(phasesList).adapter = PhaseListAdapter(ctx, phases, parentActivity)
        })
        return view
    }

    override fun getItem(position: Int): Any {
        return phases[position]
    }

    override fun getItemId(position: Int): Long {
        return phases[position].id
    }

    override fun getCount(): Int {
        return phases.size
    }
}