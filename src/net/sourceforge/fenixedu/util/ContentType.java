/**
 * 
 */
package net.sourceforge.fenixedu.util;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum ContentType {

    JPG, GIF, PNG;

    public String getMimeType() {

        ContentType type = valueOf(name());

        switch (type) {
        case JPG:
            return "image/jpeg";
        case GIF:
            return "image/gif";
        case PNG:
            return "image/png";
        default:
            return "*/*";
        }

    }

    public static ContentType getContentType(String httpContentType) {

        String contentTypeInLowerCase = httpContentType.toLowerCase();
        if (contentTypeInLowerCase.equals("image/gif")) {
            return GIF;
        }
        if (contentTypeInLowerCase.equals("image/jpeg")
                || contentTypeInLowerCase.equals("image/jpg")
                || contentTypeInLowerCase.equals("image/pjpeg")) {
            return JPG;
        }
        if (contentTypeInLowerCase.equals("image/png")
                || contentTypeInLowerCase.equals("image/x-png")) {
            return PNG;
        }

        return null;
    }

}
