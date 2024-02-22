package com.zqw.mobile.grainfull.app.utils;

import android.text.TextUtils;

import java.util.LinkedHashMap;
import java.util.Map;

import br.tiagohm.markdownview.css.StyleSheet;

/**
 * @ProjectName: EasyAIAndroid
 * @Package: com.zqw.mobile.easyai.mvp.ui.widget
 * @ClassName: ChatStyle
 * @Description:
 * @Author: WLY
 * @CreateDate: 2024/2/21 16:55
 */
public class ChatStyle implements StyleSheet {
    private static final String NO_MEDIA_QUERY = "NO_MEDIA_QUERY";
    private Map<String, Map<String, Map<String, String>>> mRules = new LinkedHashMap<>();
    private Map<String, String> mFontFaces = new LinkedHashMap<>();
    private String currentMediaQuery;

    public ChatStyle() {
        currentMediaQuery = NO_MEDIA_QUERY;
        mRules.put(currentMediaQuery, new LinkedHashMap<>());
        //Estilos padrões.
        //Alinhamento de Texto
        addRule("p", "text-align: left");
        addRule(".text-left", "text-align: left");
        addRule(".text-right", "text-align: right");
        addRule(".text-center", "text-align: center");
        addRule(".text-justify", "text-align: justify");
        //Cores.
        addRule("red, .red", "color: #f44336");
        addRule("pink, .pink", "color: #E91E63");
        addRule("purple, .purple", "color: #9C27B0");
        addRule("deeppurple, .deeppurple", "color: #673AB7");
        addRule("indigo, .indigo", "color: #3F51B5");
        addRule("blue, .blue", "color: #2196F3");
        addRule("lightblue, .lightblue", "color: #03A9F4");
        addRule("cyan, .cyan", "color: #00BCD4");
        addRule("teal, .teal", "color: #009688");
        addRule("green, .green", "color: #4CAF50");
        addRule("lightgreen, .lightgreen", "color: #8BC34A");
        addRule("lime, .lime", "color: #CDDC39");
        addRule("yellow, .yellow", "color: #FFEB3B");
        addRule("amber, .amber", "color: #FFC107");
        addRule("orange, .orange", "color: #FF9800");
        addRule("deeporange, .deeporange", "color: #FF5722");
        addRule("brown, .brown", "color: #795548");
        addRule("grey, .grey", "color: #9E9E9E");
        addRule("bluegrey, .bluegrey", "color: #607D8B");
        //Tamanho de texto.
        addRule("smaller, .text-smaller", "font-size: smaller");
        addRule("small, .text-small", "font-size: small");
        addRule("medium, .text-medium", "font-size: medium");
        addRule("large, .text-large", "font-size: large");
        addRule("larger, .text-larger", "font-size: larger");
        addRule("x-small, .text-x-small", "font-size: x-small");
        addRule("x-large, .text-x-large", "font-size: x-large");
        addRule("xx-small, .text-xx-small", "font-size: xx-small");
        addRule("xx-large, .text-xx-large", "font-size: xx-large");
        //Botão voltar ao topo.
        addRule("*", "box-sizing: border-box");
        addRule("html", "height: auto", "position: fixed");
        addRule("body", "font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif", "font-size: 14px", "line-height: 1.42857143", "color: #333", "background-color: #f6f7fb", "margin: 0", "height: auto");
        addRule("h1", "font-size: 36px", "margin-top: 20px", "margin-bottom: 10px", "font-weight: 500", "line-height: 1.1");
        addRule("h2", "font-size: 30px", "margin-top: 20px", "margin-bottom: 10px", "font-weight: 500", "line-height: 1.1");
        addRule("h3", "font-size: 24px", "margin-top: 20px", "margin-bottom: 10px", "margin-top: 20px", "margin-bottom: 10px", "font-weight: 500", "line-height: 1.1");
        addRule("h4", "font-size: 18px", "margin-top: 10px", "margin-bottom: 10px", "font-weight: 500", "line-height: 1.1");
        addRule("h5", "font-size: 14px", "margin-top: 10px", "margin-bottom: 10px", "font-weight: 500", "line-height: 1.1");
        addRule("h6", "font-size: 12px", "margin-top: 10px", "margin-bottom: 10px", "font-weight: 500", "line-height: 1.1");
        addRule("hr", "margin-top: 20px", "margin-bottom: 20px", "border: 0", "border-top: 1px solid #eee");
        addRule("p", "margin: 0 0 10px");
        addRule("strong", "font-weight: 700");
        addRule("em", "font-style: italic");
        addRule("a", "color: #337ab7", "text-decoration: none", "word-wrap: break-word");
        addRule("img", "vertical-align: middle", "border: 0", "max-width: 100%");
        addRule("code", "padding: 2px 4px", "font-size: 90%", "color: #c7254e", "background-color: #f9f2f4", "white-space: pre-wrap", "border-radius: 4px", "font-family: Menlo,Monaco,Consolas,\"Courier New\",monospace", "word-wrap: break-word");
        addRule("pre code", "padding: 0", "white-wrap: pre-wrap", "white-space: pre", "background-color: transparent", "border-radius: 0");
        addRule("pre", "display: block", "padding: 9.5px", "margin: 0 0 10px", "font-size: 13px", "line-height: 1.42857143", "color: #333", "background-color: #f5f5f5", "border: 1px solid #ccc", "border-radius: 4px", "font-family: Menlo,Monaco,Consolas,\"Courier New\",monospace", "overflow: auto");
        addRule("blockquote", "padding: 0px 20px", "margin: 0 0 20px", "font-size: 14px", "border-left: 5px solid #eee");
        addRule("ul", "margin-top: 0", "margin-bottom: 10px");
        addRule("ol", "margin-top: 0", "margin-bottom: 10px");
        addRule("ol ol", "margim-bottom: 0");
        addRule("ol ul", "margim-bottom: 0");
        addRule("ul ol", "margim-bottom: 0");
        addRule("ul ul", "margim-bottom: 0");
        addRule("li", "word-wrap: break-word");
        addRule("table", "width: 100%", "background-color: transparent", "border-spacing: 0", "border-collapse: collapse");
        addRule("td", "padding: 1px");
        addRule("th", "padding: 1px");
        addRule("th[align=left]", "text-align: left");
        addRule("th[align=center]", "text-align: center");
        addRule("th[align=right]", "text-align: right");
        addRule("td[align=left]", "text-align: left");
        addRule("td[align=center]", "text-align: center");
        addRule("td[align=right]", "text-align: right");
        addRule("abbr", "border-bottom: 1px dotted #777");
        addRule("mark", "padding: 0.2em", "background-color: #fcf8e3");
        addRule("sub", "position: relative", "font-size: 75%", "line-height: 0", "vertical-align: baseline", "bottom: -0.25em");
        addRule("sup", "position: relative", "font-size: 75%", "line-height: 0", "vertical-align: baseline", "top: -0.5em");
        addRule("kbd", "padding: 2px 4px", "font-size: 90%", "color: #fff", "background-color: #333", "border-radius: 3px", "box-shadow: inset 0 -1px 0 rgba(0,0,0,0.25)", "font-family: Menlo, Monaco, Consolas, \"Courier New\", monospace");
        addRule("span.math", "color: inherit");
        addRule("lbl", "display: inline-block", "padding: 0 10px", "background: #1e87f0", "line-height: 1.5",
                "font-size: 12px", "color: #fff", "vertical-align: middle", "white-space: nowrap", "border-radius: 2px", "text-transform: uppercase;");
        addRule("button", "margin: 0", "overflow: visible", "display: inline-block", "padding: 0 30px",
                "vertical-align: middle", "font-size: 14px", "line-height: 38px", "text-align: center",
                "color: #222", "border: 1px solid #e5e5e5", "background-color: transparent");

        addRule(".lbl-success", "background-color: #32d296");
        addRule(".lbl-warning", "background-color: #faa05a");
        addRule(".lbl-danger", "background-color: #f0506e");
        //Class
        addRule(".container", "margin-right: auto", "margin-left: auto");
        addRule(".task-list-item", "list-style-type: none");
        addRule(".task-list-item-checkbox", "vertical-align: middle", "margin: 0em 0.2em 0.25em -1.6em");
        addRule(".footnotes p", "margin: 0");
        addRule(".footnotes li", "margin-top: 2px", "margin-bottom: 2px");
        addRule(".player", "position: relative", "padding-bottom: 56.25%", "padding-top: 25px", "height: 0");
        addRule(".player iframe", "position: absolute", "top: 0", "left: 0", "width: 100%", "height: 100%");
        addRule(".twitter-follow-button", "vertical-align: middle");
        addRule(".scrollup", "background-color: #24292e");
        //IDs

        //Highlight.js
        addRule(".hljs", "display: block", "overflow-x: auto", "color: #4d4d4c", "padding: 0.5em");
        addRule(".hljs-type", "color: #880000");
        addRule(".hljs-params", "color: #880000");
        addRule(".hljs-string", "color: #880000");
        addRule(".hljs-number", "color: #880000");
        addRule(".hljs-selector-id", "color: #880000");
        addRule(".hljs-selector-class", "color: #880000");
        addRule(".hljs-comment", "color: #8e908c");
        addRule(".hljs-quote", "color: #8e908c");
        addRule(".hljs-template-tag", "color: #880000");
        addRule(".hljs-tag", "color: #880000");
        addRule(".hljs-name", "color: #880000");
        addRule(".hljs-deletion", "color: #880000");
        addRule(".hljs-title", "color: #880000", "font-weight: bold");
        addRule(".hljs-section", "color: #880000", "font-weight: bold");
        addRule(".hljs-regexp", "color: #BC6060");
        addRule(".hljs-symbol", "color: #BC6060");
        addRule(".hljs-variable", "color: #BC6060");
        addRule(".hljs-template-variable", "color: #BC6060");
        addRule(".hljs-link", "color: #BC6060");
        addRule(".hljs-attribute", "color: #eab700");
        addRule(".hljs-selector-attr", "color: #BC6060");
        addRule(".hljs-selector-pseudo", "color: #BC6060");
        addRule(".hljs-literal", "color: #78A960");
        addRule(".hljs-built_in", "color: #397300");
        addRule(".hljs-builtin-name", "color: #397300");
        addRule(".hljs-bullet", "color: #397300");
        addRule(".hljs-code", "color: #397300");
        addRule(".hljs-addition", "color: #397300");
        addRule(".hljs-meta", "color: #1f7199");
        addRule(".hljs-meta-string", "color: #4d99bf");
        addRule(".hljs-keyword", "color: #000000");
        addRule(".hljs-selector-tag", "color: #000000");
        addRule(".hljs-emphasis", "font-style: italic");
        addRule(".hljs-strong", "font-weight: bold");

        // 自定义
        addRule("html", "height: auto", "position: fixed", "background-color:#ADD8E6");
        addRule("body", "line-height: 1.6", "padding: 10px", "height: auto", "display: inline-block");
        addRule(".scrollup", "width: 55px", "height: 55px", "position: fixed", "bottom: 15px", "right: 15px", "visibility: hidden", "display: flex", "align-items: center", "justify-content: center", "margin: 0 !important", "line-height: 70px", "box-shadow: 0 0 4px rgba(0, 0, 0, 0.14), 0 4px 8px rgba(0, 0, 0, 0.28)", "border-radius: 50%", "color: #fff", "padding: 5px", "background-color: #00BF4C");

        addRule("h1", "font-size: 28px");
        addRule("h2", "font-size: 24px");
        addRule("h3", "font-size: 18px");
        addRule("h4", "font-size: 16px");
        addRule("h5", "font-size: 14px");
        addRule("h6", "font-size: 14px");
        addRule("pre", "position: relative", "padding: 14px 10px", "border: 0", "border-radius: 3px", "background-color: #f6f8fa");
        addRule("pre code", "position: relative", "line-height: 1.45", "background-color: transparent");
        addRule("table", "background-color: #fff");
        addRule("table tr:nth-child(2n)", "background-color: #f6f8fa");
        addRule("table th", "padding: 6px 13px", "border: 1px solid #dfe2e5");
        addRule("table td", "padding: 6px 13px", "border: 1px solid #dfe2e5");
        addRule("kbd", "color: #444d56", "font-family: Consolas, \"Liberation Mono\", Menlo, Courier, monospace",
                "background-color: #fcfcfc", "border: solid 1px #c6cbd1",
                "border-bottom-color: #959da5", "border-radius: 3px", "box-shadow: inset 0 -1px 0 #959da5");
        addRule("pre[language]::before", "content: attr(language)", "position: absolute", "top: 0", "right: 5px", "padding: 2px 1px",
                "text-transform: uppercase", "color: #666", "font-size: 8.5px");
        addRule("pre:not([language])", "padding: 6px 10px");
        addRule(".footnotes li p:last-of-type", "display: inline");
        addRule(".yt-player", "box-shadow: 0px 0px 12px rgba(0,0,0,0.2)");

        //Highlight.js

        addRule(".hljs-comment", "color: #8e908c");
        addRule(".hljs-quote", "color: #8e908c");

        addRule(".hljs-variable", "color: #c82829");
        addRule(".hljs-template-variable", "color: #c82829");
        addRule(".hljs-tag", "color: #c82829");
        addRule(".hljs-name", "color: #c82829");
        addRule(".hljs-selector-id", "color: #c82829");
        addRule(".hljs-selector-class", "color: #c82829");
        addRule(".hljs-regexp", "color: #c82829");
        addRule(".hljs-deletion", "color: #c82829");

        addRule(".hljs-number", "color: #f5871f");
        addRule(".hljs-built_in", "color: #f5871f");
        addRule(".hljs-builtin-name", "color: #f5871f");
        addRule(".hljs-literal", "color: #f5871f");
        addRule(".hljs-type", "color: #f5871f");
        addRule(".hljs-params", "color: #f5871f");
        addRule(".hljs-meta", "color: #f5871f");
        addRule(".hljs-link", "color: #f5871f");

        addRule(".hljs-attribute", "color: #eab700");

        addRule(".hljs-string", "color: #718c00");
        addRule(".hljs-symbol", "color: #718c00");
        addRule(".hljs-bullet", "color: #718c00");
        addRule(".hljs-addition", "color: #718c00");

        addRule(".hljs-title", "color: #4271ae");
        addRule(".hljs-section", "color: #4271ae");

        addRule(".hljs-keyword", "color: #8959a8");
        addRule(".hljs-selector-tag", "color: #8959a8");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> faces : mFontFaces.entrySet()) {
            sb.append("@font-face {");
            sb.append(faces.getValue());
            sb.append("}\n");
        }

