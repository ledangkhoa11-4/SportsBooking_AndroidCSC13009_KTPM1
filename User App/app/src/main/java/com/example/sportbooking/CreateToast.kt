package com.example.sportbooking

import android.app.Activity
import android.content.Context
import androidx.core.content.res.ResourcesCompat
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class CreateToast {
    companion object{
        fun createToast(context: Activity, title:String, message:String, isSuccess:Boolean){
            var style: MotionToastStyle
            if(isSuccess)
                style = MotionToastStyle.SUCCESS
            else
                style = MotionToastStyle.ERROR
            MotionToast.createToast(context,
                title,
                message,
                style,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular))
        }
    }
}