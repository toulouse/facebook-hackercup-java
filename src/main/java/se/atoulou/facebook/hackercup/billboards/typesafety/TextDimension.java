package se.atoulou.facebook.hackercup.billboards.typesafety;

public final class TextDimension {
    private final Width width;
    private final Height height;

    public TextDimension(Width width, Height height) {
        this.width = width;
        this.height = height;
    }

    public Width getWidth() {
        return width;
    }

    public Height getHeight() {
        return height;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((height == null) ? 0 : height.hashCode());
        result = prime * result + ((width == null) ? 0 : width.hashCode());
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
        if (height == null) {
            if (other.height != null)
                return false;
        } else if (!height.equals(other.height))
            return false;
        if (width == null) {
            if (other.width != null)
                return false;
        } else if (!width.equals(other.width))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Dimension [width=" + width + ", height=" + height + "]";
    }
}
