package ru.desh.library.ui

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.google.android.material.card.MaterialCardView
import ru.desh.library.R

// TODO show last input digit in hidden mode
open class CustomOtpInputView: LinearLayout {
    internal enum class CustomOptInputType(val type: Int) {
        NUMBER(1),
        NUMBER_HIDDEN(2)
    }

    private val mListOfDigits = mutableListOf<EditText>()
    private val mListOfViews = mutableListOf<CardView>()
    private var digitsCount = 6
    fun digitsCount() = digitsCount

    private var cornerRadius = 10
    private var inputSpacing = 15
    private var inputBackgroundColor = 0
    private var inputFocusColor = 0
    private var textSize = 0
    private var textColor = 0
    private var textStyle = 0
    private var textAppearance = 0
    private var inputType = 1

    private var borderColor = -1
    private var borderWidth = 0
    private var cursorVisibility = true
    private var hintText = ""
    private var hintColor = -1

    private lateinit var mOtpText: String
    fun getText(): String {
        return mListOfDigits.joinToString(separator = "") {
            it.text.toString()
        }
    }
    fun setText(value: String) {
        if (value.length == digitsCount) {
            mOtpText = value
            mListOfDigits.forEachIndexed { i, field ->
                field.setText(mOtpText[i].toString())
            }
        }
    }

    var isCursorVisible: Boolean
        set(value) {
            mListOfDigits.forEach {
                it.isCursorVisible = value
            }
        }
        get() {
            val sum: Int = mListOfDigits.sumOf {
                // Cast to Int to fix resolution ambiguity
                if (it.isCursorVisible) 1.toInt() else 0
            }
            return sum == mListOfDigits.size
        }

