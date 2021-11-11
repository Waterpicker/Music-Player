/*
 * Minecraft Forge
 * Copyright (c) 2016-2021.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.fmlclient.gui.widget;

import net.minecraftforge.fmlclient.gui.GuiUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

/**
 * This class provides a button that fixes several bugs present in the vanilla GuiButton drawing code.
 * The gist of it is that it allows buttons of any size without gaps in the graphics and with the
 * borders drawn properly. It also prevents button text from extending out of the sides of the button by
 * trimming the end of the string and adding an ellipsis.<br/><br/>
 *
 * The code that handles drawing the button is in GuiUtils.
 *
 * @author bspkrs
 */
public class ExtendedButton extends ButtonWidget
{
    public ExtendedButton(int xPos, int yPos, int width, int height, Text displayString, ButtonWidget.PressAction handler)
    {
        super(xPos, yPos, width, height, displayString, handler);
    }

    /**
     * Draws this button to the screen.
     */
    @Override
    public void renderButton(MatrixStack mStack, int mouseX, int mouseY, float partial)
    {
        MinecraftClient mc = MinecraftClient.getInstance();
        int k = this.getYImage(this.isHovered());
        GuiUtils.drawContinuousTexturedBox(mStack, WIDGETS_TEXTURE, this.x, this.y, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.getZOffset());
        this.renderBackground(mStack, mc, mouseX, mouseY);

        Text buttonText = this.getMessage();
        int strWidth = mc.textRenderer.getWidth(buttonText);
        int ellipsisWidth = mc.textRenderer.getWidth("...");

        if (strWidth > width - 6 && strWidth > ellipsisWidth)
            //TODO, srg names make it hard to figure out how to append to an ITextProperties from this trim operation, wraping this in StringTextComponent is kinda dirty.
            buttonText = new LiteralText(mc.textRenderer.trimToWidth(buttonText, width - 6 - ellipsisWidth).getString() + "...");

        drawCenteredText(mStack, mc.textRenderer, buttonText, this.x + this.width / 2, this.y + (this.height - 8) / 2, 0xffffff); //TODO make sure right color
    }
}