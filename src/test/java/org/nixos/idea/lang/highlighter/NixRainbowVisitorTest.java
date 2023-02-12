package org.nixos.idea.lang.highlighter;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NotNull;

public final class NixRainbowVisitorTest extends BasePlatformTestCase {

    public void testNoHighlightingWhenDisabled() {
        myFixture.testRainbow("rainbow.nix",
                "let x = y; in x",
                false, false);
    }

    public void testSelectExpression() {
        doTest("let\n" +
                "  <rainbow color='ff000004'>x</rainbow> = null;\n" +
                "in [\n" +
                "  <rainbow color='ff000004'>x</rainbow>\n" +
                "  <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow>\n" +
                "  <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow>.<rainbow color='ff000002'>z</rainbow>\n" +
                "  <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000003'>z</rainbow>\n" +
                "  <rainbow color='ff000004'>x</rainbow>.\"no-highlighting-for-string-attributes\"\n" +
                "]");
    }

    public void testLetExpression() {
        doTest("let\n" +
                "  inherit (null) \"no-highlighting-for-string-attributes\" <rainbow color='ff000004'>x</rainbow>;\n" +
                "  <rainbow color='ff000004'>x</rainbow> = null;\n" +
                "  <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow> = null;\n" +
                "  <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow>.<rainbow color='ff000002'>z</rainbow> = null;\n" +
                "  <rainbow color='ff000004'>x</rainbow>.\"no-highlighting-for-string-attributes\" = null;\n" +
                "  <rainbow color='ff000003'>copy</rainbow> = <rainbow color='ff000004'>x</rainbow>;\n" +
                "in [\n" +
                "  <rainbow color='ff000004'>x</rainbow>\n" +
                "  <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow>\n" +
                "  <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow>.<rainbow color='ff000002'>z</rainbow>\n" +
                "]");
    }

    public void testLegacyLetExpression() {
        doTest("let {\n" +
                "  inherit (null) \"no-highlighting-for-string-attributes\" <rainbow color='ff000004'>x</rainbow>;\n" +
                "  <rainbow color='ff000004'>x</rainbow> = null;\n" +
                "  <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow> = null;\n" +
                "  <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow>.<rainbow color='ff000002'>z</rainbow> = null;\n" +
                "  <rainbow color='ff000004'>x</rainbow>.\"no-highlighting-for-string-attributes\" = null;\n" +
                "  <rainbow color='ff000003'>body</rainbow> = [\n" +
                "    <rainbow color='ff000004'>x</rainbow>\n" +
                "    <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow>\n" +
                "    <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow>.<rainbow color='ff000002'>z</rainbow>\n" +
                "  ];\n" +
                "}");
    }

    public void testRecursiveSet() {
        doTest("rec {\n" +
                "  inherit (null) \"no-highlighting-for-string-attributes\" <rainbow color='ff000004'>x</rainbow>;\n" +
                "  <rainbow color='ff000004'>x</rainbow> = null;\n" +
                "  <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow> = null;\n" +
                "  <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow>.<rainbow color='ff000002'>z</rainbow> = null;\n" +
                "  <rainbow color='ff000004'>x</rainbow>.\"no-highlighting-for-string-attributes\" = null;\n" +
                "  <rainbow color='ff000003'>body</rainbow> = [\n" +
                "    <rainbow color='ff000004'>x</rainbow>\n" +
                "    <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow>\n" +
                "    <rainbow color='ff000004'>x</rainbow>.<rainbow color='ff000001'>y</rainbow>.<rainbow color='ff000002'>z</rainbow>\n" +
                "  ];\n" +
                "}");
    }

    public void testNoHighlightingForNonRecursiveSet() {
        doTest("{\n" +
                "  inherit (null) \"no-highlighting-for-string-attributes\" x;\n" +
                "  x = null;\n" +
                "  x.y = null;\n" +
                "  x.\"no-highlighting-for-string-attributes\" = null;\n" +
                "}");
    }

    public void testLambda() {
        // TODO: Ideally, za should have the same color as z.za.
        doTest("<rainbow color='ff000004'>x</rainbow>:\n" +
                "{<rainbow color='ff000002'>y</rainbow>}:\n" +
                "<rainbow color='ff000003'>z</rainbow>@{<rainbow color='ff000001'>za</rainbow>, ...}: [\n" +
                "  <rainbow color='ff000004'>x</rainbow>\n" +
                "  <rainbow color='ff000002'>y</rainbow>\n" +
                "  <rainbow color='ff000003'>z</rainbow>\n" +
                "  <rainbow color='ff000001'>za</rainbow>\n" +
                "]");
    }

    private void doTest(@NotNull String code) {
        myFixture.testRainbow("rainbow.nix", code, true, true);
    }
}
