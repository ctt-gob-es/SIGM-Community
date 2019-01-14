package es.sigem.dipcoruna.desktop.scan.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class UploadFilesServerResponseParser {
    private static final Pattern PATTERN_ERRORS = Pattern.compile("errors=(.*)");
    private static final Pattern PATTERN_OKS = Pattern.compile("oks=(.*)");

    private final String response;

    public UploadFilesServerResponseParser(final String response) {
        this.response = response;
    }

    public boolean huboError() {
        final Matcher matcher = PATTERN_ERRORS.matcher(response);
        if (! matcher.find()) {
            return false;
        }
        //Llega el mensaje de error, pero puede que llegue vacío
        return StringUtils.hasLength(matcher.group(1));
    }


    public List<String> getErrors() {
        if (! huboError()) {
            return Collections.<String>emptyList();
        }

        final Matcher matcher = PATTERN_ERRORS.matcher(response);
        matcher.find();
        return Arrays.asList(matcher.group(1).split("\\s*,\\s*"));
    }


    public List<String> getOks() {
        final Matcher matcher = PATTERN_OKS.matcher(response);
        if (matcher.find()) {
            return Arrays.asList(matcher.group(1).split("\\s*,\\s*"));
        }
        else {
            return Collections.<String>emptyList();
        }
    }
}
