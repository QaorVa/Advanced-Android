package com.example.storiesw.ui.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.storiesw.data.model.Story
import com.example.storiesw.databinding.ActivityDetailBinding
import com.example.storiesw.ui.base.BaseActivity
import com.example.storiesw.utils.Constants.SPEED_NORMAL
import com.example.storiesw.utils.Constants.SPEED_SLOW
import com.example.storiesw.utils.getTimeAgo
import com.example.storiesw.utils.setAlphaAnimation
import com.example.storiesw.utils.setImageUrl
import com.example.storiesw.utils.showLoading

class DetailActivity : BaseActivity<ActivityDetailBinding>(){
    private lateinit var viewModel: DetailViewModel

    override fun getViewBinding(): ActivityDetailBinding {
        return ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun setUI() {

    }

    override fun setProcess() {
        val id = intent.getStringExtra(EXTRA_ID)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.setStoryDetail(id)
    }

    override fun setObservers() {
        viewModel.getStoryDetail().observe(this) { storyDetail ->
            if(storyDetail != null) {
                setDetailStory(storyDetail.data)
                playAnimation()
            }
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

    private fun setDetailStory(story: Story) {
        binding.apply {
            imgStory.setImageUrl(story.photoUrl)
            tvUserName.text = story.name
            tvUploadAt.text = story.createdAt.getTimeAgo(this@DetailActivity)
            tvDescription.text = story.description
        }

    }

    private fun playAnimation() {
        binding.apply {
            ObjectAnimator.ofFloat(imgStory, View.TRANSLATION_Y, -100f, 0f).apply {
                duration = 1000L

            }.start()

            val imageUser = imgUser.setAlphaAnimation(SPEED_NORMAL)
            val textUsername = tvUserName.setAlphaAnimation(SPEED_NORMAL)
            val textUploadAt = tvUploadAt.setAlphaAnimation(SPEED_NORMAL)
            val labelDescription = txLabelDescription.setAlphaAnimation(SPEED_SLOW)
            val textDescription = tvDescription.setAlphaAnimation(SPEED_SLOW)

            val together = AnimatorSet().apply {
                playTogether(labelDescription, textDescription)
            }

            AnimatorSet().apply {
                playSequentially(imageUser, textUsername, textUploadAt, together)
                start()
            }

        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }

}