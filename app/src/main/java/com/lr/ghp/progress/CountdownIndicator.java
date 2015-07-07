/*
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lr.ghp.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CountdownIndicator extends View {
  private final Paint mRemainingSectorPaint;
  private final Paint mBorderPaint;
  private final Paint mWholeBgPaint;
  private boolean isDrawGray;
  private static final int DEFAULT_COLOR = 0xff3060c0;
  private static final int BLUE_COLOR=0xff009cff;

  private final int mBorderPaintW=18;
  private final int mRemainingSectorPaintW=4;
  private final int reduceW=mBorderPaintW/2+4;
  /**
   * Countdown phase starting with {@code 1} when a full cycle is remaining and shrinking to
   * {@code 0} the closer the countdown is to zero.
   */
  private double mPhase;

  public void setDrawGray(boolean bool){
      isDrawGray=bool;
  }

  public CountdownIndicator(Context context) {
    this(context, null);
  }

  public CountdownIndicator(Context context, AttributeSet attrs) {
    super(context, attrs);

    mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mBorderPaint.setStrokeWidth(mBorderPaintW); // hairline
    mBorderPaint.setStyle(Style.STROKE);
    mBorderPaint.setColor(BLUE_COLOR);
    mBorderPaint.setAntiAlias(true);

    mRemainingSectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mRemainingSectorPaint.setColor(0xffeeeeee);
    mRemainingSectorPaint.setStrokeWidth(mRemainingSectorPaintW); // hairline
    mRemainingSectorPaint.setStyle(Style.STROKE);
    mRemainingSectorPaint.setAntiAlias(true);

    mWholeBgPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
    mWholeBgPaint.setColor(0xffffffff);
    mWholeBgPaint.setAntiAlias(true);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    float remainingSectorSweepAngle = (float) (-mPhase * 360);
    float remainingSectorStartAngle = 270-remainingSectorSweepAngle;
    

    // Draw the sector/filled arc
    // We need to leave the leftmost column and the topmost row out of the drawingRect because
    // in anti-aliased mode drawArc and drawOval use these areas for some reason.
    RectF drawingRect = new RectF(reduceW, reduceW, getWidth()-reduceW, getWidth()-reduceW);
    RectF whiteRect=new RectF(reduceW, reduceW, getWidth()-reduceW, getWidth()-reduceW);
    if (remainingSectorStartAngle < (360-remainingSectorSweepAngle)) {
        if(isDrawGray){
        canvas.drawOval(drawingRect, mRemainingSectorPaint);
        canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() / 2 - reduceW, mRemainingSectorPaint);
        }
      canvas.drawArc(
    		  whiteRect,
              remainingSectorStartAngle,
              remainingSectorSweepAngle,
    		  false,
    		  mBorderPaint);
    } else {
      // 360 degrees is equivalent to 0 degrees for drawArc, hence the drawOval below.
//      canvas.drawOval(drawingRect, mRemainingSectorPaint);
//      canvas.drawCircle(this.getWidth()/2, this.getHeight()/2, this.getWidth()/2, mRemainingSectorPaint);
    }

    // Internal filling
//    canvas.drawOval(drawingRect, mBorderPaint);
//    canvas.drawCircle(this.getWidth()/2, this.getHeight()/2, this.getWidth()/2-reduceW, mWholeBgPaint);
  }

  /**
   * Sets the phase of this indicator.
   *
   * @param phase phase {@code [0, 1]}: {@code 1} when the maximum amount of time is remaining,
   *        {@code 0} when no time is remaining.
   */
  public void setPhase(double phase) {
    if ((phase < 0) || (phase > 1)) {
      throw new IllegalArgumentException("phase: " + phase);
    }

    mPhase = phase;
    invalidate();
  }
  public void setPaintColor(int color){
	  mBorderPaint.setColor(color);

  }
  public void setRemainingSectorPaintColor(int color){
      mRemainingSectorPaint.setColor(color);
  }
  public void setWhitePaintColor(int color){
    mWholeBgPaint.setColor(color);
  }
}
