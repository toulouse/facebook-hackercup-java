package se.atoulou.facebook.hackercup.billboards.typesafety;

public final class TextDimension {
    private final LineCount lineCount;
    private final LineWidth lineWidth;

    public TextDimension(LineCount height, LineWidth width) {
        this.lineCount = height;
        this.lineWidth = width;
    }

    public LineCount getLineCount() {
        return lineCount;
    }

    public LineWidth getLineWidth() {
        return lineWidth;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lineCount == null) ? 0 : lineCount.hashCode());
        result = prime * result + ((lineWidth == null) ? 0 : lineWidth.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TextDimension other = (TextDimension) obj;
        if (lineCount == null) {
            if (other.lineCount != null)
                return false;
        } else if (!lineCount.equals(other.lineCount))
            return false;
        if (lineWidth == null) {
            if (other.lineWidth != null)
                return false;
        } else if (!lineWidth.equals(other.lineWidth))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TextDimension [lineCount=" + lineCount + ", lineWidth=" + lineWidth + "]";
    }
}