    constructor(context: Context): super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(
        context, attrs, defStyleAttr
            ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        inflate(context, R.layout.custom_otp_input, this)
        if (attrs != null) {
            initAttrs(attrs)
        }

        if (digitsCount <= 0) {
            throw RuntimeException("Negative input length size")
        }

        for (i in 0 until digitsCount) {
            val digitView = inflate(context, R.layout.custom_otp_input_digit_field, null) as MaterialCardView
            digitView.radius = cornerRadius.toFloat()

            val editText = digitView.getChildAt(0) as EditText
            editText.setBackgroundColor(inputBackgroundColor)
            editText.textSize = textSize.toFloat()
            editText.setTextColor(textColor)
            editText.setTypeface(editText.typeface, textStyle)
            editText.setTextAppearance(textAppearance)
            editText.hint = hintText.getOrNull(i)?.toString() ?: "_"

            when (inputType) {
                CustomOptInputType.NUMBER.type -> {
                    editText.inputType =
                        InputType.TYPE_CLASS_NUMBER
                }
                CustomOptInputType.NUMBER_HIDDEN.type -> {
                    editText.inputType =
                        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
                }
            }

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (s.isNotEmpty()) {
                        moveToNext(editText)
                    } else {
                        moveBackward(editText)
                    }
                }
            })
            editText.setOnFocusChangeListener { view, b ->
                if (!b) editText.setBackgroundColor(inputBackgroundColor) else
                    editText.setBackgroundColor(inputFocusColor)
            }
            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL
                    && event.action == KeyEvent.ACTION_UP
                    && (editText.text?.toString() ?: "").isEmpty()
                ) {
                    moveBackward(editText)
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

            if(borderColor!= -1) {
                digitView.strokeColor = borderColor
                digitView.strokeWidth = borderWidth
            }

            if(hintColor != -1) {
                editText.setHintTextColor(hintColor)
            }

            mListOfDigits.add(editText)
            mListOfViews.add(digitView)

            this.addView(digitView)
        }
        isCursorVisible = cursorVisibility
    }

    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.theme
            .obtainStyledAttributes(attrs, R.styleable.CustomOtpInputView, 0, 0)

        digitsCount = typedArray.getInt(R.styleable.CustomOtpInputView_digitsCount, 6)
        inputBackgroundColor = typedArray.getColor(
            R.styleable.CustomOtpInputView_inputBackground,
            ContextCompat.getColor(context, android.R.color.transparent)
        )

        inputFocusColor = typedArray.getColor(
            R.styleable.CustomOtpInputView_inputFocusBackground,
            ContextCompat.getColor(context, android.R.color.transparent)
        )

        inputSpacing = typedArray.getDimensionPixelSize(
            R.styleable.CustomOtpInputView_inputSpacing,
            20,
        )

        val defaultRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f, resources.displayMetrics
        ).toInt()

        cornerRadius = typedArray.getDimensionPixelSize(
            R.styleable.CustomOtpInputView_inputRadius,
            defaultRadius
        )

        textColor = typedArray.getColor(
            R.styleable.CustomOtpInputView_android_textColor,
            ContextCompat.getColor(context, android.R.color.black)
        )

        val defaultTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            12f, resources.displayMetrics
        ).toInt()

        textSize = typedArray.getDimensionPixelSize(
            R.styleable.CustomOtpInputView_android_textSize,
            defaultTextSize
        )

        textStyle = typedArray.getInt(
            R.styleable.CustomOtpInputView_android_textStyle,
            0
        )

        textAppearance = typedArray.getResourceId(
            R.styleable.CustomOtpInputView_android_textAppearance,
            0
        )

        inputType = typedArray.getInt(
            R.styleable.CustomOtpInputView_inputType,
            CustomOptInputType.NUMBER.type
        )

        borderColor = typedArray.getColor(
            R.styleable.CustomOtpInputView_borderColor,
            -1
        )

        borderWidth = typedArray.getDimensionPixelSize(
            R.styleable.CustomOtpInputView_borderWidth,
            0
        )

        hintText = typedArray.getString(
            R.styleable.CustomOtpInputView_android_hint
        ) ?: ""

        hintColor = typedArray.getColor(
            R.styleable.CustomOtpInputView_android_textColorHint,
            -1
        )

        cursorVisibility = typedArray.getBoolean(
            R.styleable.CustomOtpInputView_android_cursorVisible,
            true
        )
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)

        val viewChildren = children.toList()
        val lastChild = viewChildren.last()

        viewChildren.forEach {
            val view = it as CardView

            val params = view.layoutParams as LayoutParams
            params.width = ((viewWidth - ((digitsCount - 1) * inputSpacing)) / digitsCount)
            params.height = ViewGroup.LayoutParams.MATCH_PARENT

            if (view != lastChild) {
                params.setMargins(0, 0, inputSpacing, 0)
            }
        }
    }

    private fun EditText.demandFocus() {
        this.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun moveToNext(editText: EditText) {
        val currentInput = mListOfDigits.indexOf(editText) + 1
        if (currentInput < mListOfDigits.size) {
            val v = mListOfDigits[currentInput]
            v.demandFocus()
        }
    }

    private fun moveBackward(editText: EditText) {
        val currentInput = mListOfDigits.indexOf(editText) - 1
        if (currentInput >= 0) {
            val v = mListOfDigits[currentInput]
            v.demandFocus()
        }
    }

    fun focusOtpInput() {
        val firstInput = mListOfDigits.first()
        firstInput.postDelayed({
            firstInput.clearFocus()
            firstInput.demandFocus()
        }, 100)
    }

    /*Takes a function which accepts the {inputComplete} boolean and current {otpText}*/
    fun inputChangedListener(onChanged: (inputComplete: Boolean, otpText: String) -> Unit) {
        mListOfDigits.forEach {
            it.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    onChanged(mOtpText.length == digitsCount, mOtpText)
                }
            })
        }
    }

    fun onInputFinishedListener(onInputFinished: (String) -> Unit) {
        mListOfDigits.last().addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (mOtpText.length == digitsCount) {
                    onInputFinished(mOtpText)
                }
            }
        })
    }

    fun reset() {
        mListOfDigits.forEach {
            it.setText("")
        }
        mListOfDigits.firstOrNull()?.requestFocus()
    }

    fun setHint(hintText: String) {
        mListOfDigits.forEachIndexed { index, editText ->
            editText.hint = hintText.getOrNull(index)?.toString() ?: ""
        }
    }

    fun setStrokeColor(@ColorInt color: Int) {
        mListOfDigits.forEach { editText ->
            val parent = editText.parent
            if(parent is MaterialCardView) {
                parent.strokeColor = color
            }
        }
    }

    fun setStrokeWidth(width: Int) {
        mListOfDigits.forEach { editText ->
            val parent = editText.parent
            if(parent is MaterialCardView) {
                parent.strokeWidth = width
            }
        }
    }
}