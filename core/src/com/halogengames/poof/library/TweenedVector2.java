package com.halogengames.poof.library;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class TweenedVector2 extends Vector2 {
     private Vector2 initPos;
     private Vector2 finalPos;
     private float elapsed;
     private float runTime;
     private Interpolation interpolator;
}
