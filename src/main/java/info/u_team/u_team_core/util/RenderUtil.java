package info.u_team.u_team_core.util;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

/**
 * Utility methods for rendering
 *
 * @author HyCraftHD
 */
public class RenderUtil {
	
	public static final RGBA DARK_CONTAINER_BORDER_COLOR = new RGBA(0x373737FF);
	public static final RGBA MEDIUM_CONTAINER_BORDER_COLOR = new RGBA(0x8B8B8BFF);
	public static final RGBA BRIGHT_CONTAINER_BORDER_COLOR = RGBA.WHITE;
	
	/**
	 * Draws the default container border
	 *
	 * @param poseStack Pose stack
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Width
	 * @param height Height
	 * @param blitOffset zLevel for drawing
	 * @param color The shader color. If using {@link RGBA#WHITE} then the drawing will not be colored
	 */
	public static void drawContainerBorder(MatrixStack poseStack, int x, int y, int width, int height, float blitOffset, RGBA color) {
		final var tessellator = Tessellator.getInstance();
		final var bufferBuilder = tessellator.getBuffer();
		
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		setShaderColor(color);
		
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableTexture();
		
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		
		addColoredQuad(bufferBuilder, poseStack, x, x + width - 1, y, y + 1, DARK_CONTAINER_BORDER_COLOR, blitOffset);
		addColoredQuad(bufferBuilder, poseStack, x, x + 1, y, y + height - 1, DARK_CONTAINER_BORDER_COLOR, blitOffset);
		
		addColoredQuad(bufferBuilder, poseStack, x + width - 1, x + width, y, y + 1, MEDIUM_CONTAINER_BORDER_COLOR, blitOffset);
		addColoredQuad(bufferBuilder, poseStack, x, x + 1, y + height - 1, y + height, MEDIUM_CONTAINER_BORDER_COLOR, blitOffset);
		
		addColoredQuad(bufferBuilder, poseStack, x + 1, x + width - 1, y + height - 1, y + height, BRIGHT_CONTAINER_BORDER_COLOR, blitOffset);
		addColoredQuad(bufferBuilder, poseStack, x + width - 1, x + width, y + 1, y + height, BRIGHT_CONTAINER_BORDER_COLOR, blitOffset);
		
		tessellator.draw();
		
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
	}
	
	/**
	 * Draws a textured box of any size (smallest size is borderSize * 2 square) based on a fixed size textured box with
	 * continuous borders and filler.
	 *
	 * @param poseStack Pose stack
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param u U coordinate
	 * @param v V coordinate
	 * @param width Width
	 * @param height Height
	 * @param textureWidth Texture width
	 * @param textureHeight Texture height
	 * @param topBorder Top border
	 * @param bottomBorder Bottom border
	 * @param leftBorder Left border
	 * @param rightBorder Right border
	 * @param blitOffset zLevel for drawing
	 * @param texture Texture location
	 * @param color The shader color. If using {@link RGBA#WHITE} then the image will not be colored
	 */
	public static void drawContinuousTexturedBox(MatrixStack poseStack, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int topBorder, int bottomBorder, int leftBorder, int rightBorder, float blitOffset, Identifier texture, RGBA color) {
		final var fillerWidth = textureWidth - leftBorder - rightBorder;
		final var fillerHeight = textureHeight - topBorder - bottomBorder;
		final var canvasWidth = width - leftBorder - rightBorder;
		final var canvasHeight = height - topBorder - bottomBorder;
		final var xPasses = canvasWidth / fillerWidth;
		final var remainderWidth = canvasWidth % fillerWidth;
		final var yPasses = canvasHeight / fillerHeight;
		final var remainderHeight = canvasHeight % fillerHeight;
		
		final var uScale = 1f / 256;
		final var vScale = 1f / 256;
		
		final var tessellator = Tessellator.getInstance();
		final var bufferBuilder = tessellator.getBuffer();
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, texture);
		setShaderColor(color);
		
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		
		// Draw Border
		// Top Left
		addTexturedRect(bufferBuilder, poseStack, x, y, u, v, uScale, vScale, leftBorder, topBorder, blitOffset);
		// Top Right
		addTexturedRect(bufferBuilder, poseStack, x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, uScale, vScale, rightBorder, topBorder, blitOffset);
		// Bottom Left
		addTexturedRect(bufferBuilder, poseStack, x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, uScale, vScale, leftBorder, bottomBorder, blitOffset);
		// Bottom Right
		addTexturedRect(bufferBuilder, poseStack, x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, uScale, vScale, rightBorder, bottomBorder, blitOffset);
		
