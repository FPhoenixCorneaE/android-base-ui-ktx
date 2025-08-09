package com.fphoenixcorneae.baseui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.os.BatteryManager
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

/**
 * @desc：电池
 * @date：2022/11/21 17:47
 */
class BatteryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    /** 电池边框部分 */
    /** 边框颜色 */
    @ColorInt
    private var mBorderColor = 0

    /** 电池边框宽度 */
    private var mBorderWidth = 0f

    /** 边框圆角 */
    private var mBorderRadius = 0f

    /** 边框与电量的间隔 */
    private var mBorderPadding = 0f

    /** 边框矩形 */
    private val mBorderRectF by lazy { RectF() }

    /** 边框画笔 */
    private val mBorderPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = mBorderColor
            strokeWidth = mBorderWidth
        }
    }

    /** 电量的实心部分 */
    /** 低电量   默认值  0%-20%  红色 */
    @ColorInt
    private var mLowPowerColor = 0
    private var mLowPowerThreshold = 0

    /** 中等电量 默认值 21%-40%  黄色 */
    @ColorInt
    private var mMediumPowerColor = 0
    private var mMediumPowerThreshold = 0

    /** 高电量   默认值 41%-100% 白色 */
    @ColorInt
    private var mHighPowerColor = 0

    /** 当前电量 */
    private var mCurrentPower = 0

    /** 充电中电量 */
    private var mChargingPower = 0

    /** 充电时电量颜色  默认颜色为绿色 */
    @ColorInt
    private var mChargingPowerColor = 0

    /** 充电速度 */
    private var mChargingSpeed = 0

    /** 充电中的闪电图片 */
    private val mChargingLightningBitmap by lazy {
        ContextCompat.getDrawable(context, R.drawable.ic_charging_lightning)?.toBitmap()?.run {
            val matrix = Matrix()
            if (mOrientation == BatteryOrientation.HORIZONTAL_LEFT || mOrientation == BatteryOrientation.HORIZONTAL_RIGHT) {
                matrix.setRotate(90f, width / 2f, height / 2f)
            }
            Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
        }
    }

    private val mChargingLightningRectF: RectF by lazy { RectF() }

    /** 充电处理器 */
    private val mChargeHandler = Handler(Looper.getMainLooper())
    private var mChargeRunnable: Runnable? = null

    /** 电量矩形 */
    private val mPowerRectF by lazy { RectF() }

    /** 电量画笔 */
    private val mPowerPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = mHighPowerColor
            strokeWidth = 0f
        }
    }

    /** 电池头部分 */
    private var mHeaderColor = 0
    private var mHeaderWidth = 0f
    private val mHeaderRectF by lazy { RectF() }
    private val mHeaderPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = mHeaderColor
            strokeWidth = 0f
        }
    }

    /** 电池方向 */
    private var mOrientation: BatteryOrientation? = null

    /** 电量变化监听 */
    private var mOnPowerChangedListener: OnPowerChangedListener? = null

    /** 生命周期 */
    private var mLifecycle: Lifecycle? = null

    /** 电池广播接收器 */
    private val mBatteryBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 当前电量
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            // 电池满电量数值
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
            // 电池状态
            val status = intent.getIntExtra(
                BatteryManager.EXTRA_STATUS,
                BatteryManager.BATTERY_STATUS_UNKNOWN
            )
            if (status == BatteryManager.BATTERY_STATUS_CHARGING && level < scale) {
                startCharge()
            } else {
                stopCharge()
                setPower(level)
            }
            mCurrentPower = level
            mOnPowerChangedListener?.onPowerChanged(level)
        }
    }

    init {
        obtainStyledAttributes(context, attrs)
    }

    private fun obtainStyledAttributes(context: Context, attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.BatteryView) {
            // 默认水平头朝右
            mOrientation = when (getInt(R.styleable.BatteryView_batteryOrientation, 1)) {
                0 -> BatteryOrientation.HORIZONTAL_LEFT
                1 -> BatteryOrientation.HORIZONTAL_RIGHT
                2 -> BatteryOrientation.VERTICAL_TOP
                3 -> BatteryOrientation.VERTICAL_BOTTOM
                else -> throw IllegalArgumentException("Battery orientation must be one of the BatteryOrientation enum.")
            }
            // 电池内边距
            mBorderPadding =
                getDimensionPixelSize(R.styleable.BatteryView_batteryBorderPadding, 2.dp).toFloat()
            // 电池边框厚度
            mBorderWidth =
                getDimensionPixelSize(R.styleable.BatteryView_batteryBorderWidth, 2.dp).toFloat()
            // 电池边框圆角
            mBorderRadius =
                getDimensionPixelSize(R.styleable.BatteryView_batteryBorderRadius, 2.dp).toFloat()
            // 电池边框颜色
            mBorderColor = getColor(R.styleable.BatteryView_batteryBorderColor, Color.WHITE)
            // 电池实心部分
            mLowPowerColor = getColor(R.styleable.BatteryView_batteryLowPowerColor, Color.RED)
            mLowPowerThreshold = getInt(R.styleable.BatteryView_batteryLowPowerThreshold, 20)
            mMediumPowerColor = getColor(R.styleable.BatteryView_batteryMediumPowerColor, Color.YELLOW)
            mMediumPowerThreshold = getInt(R.styleable.BatteryView_batteryMediumPowerThreshold, 40)
            mHighPowerColor = getColor(R.styleable.BatteryView_batteryHighPowerColor, Color.WHITE)
            mChargingPowerColor =
                getColor(R.styleable.BatteryView_batteryChargingPowerColor, Color.GREEN)
            mCurrentPower = getInt(R.styleable.BatteryView_batteryPower, 0)
            mChargingSpeed = getInt(R.styleable.BatteryView_batteryChargingSpeed, 1)
            if (mChargingSpeed !in 1..9) {
                mChargingSpeed = 1
            }
            // 电池头
            mHeaderColor = getColor(R.styleable.BatteryView_batteryHeaderColor, Color.WHITE)
            mHeaderWidth =
                getDimensionPixelSize(R.styleable.BatteryView_batteryHeaderWidth, 2.dp).toFloat()
        }
    }

    override fun onDraw(canvas: Canvas) {
        // 绘制边框
        drawBorder(canvas)
        // 绘制电量实心区域
        drawPower(canvas)
        // 绘制电池头
        drawHeader(canvas)
    }

    /**
     * 绘制边框
     */
    private fun drawBorder(canvas: Canvas) {
        when (mOrientation) {
            BatteryOrientation.HORIZONTAL_LEFT -> {
                mBorderRectF.left = mHeaderWidth + mBorderWidth / 2
                mBorderRectF.top = mBorderWidth / 2
                mBorderRectF.right = measuredWidth - mBorderWidth / 2
                mBorderRectF.bottom = measuredHeight - mBorderWidth / 2
            }

            BatteryOrientation.HORIZONTAL_RIGHT -> {
                mBorderRectF.left = mBorderWidth / 2f
                mBorderRectF.top = mBorderWidth / 2f
                mBorderRectF.right = measuredWidth - mHeaderWidth - mBorderWidth / 2
                mBorderRectF.bottom = measuredHeight - mBorderWidth / 2
            }

            BatteryOrientation.VERTICAL_TOP -> {
                mBorderRectF.left = mBorderWidth / 2
                mBorderRectF.top = mHeaderWidth + mBorderWidth / 2
                mBorderRectF.right = measuredWidth - mBorderWidth / 2
                mBorderRectF.bottom = measuredHeight - mBorderWidth / 2
            }

            BatteryOrientation.VERTICAL_BOTTOM -> {
                mBorderRectF.left = mBorderWidth / 2
                mBorderRectF.top = mBorderWidth / 2
                mBorderRectF.right = measuredWidth - mBorderWidth / 2
                mBorderRectF.bottom = measuredHeight - mHeaderWidth - mBorderWidth / 2
            }

            else -> {}
        }
        canvas.drawRoundRect(mBorderRectF, mBorderRadius, mBorderRadius, mBorderPaint)
    }

    /**
     * 绘制电量实心区域
     */
    private fun drawPower(canvas: Canvas) {
        mPowerPaint.color = if (mChargeRunnable != null) {
            mChargingPowerColor
        } else if (mCurrentPower <= mLowPowerThreshold) {
            mLowPowerColor
        } else if (mCurrentPower <= mMediumPowerThreshold) {
            mMediumPowerColor
        } else {
            mHighPowerColor
        }
        val power = if (mChargeRunnable != null) {
            mChargingPower
        } else {
            mCurrentPower
        }
        when (mOrientation) {
            BatteryOrientation.HORIZONTAL_RIGHT -> {
                val realWidth = getHorizontalWidth(power)
                mPowerRectF.left = mBorderWidth + mBorderPadding
                mPowerRectF.top = mBorderWidth + mBorderPadding
                mPowerRectF.right = mPowerRectF.left + realWidth
                mPowerRectF.bottom = measuredHeight - mBorderWidth - mBorderPadding
            }

            BatteryOrientation.HORIZONTAL_LEFT -> {
                val realWidth = getHorizontalWidth(power)
                mPowerRectF.left = measuredWidth - mBorderWidth - mBorderPadding - realWidth
                mPowerRectF.top = mBorderWidth + mBorderPadding
                mPowerRectF.right = measuredWidth - mBorderWidth - mBorderPadding
                mPowerRectF.bottom = measuredHeight - mBorderWidth - mBorderPadding
            }

            BatteryOrientation.VERTICAL_TOP -> {
                val realHeight = getVerticalHeight(power)
                mPowerRectF.left = mBorderWidth + mBorderPadding
                mPowerRectF.top = measuredHeight - mBorderWidth - mBorderPadding - realHeight
                mPowerRectF.right = measuredWidth - mBorderWidth - mBorderPadding
                mPowerRectF.bottom = measuredHeight - mBorderWidth - mBorderPadding
            }

            BatteryOrientation.VERTICAL_BOTTOM -> {
                val realHeight = getVerticalHeight(power)
                mPowerRectF.left = mBorderWidth + mBorderPadding
                mPowerRectF.top = mBorderWidth + mBorderPadding
                mPowerRectF.right = measuredWidth - mBorderWidth - mBorderPadding
                mPowerRectF.bottom = mBorderWidth + mBorderPadding + realHeight
            }

            else -> {}
        }
        canvas.drawRoundRect(mPowerRectF, mBorderRadius, mBorderRadius, mPowerPaint)
        // 绘制充电时闪电图片
        mChargeRunnable?.let {
            mChargingLightningBitmap?.let {
                when (mOrientation) {
                    BatteryOrientation.HORIZONTAL_LEFT, BatteryOrientation.HORIZONTAL_RIGHT -> {
                        mChargingLightningRectF.left = measuredWidth * 0.3f
                        mChargingLightningRectF.top = measuredHeight * 0.2f
                        mChargingLightningRectF.right = measuredWidth * 0.7f
                        mChargingLightningRectF.bottom = measuredHeight * 0.8f
                    }

                    else -> {
                        mChargingLightningRectF.left = measuredWidth * 0.2f
                        mChargingLightningRectF.top = measuredHeight * 0.3f
                        mChargingLightningRectF.right = measuredWidth * 0.8f
                        mChargingLightningRectF.bottom = measuredHeight * 0.7f
                    }
                }
                canvas.drawBitmap(it, null, mChargingLightningRectF, mPowerPaint)
            }
        }
    }

    /**
     * 绘制电池头
     */
    private fun drawHeader(canvas: Canvas) {
        when (mOrientation) {
            BatteryOrientation.HORIZONTAL_LEFT -> {
                val headerHeight = measuredHeight / 3f
                mHeaderRectF.left = 0f
                mHeaderRectF.top = headerHeight
                mHeaderRectF.right = mHeaderWidth
                mHeaderRectF.bottom = headerHeight * 2
            }

            BatteryOrientation.HORIZONTAL_RIGHT -> {
                val headerHeight = measuredHeight / 3f
                mHeaderRectF.left = measuredWidth - mHeaderWidth
                mHeaderRectF.top = headerHeight
                mHeaderRectF.right = measuredWidth.toFloat()
                mHeaderRectF.bottom = headerHeight * 2
            }

            BatteryOrientation.VERTICAL_TOP -> {
                val headerWidth = measuredWidth / 3f
                mHeaderRectF.left = headerWidth
                mHeaderRectF.top = 0f
                mHeaderRectF.right = headerWidth * 2
                mHeaderRectF.bottom = mHeaderWidth
            }

            BatteryOrientation.VERTICAL_BOTTOM -> {
                val headerWidth = measuredWidth / 3f
                mHeaderRectF.left = headerWidth
                mHeaderRectF.top = measuredHeight - mHeaderWidth
                mHeaderRectF.right = headerWidth * 2
                mHeaderRectF.bottom = measuredHeight.toFloat()
            }

            else -> Unit
        }
        canvas.drawRoundRect(mHeaderRectF, mBorderRadius, mBorderRadius, mHeaderPaint)
    }

    /**
     * 电池电量横向的宽度
     */
    private fun getHorizontalWidth(power: Int): Float {
        // 满电量宽度
        val fullWidth = measuredWidth - mBorderWidth * 2 - mBorderPadding * 2 - mHeaderWidth
        return fullWidth * power / 100f
    }

    /**
     * 电池电量纵向的宽度
     */
    private fun getVerticalHeight(power: Int): Float {
        // 满电量高度
        val fullHeight = measuredHeight - mBorderWidth * 2 - mBorderPadding * 2 - mHeaderWidth
        return fullHeight * power / 100f
    }

    override fun onResume(owner: LifecycleOwner) {
        context.registerReceiver(mBatteryBroadcastReceiver, IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
        })
    }

    override fun onPause(owner: LifecycleOwner) {
        context.unregisterReceiver(mBatteryBroadcastReceiver)
    }

    /**
     * 充电动态显示
     */
    private fun startCharge() {
        if (mChargeRunnable != null) {
            return
        }
        mChargingPower = mCurrentPower
        mChargeRunnable = object : Runnable {
            override fun run() {
                mChargingPower %= 100
                setPower(mChargingPower)
                mChargingPower += mChargingSpeed
                //延迟执行
                mChargeHandler.postDelayed(this, 200)
            }
        }
        mChargeHandler.post(mChargeRunnable!!)
    }

    /**
     * 停止充电
     */
    private fun stopCharge() {
        mChargeRunnable?.let {
            mChargeHandler.removeCallbacks(it)
            mChargeRunnable = null
        }
    }

    /**
     * Float 类型： dp 转换为 px
     */
    private val Int.dp: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        ).toInt()

    /**
     * 设置电量
     */
    fun setPower(power: Int) {
        mCurrentPower = power
        invalidate()
    }

    /**
     * 充电动画速度
     *
     * @param speed 1-9之间的数值
     */
    fun setChargingSpeed(@IntRange(from = 1, to = 9) speed: Int) {
        mChargingSpeed = speed
    }

    fun setLifecycleOwner(owner: LifecycleOwner?) {
        mLifecycle?.removeObserver(this)
        mLifecycle = owner?.lifecycle
        mLifecycle?.addObserver(this)
    }

    fun setOnPowerChangedListener(onPowerChangedListener: OnPowerChangedListener?) {
        mOnPowerChangedListener = onPowerChangedListener
    }

    fun removeOnBatteryPowerListener() {
        mOnPowerChangedListener = null
    }

    /**
     * @desc：电量改变监听
     * @date：2022/11/22 10:13
     */
    interface OnPowerChangedListener {
        fun onPowerChanged(power: Int)
    }

    /**
     * @desc：电池方向
     * @date：2022/11/22 10:12
     */
    enum class BatteryOrientation {
        /** 电池头朝左 */
        HORIZONTAL_LEFT,

        /** 电池头朝右 */
        HORIZONTAL_RIGHT,

        /** 电池头朝上 */
        VERTICAL_TOP,

        /** 电池头朝下 */
        VERTICAL_BOTTOM
    }
}

