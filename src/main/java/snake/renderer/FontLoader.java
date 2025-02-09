package snake.renderer;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FontLoader {
	int gridCol = 16;

	public Font loadFont(String fontPath, int size) {
		File file = new File(fontPath);
		try {
			return Font.createFont(Font.TRUETYPE_FONT, file).deriveFont(size);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Texture fontToSpriteSheet(String fontPath, int size, int charWidth, int charHeight) {
		int width = gridCol * 26;
		int height = 16;
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2d = bufferedImage.createGraphics();
		graphics2d.setFont(loadFont(fontPath, size));
		int x = 0;

		for (int i = 0; i < 26; i++) {
			char ch = 'a';
			ch = (char) (ch + i);
			graphics2d.drawString(String.valueOf(ch), x * charWidth, charHeight - 5);
			x++;
		}
		graphics2d.drawString(String.valueOf(" "), x*charWidth,charHeight-5);
		graphics2d.dispose();
		Texture texture=new Texture(fontPath.substring(fontPath.lastIndexOf("/")+1),bufferedImage);
		return texture;
	};
}
