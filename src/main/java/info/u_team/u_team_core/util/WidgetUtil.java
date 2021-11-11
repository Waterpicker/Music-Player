package info.u_team.u_team_core.util;

import java.util.List;

import info.u_team.u_team_core.api.gui.BackgroundColorProvider;
import info.u_team.u_team_core.api.gui.PerspectiveRenderable;
import info.u_team.u_team_core.api.gui.ScaleProvider;
import info.u_team.u_team_core.api.gui.TextProvider;
import info.u_team.u_team_core.api.gui.TextureProvider;
import info.u_team.u_team_core.api.gui.TooltipRenderable;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class WidgetUtil {
	
	public static boolean isHovered(ClickableWidget widget) {
		return widget.isHovered();
	}
	
	public static <T extends ClickableWidget & PerspectiveRenderable & BackgroundColorProvider> void renderButtonLikeWidget(T widget, TextureProvider textureProvider, MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		RenderUtil.drawContinuousTexturedBox(poseStack, widget.x, widget.y, textureProvider.getU(), textureProvider.getV(), widget.getWidth(), widget.getHeight(), textureProvider.getWidth(), textureProvider.getHeight(), 2, 3, 2, 2, widget.getZOffset(), textureProvider.getTexture(), widget.getCurrentBackgroundColor(poseStack, mouseY, mouseY, partialTicks));
		widget.renderBackground(poseStack, mouseX, mouseY, partialTicks);
		widget.renderForeground(poseStack, mouseX, mouseY, partialTicks);
	}
	
	public static <T extends ClickableWidget & TextProvider> void renderText(T widget, MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		final var font = widget.getCurrentTextFont();
		
		var message = widget.getCurrentText();
		if (message != Text.EMPTY) {
			final var messageWidth = font.getWidth(message);
			final var ellipsisWidth = font.getWidth("...");
			
			if (messageWidth > widget.getWidth() - 6 && messageWidth > ellipsisWidth) {
				message = new LiteralText(font.trimToWidth(message, widget.getWidth() - 6 - ellipsisWidth).getString() + "...");
			}
			
			final float xStart = (widget.x + (widget.getHeight() / 2) - messageWidth / 2);
			final float yStart = (widget.y + (widget.getHeight() - 8) / 2);
			
			font.drawWithShadow(poseStack, message, xStart, yStart, widget.getCurrentTextColor(poseStack, mouseX, mouseY, partialTicks).getColorARGB());
		}
	}
	
	public static <T extends ClickableWidget & TextProvider & ScaleProvider> void renderScaledText(T widget, MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		final var scale = widget.getCurrentScale(poseStack, mouseX, mouseY, partialTicks);
		
		if (scale == 1) {
			renderText(widget, poseStack, mouseX, mouseY, partialTicks);
		} else {
			final var font = widget.getCurrentTextFont();
			
			var message = widget.getCurrentText();
			if (message != Text.EMPTY) {
				final var messageWidth = MathHelper.ceil(scale * font.getWidth(message));
				final var ellipsisWidth = MathHelper.ceil(scale * font.getWidth("..."));
				
				if (messageWidth > widget.getHeight() - 6 && messageWidth > ellipsisWidth) {
					message = new LiteralText(font.trimToWidth(message, widget.getWidth() - 6 - ellipsisWidth).getString() + "...");
				}
				
				final var positionFactor = 1 / scale;
				
				final var xStart = (widget.x + (widget.getWidth() / 2) - messageWidth / 2) * positionFactor;
				final var yStart = (widget.y + ((int) (widget.getHeight() - 8 * scale)) / 2) * positionFactor;
				
				poseStack.push();
				poseStack.scale(scale, scale, 0);
				font.drawWithShadow(poseStack, message, xStart, yStart, widget.getCurrentTextColor(poseStack, mouseX, mouseY, partialTicks).getColorARGB());
				poseStack.pop();
			}
		}
	}
	
	public static void renderTooltips(List<DrawableHelper> widgets, MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		widgets.forEach(widget -> {
			if (widget instanceof TooltipRenderable tooltipRenderable) {
				tooltipRenderable.renderToolTip(poseStack, mouseX, mouseY, partialTicks);
			}
		});
	}
}