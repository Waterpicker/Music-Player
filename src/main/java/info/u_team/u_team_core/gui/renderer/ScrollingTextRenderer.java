package info.u_team.u_team_core.gui.renderer;

import java.util.function.Supplier;

import com.mojang.blaze3d.systems.RenderSystem;
import info.u_team.u_team_core.gui.elements.ScalingTextRenderer;
import info.u_team.u_team_core.util.RenderUtil.Matrix4fExtended;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vector4f;

public class ScrollingTextRenderer extends ScalingTextRenderer {
	
	protected int width;
	protected float stepSize;
	protected int speedTime;
	protected int waitTime;
	
	protected float moveDifference = 0;
	protected long lastTime = 0;
	protected State state = State.WAITING;
	
	public ScrollingTextRenderer(TextRenderer fontRenderer, Supplier<String> textSupplier, float x, float y) {
		super(fontRenderer, textSupplier, x, y);
		width = 100;
		stepSize = 1;
		speedTime = 20;
		waitTime = 4000;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public float getStepSize() {
		return stepSize;
	}
	
	public void setStepSize(float stepSize) {
		this.stepSize = stepSize;
	}
	
	public int getSpeedTime() {
		return speedTime;
	}
	
	public void setSpeedTime(int speedtime) {
		this.speedTime = speedtime;
	}
	
	public int getWaitTime() {
		return waitTime;
	}
	
	public void setWaitTime(int waittime) {
		this.waitTime = waittime;
	}
	
	public void copyState(ScrollingTextRenderer renderer) {
		setText(textSupplier.get());
		state = renderer.state;
		moveDifference = renderer.moveDifference;
		lastTime = renderer.lastTime;
	}
	
	@Override
	protected void updatedText() {
		state = State.WAITING;
		moveDifference = 0;
		lastTime = 0;
		super.updatedText();
	}
	
	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		final MinecraftClient minecraft = MinecraftClient.getInstance();
		final Window window = minecraft.getWindow();
		
		final Matrix4fExtended matrix = new Matrix4fExtended(matrixStack.peek().getModel());
		final double scaleFactor = window.getScaleFactor();
		
		final Vector4f vectorXY = new Vector4f(x, y, 0, 1);
		vectorXY.transform(matrix);
		
		// Cannot use transform here, because we only care about the scaling. M00 and M11 should have the right scaling
		final Vector4f vectorWH = new Vector4f(width * matrix.getM00(), (fontRenderer.fontHeight + 1) * scale * matrix.getM11(), 0, 1);
		
		final int nativeX = MathHelper.ceil(vectorXY.getX() * scaleFactor);
		final int nativeY = MathHelper.ceil(vectorXY.getY() * scaleFactor);
		
		final int nativeWidth = MathHelper.ceil(vectorWH.getX() * scaleFactor);
		final int nativeHeight = MathHelper.ceil(vectorWH.getY() * scaleFactor);
		
		RenderSystem.enableScissor(nativeX, window.getHeight() - (nativeY + nativeHeight), nativeWidth, nativeHeight);
		
		// Uncomment to test scissor
		// matrixStack.push();
		// matrixStack.getLast().getMatrix().setIdentity();
		// AbstractGui.fill(matrixStack, 0, 0, window.getScaledWidth(), window.getScaledHeight(), 0x8F00FF00);
		// matrixStack.pop();
		
		setText(textSupplier.get());
		renderFont(matrixStack, fontRenderer, getMovingX(x), y + 2 * scale);

		RenderSystem.disableScissor();
	}
	
	protected float getMovingX(float x) {
		final float textWidth = getTextWidth();
		if (width < textWidth) {
			final float maxMove = width - textWidth;
			
			if (lastTime == 0) {
				lastTime = System.currentTimeMillis();
			}
			
			if (state == State.WAITING) {
				if (hasWaitTimePassed()) {
					state = moveDifference >= 0 ? State.LEFT : State.RIGHT;
					lastTime = 0;
				}
			} else {
				if (hasSpeedTimePassed()) {
					if (state == State.LEFT ? moveDifference >= maxMove : moveDifference <= 0) {
						moveDifference += state == State.LEFT ? -stepSize : +stepSize;
					} else {
						state = State.WAITING;
					}
					lastTime = 0;
				}
			}
			return x + moveDifference;
		}
		return x;
	}
	
	protected boolean hasWaitTimePassed() {
		return System.currentTimeMillis() - waitTime >= lastTime;
	}
	
	protected boolean hasSpeedTimePassed() {
		return System.currentTimeMillis() - speedTime >= lastTime;
	}
	
	private enum State {
		WAITING,
		LEFT,
		RIGHT;
	}
	
}