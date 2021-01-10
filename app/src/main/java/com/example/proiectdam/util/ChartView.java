package com.example.proiectdam.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.proiectdam.R;

import java.util.Map;
import java.util.Random;

public class ChartView extends View {

    private Context context;
    private Map<String, Integer> source;
    private Paint paint;
    private Random random;

    public ChartView(Context context, Map<String, Integer> source) {
        super(context);
        this.context = context;
        this.source = source;
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setColor(Color.BLACK);
        random = new Random();
    }

    @SuppressLint("StringFormatMatches")
    @Override
    protected void onDraw(Canvas canvas) {
        if(source == null || source.isEmpty()) {
            return;
        }

        //valoare maxima
        int max = 0;
        for(Integer value:source.values()){
            if(max < value) {
                max = value;
            }
        }

        float barWidth = (float)(getWidth()/source.size());

        int currentBarPosition = 0;
        for(String label:source.keySet()) {
            int value = source.get(label);
            int color = Color.argb(100, 1 + random.nextInt(254),
                    1 + random.nextInt(254),
                    1 + random.nextInt(254));
            paint.setColor(color);

            float x1 = currentBarPosition*barWidth;
            float y1 = (1-(float)(value) / max)*getWidth();
            float x2 = x1+ barWidth;
            float y2 = getHeight();
            canvas.drawRect(x1, y1, x2, y2, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize((float)(0.04*getWidth()));
            float x = (float)(currentBarPosition+0.55)*barWidth;
            float y = (float) (0.95*getHeight());
            canvas.rotate(270, x, y);
            canvas.drawText(this.context.getString(R.string.chart_legend_format, label, source.get(label)), x, y, paint);

            canvas.rotate(-270, x, y);

            currentBarPosition++;
        }
    }
}
