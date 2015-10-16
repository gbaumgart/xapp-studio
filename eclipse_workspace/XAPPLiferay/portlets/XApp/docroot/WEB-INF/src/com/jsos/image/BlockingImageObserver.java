package com.jsos.image;

import java.awt.Image;
import java.awt.image.ImageObserver;

public class BlockingImageObserver
  implements ImageObserver
{
  protected int flags = 0;
  protected Object waiter = new Object();

  public boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this.flags = paramInt1;
    return isStillWorking();
  }

  public boolean isLoaded()
  {
    return (this.flags & 0x20) == 32;
  }

  public boolean isError()
  {
    return ((this.flags & 0x40) == 64) || ((this.flags & 0x80) == 128);
  }

  public boolean isStillWorking()
  {
    return (!isLoaded()) && (!isError());
  }

  public void block()
  {
    while (isStillWorking())
      synchronized (this.waiter)
      {
        try
        {
          wait(200L);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
  }
}

/* Location:           /ProjectsMain/nativeIPhone/IbizaTravel/libs/imagescalePackage.jar
 * Qualified Name:     com.jsos.image.BlockingImageObserver
 * JD-Core Version:    0.6.0
 */