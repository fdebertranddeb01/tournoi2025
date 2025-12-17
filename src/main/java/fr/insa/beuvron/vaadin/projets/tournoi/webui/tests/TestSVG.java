package fr.insa.beuvron.vaadin.projets.tournoi.webui.tests;

import com.vaadin.flow.component.Svg;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import fr.insa.beuvron.vaadin.projets.tournoi.webui.MainLayout;

@Route(value = "tests/svg", layout = MainLayout.class)
public class TestSVG extends VerticalLayout {

    public TestSVG() {
        String svgContent1 = "<svg width=\"100\" height=\"100\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                "  <circle cx=\"50\" cy=\"50\" r=\"40\" stroke=\"black\" stroke-width=\"2\" fill=\"red\" />\n" +
                "</svg>";
        Svg svg1 = new Svg(svgContent1);
        this.add(svg1);
        String svgContent2 = "<svg version=\"1.1\"\r\n" + //
                        "     baseProfile=\"full\"\r\n" + //
                        "     width=\"300\" height=\"200\"\r\n" + //
                        "     xmlns=\"http://www.w3.org/2000/svg\">\r\n" + //
                        "\r\n" + //
                        "  <rect width=\"100%\" height=\"100%\" fill=\"red\" />\r\n" + //
                        "\r\n" + //
                        "  <circle cx=\"150\" cy=\"100\" r=\"80\" fill=\"green\" />\r\n" + //
                        "\r\n" + //
                        "  <text x=\"150\" y=\"125\" font-size=\"60\" text-anchor=\"middle\" fill=\"white\">SVG</text>\r\n" + //
                        "\r\n" + //
                        "</svg>";
        Svg svg2 = new Svg(svgContent2);
        this.add(svg2);

    }

}
