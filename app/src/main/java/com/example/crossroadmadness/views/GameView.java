
package com.example.crossroadmadness.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.crossroadmadness.R;

public class GameView extends View {

    // ========================================
    // POLA KLASY
    // ========================================

    // Wygląd elementów mapy
    private final Paint roadPaint;
    private final Paint linePaint;
    private final Paint crosswalkPaint;
    private final Paint stopLinePaint;

    // Grafiki sygnalizatorów
    private final Bitmap redLight;
    private final Bitmap yellowLight;
    private final Bitmap greenLight;

    public GameView(Context context) {
        super(context);

        // Asfalt
        roadPaint = new Paint();
        roadPaint.setColor(
                ContextCompat.getColor(context, R.color.road_asphalt)
        );
        roadPaint.setStyle(Paint.Style.FILL);

        // Linie przerywane rozdzielające pasy
        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(6f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setPathEffect(
                new DashPathEffect(new float[]{35f, 25f}, 0)
        );

        // Przejścia dla pieszych
        crosswalkPaint = new Paint();
        crosswalkPaint.setColor(Color.WHITE);
        crosswalkPaint.setStyle(Paint.Style.FILL);

        // Linie zatrzymania
        stopLinePaint = new Paint();
        stopLinePaint.setColor(Color.WHITE);
        stopLinePaint.setStrokeWidth(8f);
        stopLinePaint.setStyle(Paint.Style.STROKE);

        // Wczytanie grafik z folderu drawable
        redLight = BitmapFactory.decodeResource(
                getResources(), R.drawable.redlight
        );

        yellowLight = BitmapFactory.decodeResource(
                getResources(), R.drawable.yellowlight
        );

        greenLight = BitmapFactory.decodeResource(
                getResources(), R.drawable.greenlight
        );
    }


    // ========================================
    // RYSOWANIE MAPY
    // ========================================

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // Tło mapy
        canvas.drawColor(
                ContextCompat.getColor(
                        getContext(), R.color.game_background
                )
        );

        // Wymiary skrzyżowania
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float roadWidth = getWidth() * 0.35f;

        // Położenie przejść dla pieszych
        float crosswalkTop = centerY - roadWidth * 1.05f;
        float crosswalkBottom = centerY + roadWidth * 0.80f;
        float crosswalkHeight = roadWidth * 0.25f;

        // Odstępy oznakowania
        float lineGap = 8f;
        float stopDistance = roadWidth * 0.15f;

        // Granice przejść
        float rightCrosswalkLeft = centerX
                - (crosswalkTop + crosswalkHeight - centerY);

        float leftCrosswalkRight = centerX
                - (crosswalkBottom - centerY);

        // Pozycje linii zatrzymania
        float topStopY = crosswalkTop - stopDistance;
        float bottomStopY = crosswalkBottom + crosswalkHeight
                + stopDistance;

        //LINIE STOP
        float leftStopX = leftCrosswalkRight
                - crosswalkHeight - stopDistance;

        float rightStopX = rightCrosswalkLeft
                + crosswalkHeight + stopDistance;

        // Rysowanie  elementów mapy
        drawRoads(canvas, centerX, centerY, roadWidth);

        drawRoadLines(
                canvas, centerX, centerY,
                crosswalkTop, crosswalkBottom,
                crosswalkHeight, lineGap,
                leftCrosswalkRight, rightCrosswalkLeft
        );

        drawCrosswalks(
                canvas, centerX, centerY,
                crosswalkTop, crosswalkBottom, roadWidth
        );

        drawStopLines(
                canvas, centerX, centerY, roadWidth,
                topStopY, bottomStopY,
                leftStopX, rightStopX
        );

        drawTrafficLights(
                canvas, centerX, centerY, roadWidth,
                topStopY, bottomStopY,
                leftStopX, rightStopX
        );
    }


    // ========================================
    // DROGI
    // ========================================

    private void drawRoads(
            Canvas canvas,
            float centerX,
            float centerY,
            float roadWidth
    ) {
        float halfRoad = roadWidth / 2f;
        canvas.drawRect(
                centerX - halfRoad, 0,
                centerX + halfRoad, getHeight(),
                roadPaint
        );
        canvas.drawRect(
                0, centerY - halfRoad,
                getWidth(), centerY + halfRoad,
                roadPaint
        );
    }


