package com.norg.brewhelper.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.norg.brewhelper.DBHelper
import com.norg.brewhelper.R
import com.norg.brewhelper.model.Phase
import kotlinx.android.synthetic.main.activity_edit_recipe.*

class EditRecipeActivity : AppCompatActivity() {
    private val db = DBHelper.dbHelper(this)
    private var recipe: Phase = Phase(0, "", 0, "")

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    private val logTag = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate")
        setContentView(R.layout.activity_edit_recipe)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
        setFab()

        container.addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position) {
                    0 -> fabSave.show()
//                    1 ->
                    2 -> fabSave.hide()
                }
            }
        })
    }

    fun setFab() {
        with(fabSave) {
            show()
            setOnClickListener {
                recipe = intent.extras?.get("Recipe") as Phase? ?: Phase(0, "", 0, "")
                Log.d(logTag, "Saving recipe with ${recipe.phases.size} phases")
                try {
                    db.saveRecipe(recipe)
                    onBackPressed()
                } catch (e: Exception) {
                    Snackbar.make(it, e.localizedMessage, Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show()
                }
            }
            setImageDrawable(ContextCompat.getDrawable(baseContext, android.R.drawable.ic_menu_save))
        }
    }

//    fun fabAddIngredient() {
//
//        fab.setImageDrawable(ContextCompat.getDrawable(baseContext, android.R.drawable.ic_menu_add))
//    }
//
//    fun fabAddPhase() {
//
//        fab.setOnClickListener {
//            with(recipe.phases) {
//                clear()
//                addAll((phasesList.adapter as PhaseListAdapter).phases)
//                add(TimedPhase(recipe, Phase(0, "", 0, ""), 0, From.PARENT_START, true))
//            }
//            intent.putExtra("Recipe", recipe)
//            phasesList.adapter = PhaseListAdapter(it.context, recipe.phases)
//        }
//        fab.setImageDrawable(ContextCompat.getDrawable(baseContext, android.R.drawable.ic_menu_add))
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_edit_recipe, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a EditRecipeFragment (defined as a static inner class below).
            return when (position) {
                0 -> EditRecipeFragment.newInstance(position + 1)
                1 -> EditIngredientsFragment.newInstance(position + 1)
                2 -> EditPhasesFragment.newInstance(position + 1)
                else -> throw IllegalArgumentException("No fragment for page $position")
            }

        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

}
