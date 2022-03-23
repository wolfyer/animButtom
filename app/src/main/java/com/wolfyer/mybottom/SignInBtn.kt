package com.wolfyer.mybottom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatButton

open class SignInBtn(context: Context,attrs:AttributeSet): AppCompatButton(context,attrs) {
    private var mGradientDrawable: GradientDrawable? = null  //设置按钮背景样式
    private var paint : Paint? =null  //画笔
    private var mLeft = 0f  //按钮左边缩放动画的值
    private var mRight = 0f //按钮右边缩放动画的值
    private var start : Float = 0f //圆弧开始动画的值
    private var isdraw : Boolean = false //是否开始绘制圆弧
    private var mValueAnimator2 : ValueAnimator? = null

    init {
        initView()//设置按钮的初始样式
    }

    private fun initView() {
        //初始化画笔
        paint = Paint() // 创建画笔
        paint!!.isAntiAlias = true // 设置画笔抗锯齿（使线条平滑）
        paint!!.color = Color.parseColor("#ffffff") // 设置画笔颜色
        paint!!.strokeWidth = 4f // 设置画笔宽度
        paint!!.style = Paint.Style.STROKE // 设置画笔风格

        //初始化按钮背景样式
        mGradientDrawable = GradientDrawable()  //shape在代码中的使用方式
        mGradientDrawable!!.setColor(Color.argb(100,0,0,255)) //按钮背景颜色
        mGradientDrawable!!.cornerRadius = 130f //按钮圆角度
        background = mGradientDrawable
    }

    open fun mBtnAinim() {
        isEnabled = false //设置动画期间按钮不可点击
        text = "" //设置按钮文字为空
        val mValueAnimator = ValueAnimator.ofInt(width, height) //按钮变形动画，就是把宽度变为和高度一样
        mValueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {

            override fun onAnimationUpdate(animation: ValueAnimator) {
                val value: Int = animation.animatedValue as Int
                mLeft = (width-value)/2f
                mRight = width-(width-value)/2f
                // setBounds（left，top，right，bottom） 这四个参数值不明白可以参考view坐标系
                mGradientDrawable!!.setBounds(mLeft.toInt(),0,mRight.toInt(),height)
            }
        })
        mValueAnimator.duration = 500 //设置动画时长500毫秒
        mValueAnimator.start()
        mValueAnimator.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) { //动画结束事件的监听
                super.onAnimationEnd(animation)
                drawArc()//画圆弧 加载圆弧动画
            }
        })
    }


    private fun drawArc() {
        isdraw = true  //改变状态 这样onDraw方法就可以画出圆弧
        mValueAnimator2 = ValueAnimator.ofFloat(0f,360f)
        mValueAnimator2!!.addUpdateListener(object :ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                start = animation!!.animatedValue as Float
                invalidate() // 重绘view
            }
        })
        mValueAnimator2!!.repeatCount = ValueAnimator.INFINITE // 设置动画无限重复
        mValueAnimator2!!.duration = 500
        mValueAnimator2!!.start()
    }

    open fun endAnim(btnText:String){
        //把btn改为原来样式
        val mValueAnimator = ValueAnimator.ofInt(0,width) //原来按钮的宽度值
        mValueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator) {
                val value: Int = animation.animatedValue as Int
                mGradientDrawable!!.setBounds((width-value)/2,0,width-(width-value)/2,height)
            }
        })
        mValueAnimator.duration = 500
        mValueAnimator.start()
        mValueAnimator.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                text = btnText // 改变btn字体
                isEnabled = true //设置按钮可点击
            }
        })

        mValueAnimator2!!.end()//停止圆弧旋转
        isdraw = false // 不画弧
        invalidate() //重绘界面
    }
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (isdraw){ //如果按钮动画完成缩放变形在开始画弧，不让按钮缩放时就会画弧，很难看
            val mRectF = RectF(mLeft+10,10f,mRight-10,height-10.toFloat())
            canvas!!.drawArc(mRectF,start,160f,false,paint!!) //画圆弧
        }

    }

}