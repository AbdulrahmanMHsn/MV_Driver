package com.seamlabs.mvpdriver.common.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.seamlabs.mvpdriver.databinding.ComponentAppBarBinding

class AppBarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val binding: ComponentAppBarBinding =
        ComponentAppBarBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setTitle(@StringRes titleStringId: Int) {
        binding.appBarTitle.setText(titleStringId)
    }

    fun setTitleColor(@ColorRes colorId: Int) {
        binding.appBarTitle.setTextColor(resources.getColor(colorId))
    }

    fun changeBackgroundColor(@ColorRes colorId: Int) {
        binding.appBarBackgroundV.setBackgroundColor(resources.getColor(colorId))
    }

    fun setTitleString(titleString: String) {
        binding.appBarTitle.text = titleString
    }

    fun setBackButtonAction(backButtonAction: (() -> Unit)?) {
        binding.appBarBackButton.setOnClickListener {
            backButtonAction?.invoke()
        }
    }

    fun useBackButton(toBeUsed: Boolean) {
        binding.appBarBackButton.visibility = when (toBeUsed) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun useBackButton(toBeUsed: Boolean, backButtonAction: (() -> Unit)?) {
        useBackButton(toBeUsed)
        setBackButtonAction(backButtonAction)
    }

    fun setActionButtonAction(action: (() -> Unit)?) {
        binding.actionButton.setOnClickListener {
            action?.invoke()
        }
    }

    fun setSecondActionButtonAction(action: (() -> Unit)?) {
        binding.actionButton2.setOnClickListener {
            action?.invoke()
        }
    }

    fun useActionButton(toBeUsed: Boolean, @DrawableRes icon: Int) {
        binding.actionButton.visibility = when (toBeUsed) {
            true -> View.VISIBLE
            else -> View.GONE
        }
        binding.actionButton.setImageResource(icon)
    }

    fun useSecondActionButton(toBeUsed: Boolean, @DrawableRes icon: Int) {
        binding.actionButton2.visibility = when (toBeUsed) {
            true -> View.VISIBLE
            else -> View.GONE
        }
        binding.actionButton2.setImageResource(icon)
    }

    fun useActionButton(toBeUsed: Boolean, @DrawableRes icon: Int, action: (() -> Unit)?) {
        useActionButton(toBeUsed, icon)
        setActionButtonAction(action)
    }

    fun useSecondActionButton(toBeUsed: Boolean, @DrawableRes icon: Int, action: (() -> Unit)?) {
        useSecondActionButton(toBeUsed, icon)
        setSecondActionButtonAction(action)
    }

    fun changeActionButtonDrawable(@DrawableRes icon: Int) {
        binding.actionButton2.setImageResource(icon)
    }
}