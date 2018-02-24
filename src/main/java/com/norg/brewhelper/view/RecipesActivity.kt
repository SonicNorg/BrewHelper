package com.norg.brewhelper.view

import android.content.Intent
import android.database.sqlite.SQLiteCursor
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.CursorAdapter.FLAG_AUTO_REQUERY
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.SimpleCursorAdapter
import com.norg.brewhelper.DBHelper
import com.norg.brewhelper.R
import com.norg.brewhelper.R.layout.recipe_list_item
import com.norg.brewhelper.model.Phase
import kotlinx.android.synthetic.main.activity_recipes.*
import kotlinx.android.synthetic.main.app_bar_recipes.*
import kotlinx.android.synthetic.main.content_recipes.*

class RecipesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    internal val dbHelper = DBHelper.dbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes)
        setSupportActionBar(toolbar)
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        fab.setOnClickListener {
            val intent = Intent(this, EditRecipeActivity::class.java)
            startActivityForResult(intent, 0)
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        recipesList.adapter = SimpleCursorAdapter(this, recipe_list_item, dbHelper.getRecipesCursor(), arrayOf("name", "description"), intArrayOf(R.id.recipeName, R.id.recipeDescription), FLAG_AUTO_REQUERY)
        recipesList.onItemClickListener = AdapterView.OnItemClickListener({parent, view, position, id ->
            val current = recipesList.adapter.getItem(position) as SQLiteCursor
            val item = Phase(current.getLong(0),
                    current.getString(1),
                    current.getInt(2),
                    current.getString(3))
            item.phases.addAll(dbHelper.getPhases(item))
            val intent = Intent(this, EditRecipeActivity::class.java)
            intent.putExtra("Recipe", item)
            startActivityForResult(intent, 0)})
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.recipes, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()
        recipesList.adapter = SimpleCursorAdapter(this, recipe_list_item, dbHelper.getRecipesCursor(), arrayOf("name", "description"), intArrayOf(R.id.recipeName, R.id.recipeDescription), FLAG_AUTO_REQUERY)
//        Snackbar.make(recipesList, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