		for (var index = 0; index < xPasses + (remainderWidth > 0 ? 1 : 0); index++) {
			// Top Border
			addTexturedRect(bufferBuilder, poseStack, x + leftBorder + (index * fillerWidth), y, u + leftBorder, v, uScale, vScale, (index == xPasses ? remainderWidth : fillerWidth), topBorder, blitOffset);
			// Bottom Border
			addTexturedRect(bufferBuilder, poseStack, x + leftBorder + (index * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, uScale, vScale, (index == xPasses ? remainderWidth : fillerWidth), bottomBorder, blitOffset);
			
			// Throw in some filler for good measure
			for (var j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
				addTexturedRect(bufferBuilder, poseStack, x + leftBorder + (index * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, uScale, vScale, (index == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight), blitOffset);
			}
		}
		
		// Side Borders
		for (var index = 0; index < yPasses + (remainderHeight > 0 ? 1 : 0); index++) {
			// Left Border
			addTexturedRect(bufferBuilder, poseStack, x, y + topBorder + (index * fillerHeight), u, v + topBorder, uScale, vScale, leftBorder, (index == yPasses ? remainderHeight : fillerHeight), blitOffset);
			// Right Border
			addTexturedRect(bufferBuilder, poseStack, x + leftBorder + canvasWidth, y + topBorder + (index * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, uScale, vScale, rightBorder, (index == yPasses ? remainderHeight : fillerHeight), blitOffset);
		}
		
		tessellator.draw();
		
		RenderSystem.disableBlend();
	}
	
	/**
	 * Draws a textured quad.
	 *
	 * @param poseStack Pose stack
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Width
	 * @param height Height
	 * @param uWidth U Width
	 * @param vHeight V Height
	 * @param uOffset U Offset
	 * @param vOffset V Offset
	 * @param textureWidth Texture width
	 * @param textureHeight Texture height
	 * @param blitOffset zLevel for drawing
	 * @param texture Texture location
	 * @param color The shader color. If using {@link RGBA#WHITE} then the image will not be colored
	 */
	public static void drawTexturedQuad(MatrixStack poseStack, int x, int y, int width, int height, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight, float blitOffset, Identifier texture, RGBA color) {
		drawTexturedQuad(poseStack, x, x + width, y, y + height, uOffset / textureWidth, (uOffset + uWidth) / textureWidth, vOffset / textureHeight, (vOffset + vHeight) / textureHeight, blitOffset, texture, color);
	}
	
	/**
	 * Draws a textured quad.
	 *
	 * @param poseStack Pose stack
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param width Width
	 * @param height Height
	 * @param blitOffset zLevel for drawing
	 * @param sprite Texture sprite from texture atlas
	 * @param color The shader color. If using {@link RGBA#WHITE} then the image will not be colored
	 */
	public static void drawTexturedQuad(MatrixStack poseStack, int x, int y, int width, int height, float blitOffset, Sprite sprite, RGBA color) {
		drawTexturedQuad(poseStack, x, x + width, y, y + height, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV(), blitOffset, sprite.getAtlas().getId(), color);
	}
	
	/**
	 * Draws a textured quad.
	 *
	 * @param poseStack Pose stack
	 * @param x1 X1 coordinate
	 * @param x2 X2 coordinate
	 * @param y1 Y1 coordinate
	 * @param y2 Y2 coordinate
	 * @param u1 U1 coordinate
	 * @param u2 U2 coordinate
	 * @param v1 V1 coordinate
	 * @param v2 V2 coordinate
	 * @param blitOffset zLevel for drawing
	 * @param texture Texture location
	 * @param color The shader color. If using {@link RGBA#WHITE} then the image will not be colored
	 */
	public static void drawTexturedQuad(MatrixStack poseStack, int x1, int x2, int y1, int y2, float u1, float u2, float v1, float v2, float blitOffset, Identifier texture, RGBA color) {
		final var tessellator = Tessellator.getInstance();
		final var bufferBuilder = tessellator.getBuffer();
		
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, texture);
		setShaderColor(color);
		
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		
		addTexturedQuad(bufferBuilder, poseStack, x1, x2, y1, y2, u1, u2, v1, v2, blitOffset);
		
		tessellator.draw();
		
		RenderSystem.disableBlend();
	}
	
	/**
	 * Adds a textured rectangle to the buffer builder. The vertex format must be {@link VertexFormats#POSITION_TEXTURE}
	 * and the draw format must be {@link VertexFormat.DrawMode#QUADS}
	 *
	 * @param bufferBuilder Buffer builder
	 * @param poseStack Pose stack
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param u U coordinate
	 * @param v V coordinate
	 * @param uScale U scale
	 * @param vScale V scale
	 * @param width Width
	 * @param height Height
	 * @param blitOffset zLevel for drawing
	 */
	public static void addTexturedRect(BufferBuilder bufferBuilder, MatrixStack poseStack, int x, int y, int u, int v, float uScale, float vScale, int width, int height, float blitOffset) {
		final var matrix = poseStack.peek().getModel();
		
		bufferBuilder.vertex(matrix, x, y + height, blitOffset).texture(u * uScale, ((v + height) * vScale)).next();
		bufferBuilder.vertex(matrix, x + width, y + height, blitOffset).texture((u + width) * uScale, ((v + height) * vScale)).next();
		bufferBuilder.vertex(matrix, x + width, y, blitOffset).texture((u + width) * uScale, (v * vScale)).next();
		bufferBuilder.vertex(matrix, x, y, blitOffset).texture(u * uScale, (v * vScale)).next();
	}
	
	/**
	 * Adds a textured quad to the buffer builder. The vertex format must be {@link VertexFormats#POSITION_TEXTURE} and
	 * the draw format must be {@link VertexFormat.DrawMode#QUADS}
	 *
	 * @param bufferBuilder Buffer builder
	 * @param poseStack Pose stack
	 * @param x1 X1 coordinate
	 * @param x2 X2 coordinate
	 * @param y1 Y1 coordinate
	 * @param y2 Y2 coordinate
	 * @param u1 U1 coordinate
	 * @param u2 U2 coordinate
	 * @param v1 V1 coordinate
	 * @param v2 V2 coordinate
	 * @param blitOffset zLevel for drawing
	 */
	public static void addTexturedQuad(BufferBuilder bufferBuilder, MatrixStack poseStack, int x1, int x2, int y1, int y2, float u1, float u2, float v1, float v2, float blitOffset) {
		final var matrix = poseStack.peek().getModel();
		
		bufferBuilder.vertex(matrix, x1, y2, blitOffset).texture(u1, v2).next();
		bufferBuilder.vertex(matrix, x2, y2, blitOffset).texture(u2, v2).next();
		bufferBuilder.vertex(matrix, x2, y1, blitOffset).texture(u2, v1).next();
		bufferBuilder.vertex(matrix, x1, y1, blitOffset).texture(u1, v1).next();
	}
	
	/**
	 * Adds a quad to the buffer builder. The vertex format must be {@link VertexFormats#POSITION_COLOR} and the draw
	 * format must be {@link VertexFormat.DrawMode#QUADS}
	 *
	 * @param bufferBuilder Buffer builder
	 * @param poseStack Pose stack
	 * @param x1 X1 coordinate
	 * @param x2 X2 coordinate
	 * @param y1 Y1 coordinate
	 * @param y2 Y2 coordinate
	 * @param color Color of the vertices
	 * @param blitOffset zLevel for drawing
	 */
	public static void addColoredQuad(BufferBuilder bufferBuilder, MatrixStack poseStack, int x1, int x2, int y1, int y2, RGBA color, float blitOffset) {
		final var matrix = poseStack.peek().getModel();
		
		bufferBuilder.vertex(matrix, x1, y2, blitOffset).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
		bufferBuilder.vertex(matrix, x2, y2, blitOffset).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
		bufferBuilder.vertex(matrix, x2, y1, blitOffset).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
		bufferBuilder.vertex(matrix, x1, y1, blitOffset).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).next();
	}
	
