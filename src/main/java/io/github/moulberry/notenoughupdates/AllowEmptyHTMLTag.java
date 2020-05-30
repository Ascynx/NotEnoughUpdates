package io.github.moulberry.notenoughupdates;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.HTMLTag;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AllowEmptyHTMLTag extends HTMLTag {
    public AllowEmptyHTMLTag(String name) {
        super(name);
    }

    public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
        boolean newLinesAfterTag = false;
        boolean newLinesAfterChildren = false;
        TagNode node = this;
        String name = node.getName();
        List<Object> children = node.getChildren();

        if (NEW_LINES) {
            switch (name) {
                case "div":
                case "p":
                case "li":
                case "td":
                    buf.append('\n');
                    break;
                case "table":
                case "ul":
                case "ol":
                case "th":
                case "tr":
                    buf.append('\n');
                    newLinesAfterTag = true;
                    newLinesAfterChildren = true;
                    break;
                case "pre":
                    buf.append('\n');
                    newLinesAfterTag = false;
                    newLinesAfterChildren = true;
                    break;
                case "blockquote":
                    newLinesAfterChildren = true;
                    break;
            }
        }
        buf.append('<');
        buf.append(name);

        Map<String, String> tagAtttributes = node.getAttributes();

        appendAttributes(buf, tagAtttributes);

        if (children.size() == 0) {
            buf.append(" />");
        } else {
            buf.append('>');
            if (newLinesAfterTag) {
                buf.append('\n');
            }
            converter.nodesToText(children, buf, model);
            if (newLinesAfterChildren) {
                buf.append('\n');
            }
            buf.append("</");
            buf.append(node.getName());
            buf.append('>');
        }
    }
}
