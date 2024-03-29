package ru.desh.library.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import ru.desh.library.R

//TODO fix text placement
//TODO add animations
class SteppedProgressBarView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initAttributes(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) :
            super(context, attributeSet, defStyleAttr) {
        initAttributes(attributeSet)
    }

    private fun initAttributes(attributeSet: AttributeSet) {
        val typedArray = context.theme
            .obtainStyledAttributes(attributeSet, R.styleable.SteppedProgressBarView, 0, 0)
        labelsTextAppearance = typedArray.getResourceId(
            R.styleable.SteppedProgressBarView_android_textAppearance,
            0
        )

        steppedProgressBarPaint = TextView(context).apply {
            setTextAppearance(labelsTextAppearance)
        }.paint.apply {
            textAlign = Paint.Align.CENTER
        }

        stepSize = typedArray.getDimensionPixelSize(
            R.styleable.SteppedProgressBarView_stepSize, stepSize
        )

        progressBarHeight = typedArray.getDimensionPixelSize(
            R.styleable.SteppedProgressBarView_progressBarHeight, progressBarHeight
        )

        progressDrawableId = typedArray.getResourceId(
            R.styleable.SteppedProgressBarView_progressDrawable,
            android.R.color.transparent
        )
        progressFillDrawableId = typedArray.getResourceId(
            R.styleable.SteppedProgressBarView_progressFillDrawable,
            android.R.color.transparent
        )
        notAchievedStepDrawableId = typedArray.getResourceId(
            R.styleable.SteppedProgressBarView_notAchievedStepDrawable,
            android.R.color.transparent
        )
        achievedStepDrawableId = typedArray.getResourceId(
            R.styleable.SteppedProgressBarView_achievedStepDrawable,
            android.R.color.transparent
        )
        hasPreStep = typedArray.getBoolean(
            R.styleable.SteppedProgressBarView_hasPreStep,
            hasPreStep
        )
        hasPostStep = typedArray.getBoolean(
            R.styleable.SteppedProgressBarView_hasPostStep,
            hasPostStep
        )

        progressDrawable = ResourcesCompat.getDrawable(resources, progressDrawableId, null)!!
        progressFillDrawable =
            ResourcesCompat.getDrawable(resources, progressFillDrawableId, null)!!
        notAchievedStepDrawable =
            ResourcesCompat.getDrawable(resources, notAchievedStepDrawableId, null)!!
        achievedStepDrawable =
            ResourcesCompat.getDrawable(resources, achievedStepDrawableId, null)!!

        val stepsAttr = typedArray.getTextArray(R.styleable.SteppedProgressBarView_android_entries)
        if (stepsAttr != null) {
            setSteps(stepsAttr.toList().map { it.toString() })
        }
        currentStep = typedArray.getInt(
            R.styleable.SteppedProgressBarView_defaultStep,
            0
        )

        typedArray.recycle()
    }

    private var stepsNumber = 3

    @DrawableRes
    private var progressDrawableId = 0
    private lateinit var progressDrawable: Drawable

    @DrawableRes
    private var progressFillDrawableId = 0
    private lateinit var progressFillDrawable: Drawable

    @DrawableRes
    private var notAchievedStepDrawableId = 0
    private lateinit var notAchievedStepDrawable: Drawable

    @DrawableRes
    private var achievedStepDrawableId = 0
    private lateinit var achievedStepDrawable: Drawable

    private var labelsTextAppearance = 0

    private var mStepsList = mutableListOf<String>()
    private var mStepsBordersList = mutableListOf<Rect>()
    private var mLabelsBordersList = mutableListOf<Rect>()
    private lateinit var mProgressBarBorders: Rect
    private lateinit var mProgressFillBorders: Rect

    private var steppedProgressBarPaint = Paint()

    private var currentStep = 0

    private var viewWidth = 0
    private var viewHeight = 0

    private var stepSize = 30
    private var progressBarHeight = 10

    private var hasPreStep = true
    private var hasPostStep = true

    var onStart = true
        private set
    var finished = false
        private set

    // TODO
    // onStartProgressAnimator
    // onProgressBarChangeAnimator
    // onActivateStepAnimator
    // onDeactivateStepAnimator
    // onFinishProgressAnimator

    init {
        val emptySteps = arrayOfNulls<String>(stepsNumber).map { it ?: "" }.toList()
        setSteps(emptySteps)
    }

    fun setSteps(steps: List<String>) {
        // stepsNumber >= 2
        stepsNumber = steps.size
        currentStep = 0
        mStepsList = steps.toMutableList()
        mProgressBarBorders = Rect()
        mProgressFillBorders = Rect()
        mStepsBordersList = mStepsList.map { Rect() }.toMutableList()
        mLabelsBordersList = mStepsList.map { Rect() }.toMutableList()
        remeasure()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        remeasure()
    }

    private fun remeasure() {
        mProgressBarBorders.set(
            0, stepSize / 2 - progressBarHeight / 2,
            viewWidth, stepSize / 2 + progressBarHeight / 2
        )
        val stepStartEndDistance = viewWidth / (stepsNumber + 1) / 2
        val stepsIndent = (viewWidth - 2 * stepStartEndDistance) / (stepsNumber - 1)
        for (i in 0 until mStepsBordersList.size) {
            mStepsBordersList[i].set(
                stepStartEndDistance + i * stepsIndent - stepSize / 2, 0,
                stepStartEndDistance + i * stepsIndent + stepSize / 2, stepSize
            )

            val stepLabel = mStepsList[i]
            steppedProgressBarPaint.getTextBounds(
                stepLabel,
                0,
                stepLabel.length,
                mLabelsBordersList[i]
            )
        }
        val fillRightBorder = if (hasPostStep && currentStep > stepsNumber) viewWidth else
            stepStartEndDistance + (currentStep - 1) * stepsIndent

        mProgressFillBorders.set(
            0, stepSize / 2 - progressBarHeight / 2,
            fillRightBorder, stepSize / 2 + progressBarHeight / 2
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            progressDrawable.bounds = mProgressBarBorders
            progressDrawable.draw(canvas)
            progressFillDrawable.bounds = mProgressFillBorders
            progressFillDrawable.draw(canvas)
            mStepsBordersList.forEachIndexed { i, step ->
                if (i + 1 <= currentStep) {
                    achievedStepDrawable.bounds = step
                    achievedStepDrawable.draw(canvas)
                } else {
                    notAchievedStepDrawable.bounds = step
                    notAchievedStepDrawable.draw(canvas)
                }
                canvas.drawText(
                    mStepsList[i],
                    step.left.toFloat() + step.width() / 2,
                    step.bottom.toFloat() + 15 + mLabelsBordersList[i].height(),
                    steppedProgressBarPaint
                )
            }
        }
    }

    fun nextStep() {
        if ((currentStep + 1 <= stepsNumber + 1 && hasPostStep) ||
            currentStep + 1 <= stepsNumber
        ) {
            currentStep += 1
            remeasure()
        }
        invalidate()
        // init draw ProgressBarChange
        // init draw NewStep
    }

    fun previousStep() {
        if ((currentStep > 0 && hasPreStep) || currentStep > 1) {
            currentStep -= 1
            remeasure()
        }
        invalidate()
        // init draw ProgressBarChange
        // init draw DeactivateStep
    }
}