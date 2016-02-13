package pv;

import com.squareup.protoparser.FieldElement;
import com.squareup.protoparser.MessageElement;
import com.squareup.protoparser.ProtoFile;
import com.squareup.protoparser.ProtoParser;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Takes input as location of the proto file in PROTO_FILE_LOCATION
 * and generates a directed graph representing attribute composition of proto messages
 *
 * @author Parth Solanki
 */

public class ProtoVisualize {

    private Graph<String, String> graph;

    public ProtoVisualize() {
        this.graph = new DelegateForest<String, String>();
    }

    private static Properties properties = PropertyLoader.getProperties();


    public static void main(String[] args) throws IOException {

        ProtoVisualize protoVisualize = new ProtoVisualize();

        ProtoFile protoFile = ProtoParser.parseUtf8(new File(properties.getProperty("proto.file.path")));

        protoVisualize.graph = protoVisualize.createGraphFromProto(protoFile);

        protoVisualize.plotGraph();
    }


    private Graph<String, String> createGraphFromProto(ProtoFile protoFile) {

        for (com.squareup.protoparser.TypeElement typeElement : protoFile.typeElements()) {

            MessageElement messageElement = (MessageElement) typeElement;

            //Add proto message to graph
            graph.addVertex(messageElement.name());

            for (FieldElement fieldElement : messageElement.fields()) {

                //Add the attributes to graph
                graph.addVertex(fieldElement.name());

                //Add edge from proto message to it's attribute
                graph.addEdge(messageElement.name() + "->" + fieldElement.name(),
                        messageElement.name(), fieldElement.name());
            }
        }

        return graph;
    }


    private void plotGraph() {

        //Defines the layout to be used. Layout determines the graph plotting style
        Layout<String, String> layout = new RadialTreeLayout<String, String>((Forest<String, String>) graph);

        VisualizationViewer<String, String> visualizationViewer = new VisualizationViewer<String, String>(layout);

        int width = Integer.parseInt(properties.getProperty("visualizationviewer.width").trim());

        int height = Integer.parseInt(properties.getProperty("visualizationviewer.height").trim());

        visualizationViewer.setPreferredSize(new Dimension(width, height));

        visualizationViewer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

        visualizationViewer.setGraphMouse(graphMouse);

        JFrame frame = new JFrame("Proto Visualizer");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(visualizationViewer);

        frame.pack();

        frame.setVisible(true);
    }
}
