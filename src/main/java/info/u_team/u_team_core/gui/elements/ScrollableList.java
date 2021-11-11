package info.u_team.u_team_core.gui.elements;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import info.u_team.u_team_core.util.RGBA;
import info.u_team.u_team_core.util.RenderUtil;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public abstract class ScrollableList<T extends AlwaysSelectedEntryListWidget.Entry<T>> extends AlwaysSelectedEntryListWidget<T> {
	
	protected int sideDistance;
	
	protected boolean shouldUseScissor;
	protected boolean renderTransparentBorder;
	protected float transparentBorderSize;
	
	public ScrollableList(int x, int y, int width, int height, int slotHeight, int sideDistance) {
		super(MinecraftClient.getInstance(), 0, 0, 0, 0, slotHeight);
		updateSettings(x, y, width, height);
		this.sideDistance = sideDistance;
		transparentBorderSize = 4;
	}
	
	public ScrollableList(int width, int height, int top, int bottom, int left, int right, int slotHeight, int sideDistance) {
		super(MinecraftClient.getInstance(), 0, 0, 0, 0, slotHeight);
		updateSettings(width, height, top, bottom, left, right);
		this.sideDistance = sideDistance;
		transparentBorderSize = 4;
	}
	
	public void updateSettings(int x, int y, int width, int height) {
		updateSettings(width, client.getWindow().getScaledHeight(), y, y + height, x, x + width);
	}
	
	public void updateSettings(int width, int height, int top, int bottom, int left, int right) {
		this.width = width;
		this.height = height;
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}
	
	public int getSideDistance() {
		return sideDistance;
	}
	
	public void setSideDistance(int sideDistance) {
		this.sideDistance = sideDistance;
	}
	
	public boolean isShouldUseScissor() {
		return shouldUseScissor;
	}
	
	public void setShouldUseScissor(boolean shouldUseScissor) {
		this.shouldUseScissor = shouldUseScissor;
	}
	
	public boolean isRenderTransparentBorder() {
		return renderTransparentBorder;
	}
	
	public void setRenderTransparentBorder(boolean renderTransparentBorder) {
		this.renderTransparentBorder = renderTransparentBorder;
	}
	
	public float getTransparentBorderSize() {
		return transparentBorderSize;
	}
	
	public void setTransparentBorderSize(float transparentBorderSize) {
		this.transparentBorderSize = transparentBorderSize;
	}
	
	@Override
	public int getRowWidth() {
		return width - sideDistance;
	}
	
	@Override
	protected int getScrollbarPositionX() {
		return left + width - 5;
	}
	
	@Override
	protected void renderList(MatrixStack poseStack, int rowLeft, int scrollAmount, int mouseX, int mouseY, float partialTicks) {
		if (shouldUseScissor) {
			final Window window = client.getWindow();
			final double scaleFactor = window.getScaleFactor();
			
			final int nativeX = MathHelper.ceil(left * scaleFactor);
			final int nativeY = MathHelper.ceil(top * scaleFactor);
			
			final int nativeWidth = MathHelper.ceil((right - left) * scaleFactor);
			final int nativeHeight = MathHelper.ceil((bottom - top) * scaleFactor);
			
			RenderSystem.enableScissor(nativeX, window.getHeight() - (nativeY + nativeHeight), nativeWidth, nativeHeight);
			
			// Uncomment to test scissor
			// poseStack.pushPose();
			// poseStack.last().pose().setIdentity();
			// GuiComponent.fill(poseStack, 0, 0, window.getGuiScaledWidth(), window.getGuiScaledHeight(), 0x8F00FF00);
			// poseStack.popPose();
			
			super.renderList(poseStack, rowLeft, scrollAmount, mouseX, mouseY, partialTicks);
			
			RenderSystem.disableScissor();
		} else {
			super.renderList(poseStack, rowLeft, scrollAmount, mouseX, mouseY, partialTicks);
		}
		
		if (renderTransparentBorder) {
			final Tessellator tessellator = Tessellator.getInstance();
			final BufferBuilder buffer = tessellator.getBuffer();
			
			RenderSystem.setShader(GameRenderer::getPositionColorShader);
			RenderUtil.setShaderColor(RGBA.BLACK);
			
			RenderSystem.enableBlend();
			RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
			RenderSystem.disableTexture();
			
			buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
			buffer.vertex(left, top + transparentBorderSize, 0).color(0, 0, 0, 0).next();
			buffer.vertex(right, top + transparentBorderSize, 0).color(0, 0, 0, 0).next();
			buffer.vertex(right, top, 0).color(0, 0, 0, 255).next();
			buffer.vertex(left, top, 0).color(0, 0, 0, 255).next();
			buffer.vertex(left, bottom, 0).color(0, 0, 0, 255).next();
			buffer.vertex(right, bottom, 0).color(0, 0, 0, 255).next();
			buffer.vertex(right, bottom - transparentBorderSize, 0).color(0, 0, 0, 0).next();
			buffer.vertex(left, bottom - transparentBorderSize, 0).color(0, 0, 0, 0).next();
			
			tessellator.draw();
			
			RenderSystem.enableTexture();
			RenderSystem.disableBlend();
		}
	}
}