	/**
	 * Adds a quad to the buffer builder. The vertex format must be {@link VertexFormats#POSITION} and the draw format
	 * must be {@link VertexFormat.DrawMode#QUADS}
	 *
	 * @param bufferBuilder Buffer builder
	 * @param poseStack Pose stack
	 * @param x1 X1 coordinate
	 * @param x2 X2 coordinate
	 * @param y1 Y1 coordinate
	 * @param y2 Y2 coordinate
	 * @param blitOffset zLevel for drawing
	 */
	public static void addQuad(BufferBuilder bufferBuilder, MatrixStack poseStack, int x1, int x2, int y1, int y2, float blitOffset) {
		final var matrix = poseStack.peek().getModel();
		
		bufferBuilder.vertex(matrix, x1, y2, blitOffset).next();
		bufferBuilder.vertex(matrix, x2, y2, blitOffset).next();
		bufferBuilder.vertex(matrix, x2, y1, blitOffset).next();
		bufferBuilder.vertex(matrix, x1, y1, blitOffset).next();
	}
	
	/**
	 * Sets the shader color from {@link RGBA} type
	 *
	 * @param rgba Color
	 */
	public static void setShaderColor(RGBA rgba) {
		RenderSystem.setShaderColor(rgba.getRedComponent(), rgba.getGreenComponent(), rgba.getBlueComponent(), rgba.getAlphaComponent());
	}
	
