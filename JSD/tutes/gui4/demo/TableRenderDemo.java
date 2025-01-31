package tutes.gui4.demo;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * TableRenderDemo is just like TableDemo, except that it explicitly initializes
 * column sizes, and it uses a combo box as an editor for the Sport column.
 */
public class TableRenderDemo extends JPanel {
    private final boolean DEBUG = false;

    public TableRenderDemo() {
        super(new GridLayout(1, 0));

        JTable table = new JTable(new MyTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(500, 100));
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);

        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        // Set up column sizes.
        initColumnSizes(table);

        // Fiddle with the Sport column's cell editors/renderers.
        setUpSportColumn(table.getColumnModel().getColumn(2));

        // Add the scroll pane to this panel.
        add(scrollPane);
    }

    /*
     * This method picks good column sizes. If all column heads are wider than the
     * column's cells' contents, then you can just use column.sizeWidthToFit().
     */
    private void initColumnSizes(JTable table) {
        MyTableModel model = (MyTableModel) table.getModel();
        TableColumn column;
        Component comp;
        int headerWidth;
        int cellWidth;
        TableCellRenderer headerRenderer = table.getTableHeader()
                .getDefaultRenderer();

        Object longValue = null;
        Object val;
        int rc = table.getRowCount();
        for (int i = 0; i < 5; i++) {
            // get the column
            column = table.getColumnModel().getColumn(i);
            // get the table render component for the column
            comp = headerRenderer.getTableCellRendererComponent(null,
                    column.getHeaderValue(), false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;
            // get the longest cell render component at the column
            // it is the cell render that is used to render the longest
            // value on the column

            // get the longest value of this column
            for (int j = 0; j < rc; j++) {
                val = model.getValueAt(j, i);
                if (longValue == null || longValue.toString().length() < val.toString().length()) {
                    longValue = val;
                }
            }

            comp = table.getDefaultRenderer(model.getColumnClass(i))
                    .getTableCellRendererComponent(table, longValue, false, false, 0,
                            i);
            cellWidth = comp.getPreferredSize().width;

            if (DEBUG) {
                System.out.println("Initializing width of column " + i + ". "
                        + "headerWidth = " + headerWidth + "; cellWidth = " + cellWidth);
            }

            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
            longValue = null;
        }
    }

    public void setUpSportColumn(TableColumn sportColumn) {
        // Set up the editor for the sport cells.
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("Snowboarding");
        comboBox.addItem("Rowing");
        comboBox.addItem("Knitting");
        comboBox.addItem("Speed reading");
        comboBox.addItem("Pool");
        comboBox.addItem("None of the above");
        sportColumn.setCellEditor(new DefaultCellEditor(comboBox));

        // Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        sportColumn.setCellRenderer(renderer);
    }

    /**
     * Represents the table model.
     */
    private class MyTableModel extends AbstractTableModel {
        private final String[] columnNames;
        private final Object[][] data;

        public MyTableModel() {
            columnNames = new String[]{"First Name", "Last Name", "Sport",
                    "# of Years", "Vegetarian"};
            data = new Object[][]{
                    {"Kathy", "Smith", "Snowboarding", 5, false},
                    {"John", "Doe", "Rowing", 3, true},
                    {"Sue", "Black", "Knitting", 2, false},
                    {"Jane", "White", "Speed reading", 20, true},
                    {"Joe", "Brown", "None of the above", 10, false}};
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/ editor for
         * each cell. If we didn't implement this method, then the last column would
         * contain text ("true"/"false"), rather than a check box.
         */
        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's editable.
         */
        public boolean isCellEditable(int row, int col) {
            // Note that the data/cell address is constant,
            // no matter where the cell appears onscreen.
            return col >= 2;
        }

        /*
         * Don't need to implement this method unless your table's data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col + " to "
                        + value + " (an instance of " + value.getClass() + ")");
            }

            data[row][col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i = 0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j = 0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("TableRenderDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane.
        TableRenderDemo newContentPane = new TableRenderDemo();
        newContentPane.setOpaque(true); // content panes must be opaque
        frame.setContentPane(newContentPane);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(TableRenderDemo::createAndShowGUI);
    }
}
