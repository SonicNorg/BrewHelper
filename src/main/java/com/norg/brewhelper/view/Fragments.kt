package com.norg.brewhelper.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.norg.brewhelper.R
import com.norg.brewhelper.model.Phase
import com.norg.brewhelper.model.TimedPhase
import kotlinx.android.synthetic.main.fragment_edit_phases.*
import kotlinx.android.synthetic.main.fragment_edit_phases.view.*
import kotlinx.android.synthetic.main.fragment_edit_recipe.view.*

abstract class CommonFragment : Fragment() {
    val logTag: String = javaClass.simpleName

    abstract fun fillRecipeFromView(recipe: Phase, view: View): Phase

    abstract fun fillViewFromRecipe(view: View, recipe: Phase)

    protected fun fromViewToIntent(rootView: View) {
        val recipe = activity.intent.extras?.get("Recipe") as Phase? ?: Phase(0, "", 0, "")
        fillRecipeFromView(recipe, rootView)
        activity.intent.putExtra("Recipe", recipe)
        Log.d(logTag, "Saved to intent $recipe")
    }

    protected fun fromIntentToView(view: View) {
        val recipe = activity.intent.extras?.get("Recipe") as Phase? ?: Phase(0, "", 0, "")
        fillViewFromRecipe(view, recipe)
        Log.d(logTag, "Filled from intent $recipe")
    }

    override fun onPause() {
        Log.d(logTag, "onPause")
//        view?.let { fromViewToIntent(view!!) }
        super.onPause()
    }

    override fun onResume() {
        Log.d(logTag, "onResume")
        super.onResume()
        view?.let { fromIntentToView(view!!) }
    }
}

class EditRecipeFragment : CommonFragment() {
    override fun fillRecipeFromView(recipe: Phase, view: View): Phase {
        with(recipe) {
            name = view.recipeName.text.toString()
            description = view.recipeDescription.text.toString()
        }
        return recipe
    }

    override fun fillViewFromRecipe(view: View, recipe: Phase) {
        view.recipeName.setText(recipe.name)
        view.recipeDescription.setText(recipe.description)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //            rootView.section_label.text = getString(R.string.section_format, arguments.getInt(ARG_SECTION_NUMBER))
        val view = inflater.inflate(R.layout.fragment_edit_recipe, container, false)
        fromIntentToView(view)

        val onFocusChangeListener = View.OnFocusChangeListener({ _, focus: Boolean ->
            Log.d(logTag, "Focus changed to $focus")
            if (!focus) fromViewToIntent(view)
        })

        view.recipeName.onFocusChangeListener = onFocusChangeListener
        view.recipeDescription.onFocusChangeListener = onFocusChangeListener
        return view
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int): EditRecipeFragment {
            val fragment = EditRecipeFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}

class EditIngredientsFragment : CommonFragment() {
    override fun fillRecipeFromView(recipe: Phase, view: View): Phase {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return recipe
    }

    override fun fillViewFromRecipe(view: View, recipe: Phase) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //            rootView.section_label.text = getString(R.string.section_format, arguments.getInt(ARG_SECTION_NUMBER))
        return inflater.inflate(R.layout.fragment_edit_ingredients, container, false)
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int): EditIngredientsFragment {
            val fragment = EditIngredientsFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}

class EditPhasesFragment : CommonFragment() {
    private lateinit var parent: Phase

    override fun fillRecipeFromView(recipe: Phase, view: View): Phase {
        with(recipe) {
            phases.clear()
            phases.addAll(parent.phases)
            name = parent.name
            description = parent.description
        }
        return recipe
    }

    override fun fillViewFromRecipe(view: View, recipe: Phase) {
        parent = recipe
        view.phasesList.adapter = PhaseListAdapter(view.context, parent.phases, activity)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //            rootView.section_label.text = getString(R.string.section_format, arguments.getInt(ARG_SECTION_NUMBER))
        val view = inflater.inflate(R.layout.fragment_edit_phases, container, false)
        parent = activity.intent.extras?.get("Recipe") as Phase? ?: Phase(0, "", 0, "")
//            view.phasesList.adapter = PhaseListAdapter(view.context, DBHelper.dbHelper(view.context).getPhases(Phase(0)))
        view.phasesList.descendantFocusability = ListView.FOCUS_AFTER_DESCENDANTS
        view.phasesList.adapter = PhaseListAdapter(view.context, parent.phases, activity)
        view.fabPhases.show()
        view.fabPhases.setOnClickListener({
            parent.phases.add(TimedPhase(parent, Phase(0)))
            activity.intent.putExtra("Recipe", parent)
            phasesList.adapter = PhaseListAdapter(it.context, parent.phases, activity)
        })
        return view
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int): EditPhasesFragment {
            val fragment = EditPhasesFragment()
            val args = Bundle() //TODO: look for a way to put existing bundle with Phase
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}