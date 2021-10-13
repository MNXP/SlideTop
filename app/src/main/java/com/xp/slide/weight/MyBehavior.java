package com.xp.slide.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

/**
 * Description:
 * ---------------------
 * Author: xiangpan
 * Date: 2021/9/23 11:32 上午
 */
public class MyBehavior extends AppBarLayout.Behavior {


    private boolean canMove = true;

    public MyBehavior() {

    }

    public MyBehavior(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull AppBarLayout child, @NonNull MotionEvent ev) {
        if (!canMove && ev.getAction() == MotionEvent.ACTION_DOWN){
            return false;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    public void setCanMove(boolean canMove){
        this.canMove = canMove;
    }

    public boolean isCanMove() {
        return canMove;
    }
}
