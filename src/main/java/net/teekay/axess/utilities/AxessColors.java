package net.teekay.axess.utilities;

public class AxessColors {
    public static class Color {
        public int colorInt;

        public Color(int color) {
            this.colorInt = color;
        }

        public float getRed() {
            return ((colorInt >> 16) & 0xFF) / 255f;
        }

        public float getGreen() {
            return ((colorInt >> 8) & 0xFF) / 255f;
        }

        public float getBlue() {
            return (colorInt & 0xFF) / 255f;
        }
    }

    public static Color MAIN = new Color(0x1170FF);
}
