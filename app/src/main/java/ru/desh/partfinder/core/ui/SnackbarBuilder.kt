package ru.desh.partfinder.core.ui

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat
import com.google.android.material.snackbar.Snackbar
import ru.desh.partfinder.R
import ru.desh.partfinder.databinding.AppSnackbarBinding

class SnackbarBuilder(view: ViewGroup, layoutInflater: LayoutInflater, duration: Int) {
    enum class Type {
        PRIMARY, SECONDARY, SUCCESS, WARNING, DANGER
    }

    private var snackbar: Snackbar
    private var snackbarLayout: Snackbar.SnackbarLayout
    private var snackbarBinding: AppSnackbarBinding
    //private lateinit var customView: View
    private var snackbarType = Type.PRIMARY

    @LayoutRes
    private val snackbarLayoutId = R.layout.app_snackbar
    private var resources: Resources

    init {
        snackbar = Snackbar.make(view, "", duration)
        snackbar.view.setBackgroundColor(Color.TRANSPARENT)
        snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarBinding = AppSnackbarBinding.inflate(layoutInflater, snackbar.view as ViewGroup, false)
        snackbarBinding.appSnackbarButtonClose.setOnClickListener {
            snackbar.dismiss()
        }
        //        customView = layoutInflater.inflate(snackbarLayoutId, null)
//        customView.findViewById<ImageButton>(R.id.app_snackbar_button_close).setOnClickListener {
//            snackbar.dismiss()
//        }
        resources = view.resources
    }
    fun setType(type: Type): SnackbarBuilder {
        snackbarType = type
        val theme = snackbar.view.context.theme
        val icon = when(type) {
            Type.PRIMARY -> R.drawable.ic_info
            Type.SECONDARY -> R.drawable.ic_info
            Type.SUCCESS -> R.drawable.ic_success
            Type.WARNING -> R.drawable.ic_warning
            Type.DANGER -> R.drawable.ic_danger
        }

        var typedValue = TypedValue()
        theme.resolveAttribute(when(type){
            Type.PRIMARY -> R.attr.primaryIconTint
            Type.SECONDARY -> R.attr.secondaryIconTint
            Type.SUCCESS -> R.attr.successIconTint
            Type.WARNING -> R.attr.warningIconTint
            Type.DANGER -> R.attr.dangerIconTint
        }, typedValue, true)
        @ColorInt val iconTint = typedValue.data
        theme.resolveAttribute(when(type){
            Type.PRIMARY -> R.attr.primaryTextColor
            Type.SECONDARY -> R.attr.secondaryTextColor
            Type.SUCCESS -> R.attr.successTextColor
            Type.WARNING -> R.attr.warningTextColor
            Type.DANGER -> R.attr.dangerTextColor
        }, typedValue, true)
        @ColorInt val textColor = typedValue.data
        theme.resolveAttribute(when(type){
            Type.PRIMARY -> R.attr.primaryBgColor
            Type.SECONDARY -> R.attr.secondaryBgColor
            Type.SUCCESS -> R.attr.successBgColor
            Type.WARNING -> R.attr.warningBgColor
            Type.DANGER -> R.attr.dangerBgColor
        }, typedValue, true)
        @ColorInt val bgColor = typedValue.data
        theme.resolveAttribute(when(type){
            Type.PRIMARY -> R.attr.primaryAccentColor
            Type.SECONDARY -> R.attr.secondaryAccentColor
            Type.SUCCESS -> R.attr.successAccentColor
            Type.WARNING -> R.attr.warningAccentColor
            Type.DANGER -> R.attr.dangerAccentColor
        }, typedValue, true)
        @ColorInt val accentColor = typedValue.data

        snackbarBinding.apply {
            appSnackbarIcon.setImageDrawable(
                ResourcesCompat.getDrawable(resources, icon, null)
            )
            ImageViewCompat.setImageTintList(appSnackbarIcon, ColorStateList.valueOf(iconTint))
            appSnackbarTitle.setTextColor(textColor)
            appSnackbarText.setTextColor(textColor)
            appSnackbarCard.backgroundTintList = ColorStateList.valueOf(bgColor)
            appSnackbarButtonClose.backgroundTintList = ColorStateList.valueOf(iconTint)
            appSnackbarAccentLine.backgroundTintList = ColorStateList.valueOf(accentColor)
        }
//        customView.apply {
//            findViewById<ImageView>(R.id.app_snackbar_icon).setImageDrawable(
//                ResourcesCompat.getDrawable(resources, icon, null)
//            )
//            ImageViewCompat.setImageTintList(findViewById(R.id.app_snackbar_icon),
//                ColorStateList.valueOf(iconTint)
//            )
//            findViewById<TextView>(R.id.app_snackbar_title).setTextColor(textColor)
//            findViewById<TextView>(R.id.app_snackbar_text).setTextColor(textColor)
//            findViewById<CardView>(R.id.app_snackbar_card).backgroundTintList =
//                ColorStateList.valueOf(bgColor)
//            findViewById<ImageButton>(R.id.app_snackbar_button_close).backgroundTintList =
//                ColorStateList.valueOf(iconTint)
//            findViewById<CardView>(R.id.app_snackbar_accent_line).backgroundTintList =
//                ColorStateList.valueOf(accentColor)
//
//        }
        return this
    }

    fun setTitle(title: String): SnackbarBuilder {
//        customView.findViewById<TextView>(R.id.app_snackbar_title)
//            .text = title
        snackbarBinding.appSnackbarTitle.text = title
        return this
    }

    fun setText(text: String): SnackbarBuilder {
//        customView.findViewById<TextView>(R.id.app_snackbar_text)
//            .text = text
        snackbarBinding.appSnackbarText.text = text
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
        if ((snackbarLayout as ViewGroup).childCount > 1) {
            snackbarLayout.removeViewAt(0)
        }
        snackbarLayout.addView( snackbarBinding.root, 0)//customView, 0)
        snackbar.show()
    }
}