package com.bigwanggang;

import org.junit.Assert;

import java.awt.*;

public class UtilTest {
    @org.junit.Test
    public void ifHitPlane() throws Exception {
        Plane plane = new Plane(3, 3, 3, 6);
        Assert.assertTrue(Util.ifHitPlane(plane, new Point(1, 4)));
        Assert.assertTrue(Util.ifHitPlane(plane, new Point(2, 4)));
        Assert.assertTrue(Util.ifHitPlane(plane, new Point(3, 4)));
        Assert.assertTrue(Util.ifHitPlane(plane, new Point(4, 4)));
        Assert.assertTrue(Util.ifHitPlane(plane, new Point(5, 4)));
        Assert.assertFalse(Util.ifHitPlane(plane, new Point(6, 4)));
        Assert.assertFalse(Util.ifHitPlane(plane, new Point(0, 4)));
        Assert.assertTrue(Util.ifHitPlane(plane, new Point(3, 5)));
        Assert.assertFalse(Util.ifHitPlane(plane, new Point(2, 5)));
        Assert.assertFalse(Util.ifHitPlane(plane, new Point(4, 5)));
        Assert.assertTrue(Util.ifHitPlane(plane, new Point(2, 6)));
        Assert.assertTrue(Util.ifHitPlane(plane, new Point(3, 6)));
        Assert.assertTrue(Util.ifHitPlane(plane, new Point(4, 6)));
        Assert.assertFalse(Util.ifHitPlane(plane, new Point(1, 6)));
        Assert.assertFalse(Util.ifHitPlane(plane, new Point(5, 6)));
    }

    @org.junit.Test
    public void ifHitDownPlane() throws Exception {
        Plane plane = new Plane(3, 3, 3, 6);
        Assert.assertTrue(Util.ifHitDownPlane(plane, new Point(3,3)));
        Assert.assertFalse(Util.ifHitDownPlane(plane, new Point(2,3)));
        Assert.assertFalse(Util.ifHitDownPlane(plane, new Point(4,3)));
        Assert.assertFalse(Util.ifHitDownPlane(plane, new Point(3,2)));
        Assert.assertFalse(Util.ifHitDownPlane(plane, new Point(3,4)));
    }

}