    // ========================================
    // LINIE PRZERYWANE
    // ========================================

    private void drawRoadLines(
            Canvas canvas,
            float centerX,
            float centerY,
            float crosswalkTop,
            float crosswalkBottom,
            float crosswalkHeight,
            float lineGap,
            float leftCrosswalkRight,
            float rightCrosswalkLeft
    ) {
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
    }


    // ========================================
    // PRZEJŚCIA DLA PIESZYCH
    // ========================================

    private void drawCrosswalks(
            Canvas canvas,
            float centerX,
            float centerY,
            float crosswalkTop,
            float crosswalkBottom,
            float roadWidth
    ) {
        drawCrosswalk(canvas, centerX, crosswalkTop, roadWidth);
        drawCrosswalk(canvas, centerX, crosswalkBottom, roadWidth);
        canvas.save();
        canvas.rotate(90, centerX, centerY);

        drawCrosswalk(canvas, centerX, crosswalkTop, roadWidth);
        drawCrosswalk(canvas, centerX, crosswalkBottom, roadWidth);

        canvas.restore();
    }

    // Rysowanie pojedynczego przejścia
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

            canvas.drawRect(
                    left, top,
                    left + stripeWidth, top + crosswalkHeight,
                    crosswalkPaint
            );
        }
    }


    // ========================================
    // LINIE ZATRZYMANIA
    // ========================================

    private void drawStopLines(
            Canvas canvas,
            float centerX,
            float centerY,
            float roadWidth,
            float topStopY,
            float bottomStopY,
            float leftStopX,
            float rightStopX
    ) {
        float halfRoad = roadWidth / 2f;
        drawStopLine(
                canvas,
                centerX - halfRoad, topStopY,
                centerX, topStopY
        );
        drawStopLine(
                canvas,
                centerX, bottomStopY,
                centerX + halfRoad, bottomStopY
        );
        drawStopLine(
                canvas,
                leftStopX, centerY,
                leftStopX, centerY + halfRoad
        );
        drawStopLine(
                canvas,
                rightStopX, centerY - halfRoad,
                rightStopX, centerY
        );
    }

    // Rysowanie pojedynczej linii zatrzymania
    private void drawStopLine(
            Canvas canvas,
            float startX,
            float startY,
            float endX,
            float endY
    ) {
        canvas.drawLine(
                startX, startY,
                endX, endY,
                stopLinePaint
        );
    }


    // ========================================
    // SYGNALIZATORY ŚWIETLNE
    // ========================================

    private void drawTrafficLights(
            Canvas canvas,
            float centerX,
            float centerY,
            float roadWidth,
            float topStopY,
            float bottomStopY,
            float leftStopX,
            float rightStopX
    ) {
        // Rozmiar sygnalizatorów i odstęp od drogi
        float lightWidth = roadWidth * 0.30f;
        float lightHeight = lightWidth * 2.25f;
        float lightGap = 10f;
        float halfRoad = roadWidth / 2f;
        drawTrafficLight(
                canvas, redLight,
                centerX - halfRoad - lightWidth - lightGap,
                topStopY - lightHeight / 2f,
                lightWidth, lightHeight
        );
        drawTrafficLight(
                canvas, redLight,
                centerX + halfRoad + lightGap,
                bottomStopY - lightHeight / 2f,
                lightWidth, lightHeight
        );
        drawTrafficLight(
                canvas, redLight,
                leftStopX - lightWidth / 2f,
                centerY + halfRoad + lightGap,
                lightWidth, lightHeight
        );
        drawTrafficLight(
                canvas, redLight,
                rightStopX - lightWidth / 2f,
                centerY - halfRoad - lightHeight - lightGap,
                lightWidth, lightHeight
        );
    }
    // Rysowanie pojedynczego sygnalizatora
    private void drawTrafficLight(
            Canvas canvas,
            Bitmap bitmap,
            float x,
            float y,
            float width,
            float height
    ) {
        RectF lightRect = new RectF(
                x, y,
                x + width, y + height
        );

        canvas.drawBitmap(bitmap, null, lightRect, null);
    }
}
