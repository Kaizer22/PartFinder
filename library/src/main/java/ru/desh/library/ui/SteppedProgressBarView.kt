package ru.desh.library.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import ru.desh.library.R

class SteppedProgressBarView: View {
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
        initAttributes(attributeSet)
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int):
            super(context, attributeSet, defStyleAttr) {
            initAttributes(attributeSet)
            }

    private fun initAttributes(attributeSet: AttributeSet) {
        val typedArray = context.theme
            .obtainStyledAttributes(attributeSet, R.styleable.SteppedProgressBarView, 0, 0)

        progressDrawableId = typedArray.getResourceId(R.styleable.SteppedProgressBarView_progressDrawable,
            android.R.color.transparent)
        progressFillDrawableId = typedArray.getResourceId(R.styleable.SteppedProgressBarView_progressFillDrawable,
            android.R.color.transparent)
        notAchievedStepDrawableId = typedArray.getResourceId(R.styleable.SteppedProgressBarView_notAchievedStepDrawable,
            android.R.color.transparent)
        achievedStepDrawableId = typedArray.getResourceId(R.styleable.SteppedProgressBarView_achievedStepDrawable,
            android.R.color.transparent)
        hasPreStep = typedArray.getBoolean(R.styleable.SteppedProgressBarView_hasPreStep,
            true)
        hasPostStep = typedArray.getBoolean(R.styleable.SteppedProgressBarView_hasPostStep,
            true)
        currentStep = typedArray.getInt(R.styleable.SteppedProgressBarView_defaultStep,
        0)

        progressDrawable = ResourcesCompat.getDrawable(resources, progressDrawableId, null)!!
        progressFillDrawable = ResourcesCompat.getDrawable(resources, progressFillDrawableId, null)!!
        notAchievedStepDrawable = ResourcesCompat.getDrawable(resources, notAchievedStepDrawableId, null)!!
        achievedStepDrawable = ResourcesCompat.getDrawable(resources, achievedStepDrawableId, null)!!

        val stepsAttr = typedArray.getTextArray(R.styleable.SteppedProgressBarView_android_entries)
        if (stepsAttr != null) {
            setSteps(stepsAttr.toList().map { it.toString() })
        }

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


    private var mStepsList = mutableListOf<String>()
    private var mStepsBordersList = mutableListOf<Rect>()
    private lateinit var mProgressBarBorders: Rect
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
        mStepsBordersList = mStepsList.map { Rect() }.toMutableList()
        remeasure()
    }

    private var currentStep = 0

    private var viewWidth = 0
    private var viewHeight = 0

    //private var stepHeight = 20
    //private var stepWidth = 20
    private var progressHeight = 10

    private var hasPreStep = true
    private var hasPostStep = true

    var onStart = true
        private set
    var finished = false
        private set

    private val steppedProgressBarPaint = Paint()

    // onStartProgressAnimator
    // onProgressBarChangeAnimator
    // onActivateStepAnimator
    // onDeactivateStepAnimator
    // onFinishProgressAnimator

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
        remeasure()
    }

    private fun remeasure() {
        mProgressBarBorders.set(0, viewHeight / 2 - progressHeight / 2,
            viewWidth, viewHeight / 2 + progressHeight / 2)
        val stepStartEndDistance = viewWidth / (stepsNumber + 1) / 2
        val stepsIndent = (viewWidth - 2 * stepStartEndDistance) / (stepsNumber - 1)
        for (i in 0 until mStepsBordersList.size) {
            mStepsBordersList[i].set(stepStartEndDistance + i * stepsIndent - viewHeight / 2,0,
                stepStartEndDistance + i * stepsIndent + viewHeight / 2, viewHeight)
        }
        mProgressBarBorders.set(0, viewHeight / 2 - progressHeight / 2,
            viewWidth, viewHeight / 2 + progressHeight / 2)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            for (step in mStepsBordersList) {
                notAchievedStepDrawable.bounds = step
                notAchievedStepDrawable.draw(canvas)
            }
            progressDrawable.bounds = mProgressBarBorders
            progressDrawable.draw(canvas)
        }
    }

    fun nextStep(){
        if (currentStep < stepsNumber) currentStep += 1
        // init draw ProgressBarChange
        // init draw NewStep
    }

    fun previousStep() {
        if (currentStep > 0) currentStep -= 1
        // init draw ProgressBarChange
        // init draw DeactivateStep
    }
}