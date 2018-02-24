package com.norg.brewhelper.view

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.SimpleAdapter
import android.widget.SpinnerAdapter
import com.norg.brewhelper.R
import com.norg.brewhelper.model.From
import com.norg.brewhelper.model.TimedPhase
import kotlinx.android.synthetic.main.phase_list_item.view.*

class PhaseListAdapter(var ctx: Context, var phases: MutableList<TimedPhase>) : BaseAdapter() {
    var lInflater = ctx.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: lInflater.inflate(R.layout.phase_list_item, parent, false)
        val phase = phases[position]

        view.phaseName.setText(phase.name)
        view.duration.setText(phase.duration)
        view.delay.setText(phase.delay)
        view.description.setText(phase.description)
        view.alarm.isChecked = phase.alarm

        view.from.adapter = ArrayAdapter(ctx, view.id, R.id.from, From.values())
        view.from.setSelection(phase.from.ordinal)

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