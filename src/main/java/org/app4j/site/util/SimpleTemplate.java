package org.app4j.site.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class SimpleTemplate {
    private final String template;
    private final List<Segment> segments = new ArrayList<>();

    public SimpleTemplate(String template) {
        this.template = template;
        parse();
    }

    final void parse() {
        int last = 0;
        int index = 0;

        for (int i = 0; i < template.length(); i++) {
            char c = template.charAt(i);
            if (c == '{') {
                if (i > 0) {
                    segments.add(new Segment(last, i));
                }
                last = i;
            } else if (c == '}') {
                if (template.charAt(last) == '{') {
                    segments.add(new Variable(last, i + 1));
                    index++;
                } else {
                    segments.add(new Segment(last, i + 1));
                }
                last = i + 1;
            }
        }

        if (last < template.length()) {
            segments.add(new Segment(last, template.length()));
        }
    }

    public String eval(Map<String, String> context) {
        StringBuilder b = new StringBuilder();
        for (Segment segment : segments) {
            b.append(segment.get(context));
        }
        return b.toString();
    }

    class Segment {
        final int start;
        final int end;

        public Segment(int start, int end) {
            this.start = start;
            this.end = end;
        }

        String get(Map<String, String> context) {
            return template.substring(start, end);
        }
    }

    class Variable extends Segment {
        final String key;

        public Variable(int start, int end) {
            super(start, end);
            key = template.substring(start + 1, end - 1);
        }

        String get(Map<String, String> context) {
            if (context.containsKey(key)) {
                return context.get(key);
            }
            return "";
        }
    }
}
