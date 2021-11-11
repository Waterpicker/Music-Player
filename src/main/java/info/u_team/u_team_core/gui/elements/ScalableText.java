package info.u_team.u_team_core.gui.elements;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;
import info.u_team.u_team_core.api.gui.Scalable;
import info.u_team.u_team_core.util.RGBA;

public class ScalableText implements Drawable, Scalable {
	
	protected static final Consumer<ScalableText> EMPTY_TEXT_CHANGED = renderer -> {
	};
	
	protected final TextRenderer font;
	
	protected Supplier<String> textSupplier;
	
	protected float x;
	protected float y;
	
	private String text;
	private int textWidth;
	
	protected RGBA color;
	protected boolean shadow;
	protected float scale;
	private float positionFactor;
	
	protected Consumer<ScalableText> textChanged;
	
	public ScalableText(TextRenderer font, Supplier<String> textSupplier, float x, float y) {
		this.font = font;
		this.textSupplier = textSupplier;
		this.x = x;
		this.y = y;
		textWidth = -1;
		color = RGBA.WHITE;
		scale = 1;
		positionFactor = 1;
		textChanged = EMPTY_TEXT_CHANGED;
	}
	
	public void setTextSupplier(Supplier<String> textSupplier) {
		Objects.requireNonNull(textSupplier);
		this.textSupplier = textSupplier;
	}
	
	public Supplier<String> getTextSupplier() {
		return textSupplier;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public RGBA getColor() {
		return color;
	}
	
	public void setColor(RGBA color) {
		this.color = color;
	}
	
	public boolean isShadow() {
		return shadow;
	}
	
	public void setShadow(boolean shadow) {
		this.shadow = shadow;
	}
	
	@Override
	public float getScale() {
		return scale;
	}
	
	@Override
	public void setScale(float scale) {
		this.scale = scale;
		positionFactor = 1 / scale;
	}
	
	public void setTextChanged(Consumer<ScalableText> textChanged) {
		this.textChanged = textChanged;
	}
	
	public float getTextWidth() {
		if (textWidth == -1) { // If text width has never been set
			setText(textSupplier.get());
		}
		return textWidth * scale;
	}
	
	protected void setText(String newText) {
		if ((newText != null && !newText.equals(text)) || (newText == null && text != null)) {
			text = newText;
			textWidth = font.getWidth(newText);
			updatedText();
		}
	}
	
	protected void updatedText() {
		textChanged.accept(this);
	}
	
	@Override
	public void render(MatrixStack poseStack, int mouseX, int mouseY, float partialTicks) {
		// Get new text and set if has changed
		setText(textSupplier.get());
		renderFont(poseStack, font, x, y);
	}
	
	protected void renderFont(MatrixStack poseStack, TextRenderer font, float x, float y) {
		poseStack.push();
		poseStack.scale(scale, scale, 0);
		font.draw(text, x * positionFactor, y * positionFactor, color.getColorARGB(), poseStack.peek().getModel(), shadow, font.isRightToLeft());
		poseStack.pop();
	}
}
