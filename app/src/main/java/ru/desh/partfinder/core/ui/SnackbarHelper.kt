package ru.desh.partfinder.core.ui

import android.content.ClipboardManager.OnPrimaryClipChangedListener
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import ru.desh.partfinder.R

class SnackbarManager {
    enum class Type {
        PRIMARY, SECONDARY, SUCCESS, WARNING, DANGER
    }

    private lateinit var snackbar: Snackbar
    private lateinit var snackbarLayout: Snackbar.SnackbarLayout
    private lateinit var customView: View
    private var snackbarType = Type.PRIMARY

    @LayoutRes
    private val snackbarLayoutId = R.layout.app_snackbar



    fun build(view: View, layoutInflater: LayoutInflater,  duration: Int):SnackbarManager {
        snackbar = Snackbar.make(view, "", duration)
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        customView = layoutInflater.inflate(snackbarLayoutId, null)
        customView.findViewById<ImageButton>(R.id.app_snackbar_button_close).setOnClickListener {
            snackbar.dismiss()
        }
        return this
    }

    fun setType(type: Type): SnackbarManager {
        snackbarType = type
        val icon = when(type) {
            Type.PRIMARY -> R.drawable.ic_info
            Type.SECONDARY -> R.drawable.ic_info
            Type.SUCCESS -> R.drawable.ic_success
            Type.WARNING -> R.drawable.ic_warning
            Type.DANGER -> R.drawable.ic_danger
        }
        return this
    }

    fun setTitle(title: String): SnackbarManager {
        customView.findViewById<TextView>(R.id.app_snackbar_title)
            .text = title
        return this
    }

    fun setText(text: String): SnackbarManager {
        customView.findViewById<TextView>(R.id.app_snackbar_text)
            .text = text
        return this
    }

    // set button onClickListener
//    fun setLayout(layoutInflater: LayoutInflater,
//                  @LayoutRes layoutId: Int): SnackbarManager {
//        customView = layoutInflater.inflate(layoutId, null)
//        return this
//    }
    fun show() {
        snackbarLayout.setPadding(0,0,0,0)
        snackbarLayout.addView(customView, 0)
        snackbar.show()
    }
}