        for (Map.Entry<String, Map<String, Map<String, String>>> q : mRules.entrySet()) {
            if (!q.getKey().equals(NO_MEDIA_QUERY)) {
                sb.append("@media ");
                sb.append(q.getKey());
                sb.append(" {\n");
            }

            for (Map.Entry<String, Map<String, String>> e : q.getValue().entrySet()) {
                sb.append(e.getKey());
                sb.append(" {");
                for (Map.Entry<String, String> declaration : e.getValue().entrySet()) {
                    sb.append(declaration.getKey());
                    sb.append(":");
                    sb.append(declaration.getValue());
                    sb.append(";");
                }
                sb.append("}\n");
            }

            if (!q.getKey().equals(NO_MEDIA_QUERY)) {
                sb.append("}\n");
            }
        }

        return sb.toString();
    }

    @Override
    public String toHTML() {
        return "<style>\n" + toString() + "\n</style>\n";
    }

    private Map<String, Map<String, String>> getCurrentMediaQuery() {
        return mRules.get(currentMediaQuery);
    }

    public void addMedia(String mediaQuery) {
        if (mediaQuery != null && mediaQuery.trim().length() > 0) {
            mediaQuery = mediaQuery.trim();

            if (!mRules.containsKey(mediaQuery)) {
                mRules.put(mediaQuery, new LinkedHashMap<String, Map<String, String>>());
                currentMediaQuery = mediaQuery;
            }
        }
    }

    public void addFontFace(String fontFamily, String fontStretch, String fontStyle, String fontWeight,
                            String... src) {
        if (!TextUtils.isEmpty(fontFamily) && src.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("font-family:").append(fontFamily).append(";");
            sb.append("font-stretch:").append(TextUtils.isEmpty(fontStretch) ? "normal" : fontStretch).append(";");
            sb.append("font-style:").append(TextUtils.isEmpty(fontStyle) ? "normal" : fontStyle).append(";");
            sb.append("font-weight:").append(TextUtils.isEmpty(fontWeight) ? "normal" : fontWeight).append(";");
            sb.append("src:");
            for (int i = 0; i < src.length; i++) {
                sb.append(src[i]);
                if (i < src.length - 1) sb.append(",");
            }
            sb.append(";");
            mFontFaces.put(fontFamily.trim(), sb.toString());
        }
    }

    public void endMedia() {
        currentMediaQuery = NO_MEDIA_QUERY;
    }

    public void addRule(String selector, String... declarations) {
        if (selector != null && selector.trim().length() > 0 && declarations.length > 0) {
            selector = selector.trim();

            if (!getCurrentMediaQuery().containsKey(selector)) {
                getCurrentMediaQuery().put(selector, new LinkedHashMap<String, String>());
            }

            for (String declaration : declarations) {
                //String vazia.
                if (declaration == null || declaration.trim().length() <= 0) continue;

                String[] nameAndValue = declaration.trim().split(":");

                if (nameAndValue.length == 2) {
                    String name = nameAndValue[0].trim();
                    String value = nameAndValue[1].trim();
                    getCurrentMediaQuery().get(selector).put(name, value);
                } else {
//                    Logger.e("invalid css: '" + declaration + "' in selector: " + selector);
                }
            }
        }
    }

    public void removeRule(String selector) {
        getCurrentMediaQuery().remove(selector);
    }

    public void removeDeclaration(String selector, String declarationName) {
        if (!TextUtils.isEmpty(selector) && getCurrentMediaQuery().containsKey(selector)) {
            getCurrentMediaQuery().get(selector).remove(declarationName);
        }
    }

    public void replaceDeclaration(String selector, String declarationName, String newDeclarationValue) {
        if (!TextUtils.isEmpty(selector) && !TextUtils.isEmpty(declarationName)) {
            if (getCurrentMediaQuery().containsKey(selector) && getCurrentMediaQuery().get(selector).containsKey(declarationName)) {
                getCurrentMediaQuery().get(selector).put(declarationName, newDeclarationValue);
            }
        }
    }
}
