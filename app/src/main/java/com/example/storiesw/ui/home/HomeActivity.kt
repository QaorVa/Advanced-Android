package com.example.storiesw.ui.home

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storiesw.R
import com.example.storiesw.data.model.Story
import com.example.storiesw.databinding.ActivityHomeBinding
import com.example.storiesw.ui.adapter.HomeAdapter
import com.example.storiesw.ui.adapter.LoadingStateAdapter
import com.example.storiesw.ui.add.AddActivity
import com.example.storiesw.ui.base.BaseActivity
import com.example.storiesw.ui.detail.DetailActivity
import com.example.storiesw.ui.map.MapsActivity
import com.example.storiesw.ui.settings.SettingsActivity
import com.example.storiesw.utils.showLoading

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModel.ViewModelFactory(this)
    }
    private lateinit var adapter: HomeAdapter

    override fun getViewBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun setUI() {

    }

    override fun setProcess() {

    }

    override fun setActions() {
        with(binding) {
            addButton.setOnClickListener {
                moveToAdd()
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

    override fun setAdapter() {
        adapter = HomeAdapter(object : HomeAdapter.OnItemClickCallback {

            override fun onItemClicked(data: Story) {
                moveToDetail(data)
            }
        })

        with(binding) {
            val layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
            rvStory.layoutManager = layoutManager
            rvStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
        }

    }

    override fun setObservers() {
        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(binding.progressCard, it)
        }

        viewModel.isError.observe(this) { isError ->
            if(isError) {
                showErrorDialog()
            }
        }
    }

    private fun moveToDetail(data: Story) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID, data.id)
        startActivity(intent)
    }

    private fun moveToAdd() {
        val intent = Intent(this@HomeActivity, AddActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings_menu -> {
                moveToSettings()
            }
            R.id.maps_menu -> {
                moveToMaps()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun moveToMaps() {
        val intent = Intent(this@HomeActivity, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun moveToSettings() {
        val intent = Intent(this@HomeActivity, SettingsActivity::class.java)
        startActivity(intent)
    }


}