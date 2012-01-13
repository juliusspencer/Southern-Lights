package org.gtugs.auckland.widget;

/*
Copyright (c) 2011, Damian Kolakowski
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. All advertising materials mentioning features or use of this software
   must display the following acknowledgement:
   This product includes software developed by the Damian Kolakowski.
4. Neither the name of the Damian Kolakowski nor the
   names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Damian Kolakowski ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Damian Kolakowski BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.HashMap;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class DJDispatcher {

	private final HashMap<Integer, Integer> mPointerToViewMap = new HashMap<Integer, Integer>();
	private final Rect mTmpRect = new Rect();

	private MotionEvent convertEvent(View targetView, MotionEvent eventToConvert, int pointerId, int newAction) {
		final MotionEvent newEvent = MotionEvent.obtain(eventToConvert);
		newEvent.setAction(newAction);
		Log.i("SL",String.valueOf(eventToConvert.findPointerIndex(pointerId)));
//						Log.i("SL", pointerId+", "+eventToConvert.getX(pointerId)+", "+targetView.getLeft()+", "+eventToConvert.getY(pointerId)+", "+targetView.getTop());
//						Log.i("SL", String.valueOf(eventToConvert.getX(pointerId)-targetView.getLeft())+", "+String.valueOf(eventToConvert.getY(pointerId)-targetView.getTop()));
			newEvent.setLocation(eventToConvert.getX(eventToConvert.findPointerIndex(pointerId))-targetView.getLeft(), eventToConvert.getY(eventToConvert.findPointerIndex(pointerId))-targetView.getTop());
		return newEvent;
	}

	public boolean dispatchTouchEvent(ViewGroup group, MotionEvent ev) {    
		boolean returnValue = false;
		if ( group == null || ev == null || ev.getPointerCount() <= 0 ) return false;
		final int childCount    = group.getChildCount();
		final int pointerCount  = ev.getPointerCount();
		final int actionCode    = ev.getAction() & MotionEvent.ACTION_MASK;
		final int pointerId     = ev.getPointerId(ev.getAction() >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
		switch ( actionCode ) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			for ( int j = 0 ; j < childCount ; ++j ) {
				final View child = group.getChildAt(j);
				child.getHitRect(mTmpRect);
				if(pointerId < ev.getPointerCount()) {
					if ( mTmpRect.contains((int)ev.getX(pointerId), (int)ev.getY(pointerId)))  {
						mPointerToViewMap.put(pointerId, j);
						returnValue = child.dispatchTouchEvent(convertEvent(child, ev, pointerId, MotionEvent.ACTION_DOWN));
						break;
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			if ( mPointerToViewMap.containsKey(pointerId)) {
				final int childIndex = mPointerToViewMap.get(pointerId);
				if ( childIndex < childCount ) {
					final View tragetView = group.getChildAt(childIndex);
					final MotionEvent event = convertEvent(tragetView, ev, pointerId, MotionEvent.ACTION_UP);
					returnValue = tragetView.dispatchTouchEvent(event);
					if ( returnValue ) mPointerToViewMap.remove(pointerId);
				} else {
					mPointerToViewMap.remove(pointerId);
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			for ( int i = 0 ; i < pointerCount ; ++i ) {
				final int pointerIdForMove = ev.getPointerId(i);
				if ( mPointerToViewMap.containsKey(pointerIdForMove) ) {
					final int childIndex = mPointerToViewMap.get(pointerIdForMove);
					if ( childIndex < childCount ) {
						final View tragetView = group.getChildAt(childIndex);
						final MotionEvent event = convertEvent(tragetView, ev, pointerIdForMove, MotionEvent.ACTION_MOVE);
						if ( tragetView.dispatchTouchEvent(event) ) returnValue = true;
					} else {
						mPointerToViewMap.remove(pointerId);
					}
				}
			}
			break;
		}
		return returnValue;
	}
}