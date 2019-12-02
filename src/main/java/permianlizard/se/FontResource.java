package permianlizard.se;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FontResource {

    public static Path FONTS_PATH = Paths.get("resources", "fonts");

    public static final String DEFAULT = "Mono.ttf";

    private static final String[] FONT_FILES = { DEFAULT };
    private static Map<String, Font> fontMap = new HashMap<String,Font>(FONT_FILES.length);

    public static void loadFonts() throws IOException, FileNotFoundException, FontFormatException {
        for (String fontName: FONT_FILES) {
            loadFont(fontName);
        }
    }

    public static Font loadFont(String name) throws IOException, FileNotFoundException, FontFormatException {
        Font font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(FONTS_PATH.resolve(name).toString()));
        fontMap.put(name, font);
        return font;
    }

    public static Font getFont(String name) {
        return fontMap.get(name);
    }
}
