package com.example.storiesw.utils

import android.animation.ObjectAnimator
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.setImageUrl(url: String?) {
    Glide.with(this.rootView).load(url).apply(RequestOptions())
        .into(this)
}

fun EditText.showError(message: String) {
    error = message
    requestFocus()
}

fun View.setAlphaAnimation(animationSpeed: Long): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, View.ALPHA, 1f)
        .setDuration(animationSpeed)
}