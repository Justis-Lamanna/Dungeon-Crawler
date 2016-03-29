/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysterydungeon.animation;

import java.awt.image.BufferedImage;

/**
 *
 * @author jlamanna
 */
public interface Animation
{
    boolean animate();
    BufferedImage getImage();
    int getX();
    int getY();
}