	/**
	 * Extended matrix that adds getters and setters for all matrix values
	 *
	 * @author HyCraftHD
	 */
	public static class Matrix4fExtended extends Matrix4f {
		
		/**
		 * Creates a matrix4f with the values of the parameter
		 *
		 * @param matrix4f Matrix4f to copy values
		 */
		public Matrix4fExtended(Matrix4f matrix4f) {
			super(matrix4f);
		}
		
		public float getM00() {
			return a00;
		}
		
		public void setM00(float m00) {
			this.a00 = m00;
		}
		
		public float getM01() {
			return a01;
		}
		
		public void setM01(float m01) {
			this.a01 = m01;
		}
		
		public float getM02() {
			return a02;
		}
		
		public void setM02(float m02) {
			this.a02 = m02;
		}
		
		public float getM03() {
			return a03;
		}
		
		public void setM03(float m03) {
			this.a03 = m03;
		}
		
		public float getM10() {
			return a10;
		}
		
		public void setM10(float m10) {
			this.a10 = m10;
		}
		
		public float getM11() {
			return a11;
		}
		
		public void setM11(float m11) {
			this.a11 = m11;
		}
		
		public float getM12() {
			return a12;
		}
		
		public void setM12(float m12) {
			this.a12 = m12;
		}
		
		public float getM13() {
			return a13;
		}
		
		public void setM13(float m13) {
			this.a13 = m13;
		}
		
		public float getM20() {
			return a20;
		}
		
		public void setM20(float m20) {
			this.a20 = m20;
		}
		
		public float getM21() {
			return a21;
		}
		
		public void setM21(float m21) {
			this.a21 = m21;
		}
		
		public float getM22() {
			return a22;
		}
		
		public void setM22(float m22) {
			this.a22 = m22;
		}
		
		public float getM23() {
			return a23;
		}
		
		public void setM23(float m23) {
			this.a23 = m23;
		}
		
		public float getM30() {
			return a30;
		}
		
		public void setM30(float m30) {
			this.a30 = m30;
		}
		
		public float getM31() {
			return a31;
		}
		
		public void setM31(float m31) {
			this.a31 = m31;
		}
		
		public float getM32() {
			return a32;
		}
		
		public void setM32(float m32) {
			this.a32 = m32;
		}
		
		public float getM33() {
			return a33;
		}
		
		public void setM33(float m33) {
			this.a33 = m33;
		}
	}
}