package com.example.crossroadmadness.views;

import android.content.Context;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;


public class GameView extends View {

    public GameView(Context context) {
        super(context);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.GREEN);
    }

}