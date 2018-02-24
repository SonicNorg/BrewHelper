package com.norg.brewhelper.view

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import com.norg.brewhelper.R
import com.norg.brewhelper.model.From
import com.norg.brewhelper.model.TimedPhase
import kotlinx.android.synthetic.main.phase_list_item.view.*

class PhaseListAdapter(var ctx: Context, var phases: MutableList<TimedPhase>) : BaseAdapter() {
    var lInflater = ctx.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val LOG_TAG: String = this::class.java.simpleName

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: lInflater.inflate(R.layout.phase_list_item, parent, false)
        val phase = phases[position]

        view.phaseName.setText(phase.name)
        view.duration.setText(phase.duration.toString())
        view.delay.setText(phase.delay.toString())
        view.description.setText(phase.description)
        view.alarm.isChecked = phase.alarm

        view.start.adapter = ArrayAdapter<From>(ctx, android.R.layout.simple_list_item_1, From.values())
        view.start.setSelection(phase.start.ordinal)

        for(i in 0 until (view as ViewGroup).childCount) {
            view.getChildAt(i).onFocusChangeListener = View.OnFocusChangeListener({ v: View, b: Boolean ->
                Log.d(LOG_TAG, "Focus changed, this view=$view new view=$v focus=$b")
                with(phases[position]) {
                    name = view.phaseName.text.toString()
                    duration = Integer.valueOf(view.duration.text.toString())
                    delay = Integer.valueOf(view.delay.text.toString())
                    description = view.description.text.toString()
                    alarm = view.alarm.isChecked
                    start = view.start.selectedItem as From
                }
            })
        }
        view.start.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, v: View?, p: Int, i: Long) {
                phases[position].start = From.values()[p]
            }

        }
        view.alarm.setOnClickListener({
            phases[position].alarm = view.alarm.isChecked
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