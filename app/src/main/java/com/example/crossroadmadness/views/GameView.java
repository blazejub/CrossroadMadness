
package com.example.crossroadmadness.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.crossroadmadness.R;

public class GameView extends View {

    private Paint roadPaint;
    private Paint linePaint;
    private Paint crosswalkPaint;

    public GameView(Context context) {
        super(context);

        // Kolor asfaltu
        roadPaint = new Paint();
        roadPaint.setColor(ContextCompat.getColor(context, R.color.road_asphalt));
        roadPaint.setStyle(Paint.Style.FILL);

        // Przerywane linie na jezdni
        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(6f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setPathEffect(new DashPathEffect(
                new float[]{35f, 25f}, 0
        ));

        // Białe pasy przejść dla pieszych
        crosswalkPaint = new Paint();
        crosswalkPaint.setColor(Color.WHITE);
        crosswalkPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Tło
        canvas.drawColor(
                ContextCompat.getColor(getContext(), R.color.game_background)
        );

        // Wymiary skrzyżowania
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float roadWidth = getWidth() * 0.35f;

        // Droga pionowa
        canvas.drawRect(
                centerX - roadWidth / 2f,
                0,
                centerX + roadWidth / 2f,
                getHeight(),
                roadPaint
        );

        // Droga pozioma
        canvas.drawRect(
                0,
                centerY - roadWidth / 2f,
                getWidth(),
                centerY + roadWidth / 2f,
                roadPaint
        );

        // Położenie górnego i dolnego przejścia
        float crosswalkTop = centerY - roadWidth / 2f
                - roadWidth * 0.55f;

        float crosswalkBottom = centerY + roadWidth / 2f
                + roadWidth * 0.30f;

        float crosswalkHeight = roadWidth * 0.25f;
        float lineGap = 8f;

        // Linie przerywane na drodze pionowej
        canvas.drawLine(
                centerX, 0,
                centerX, crosswalkTop - lineGap,
                linePaint
        );

        canvas.drawLine(
                centerX, crosswalkBottom + crosswalkHeight + lineGap,
                centerX, getHeight(),
                linePaint
        );

        // Granice przejść na drodze poziomej.
        // Po obrocie o 90 stopni:
        // górna zebra trafia na prawo, dolna na lewo.
        float rightCrosswalkLeft = centerX
                - (crosswalkTop + crosswalkHeight - centerY);

        float leftCrosswalkRight = centerX
                - (crosswalkBottom - centerY);

        // Linie przerywane na drodze poziomej
        canvas.drawLine(
                0, centerY,
                leftCrosswalkRight - lineGap, centerY,
                linePaint
        );

        canvas.drawLine(
                rightCrosswalkLeft + lineGap, centerY,
                getWidth(), centerY,
                linePaint
        );

        // Górne i dolne przejście
        drawCrosswalk(canvas, centerX, crosswalkTop, roadWidth);
        drawCrosswalk(canvas, centerX, crosswalkBottom, roadWidth);

        // Lewe i prawe przejście
        canvas.save();
        canvas.rotate(90, centerX, centerY);

        drawCrosswalk(canvas, centerX, crosswalkTop, roadWidth);
        drawCrosswalk(canvas, centerX, crosswalkBottom, roadWidth);

        canvas.restore();
    }

    // Metoda rysująca przejście dla pieszych
    private void drawCrosswalk(
            Canvas canvas,
            float centerX,
            float top,
            float roadWidth
    ) {
        int stripeCount = 8;

        float stripeWidth = roadWidth / (stripeCount * 2f);
        float crosswalkHeight = roadWidth * 0.25f;
        float roadLeft = centerX - roadWidth / 2f;

        for (int i = 0; i < stripeCount; i++) {
            float left = roadLeft + i * 2f * stripeWidth;
            float right = left + stripeWidth;

            canvas.drawRect(
                    left,
                    top,
                    right,
                    top + crosswalkHeight,
                    crosswalkPaint
            );
        }
    }
}
