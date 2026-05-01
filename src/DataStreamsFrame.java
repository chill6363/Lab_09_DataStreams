import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamsFrame extends JFrame
{
    private final JPanel mainPnl;
    private JPanel inputPnl, outputPnl, footerPnl;
    private JButton addFileBtn, searchFileBtn, quitBtn, clearBtn, searchClearBtn;
    private JTextArea originalFileTa, filteredFileTa;
    private JTextField searchField;
    private File selectedFile;

    public DataStreamsFrame()
    {
        setTitle("DataSteams");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());
        add(mainPnl);
        createInputPanel();
        createOutputPanel();
        createFooterPanel();
    }

    public void createInputPanel()
    {
        inputPnl = new JPanel();
        inputPnl.setLayout(new FlowLayout());
        inputPnl.setBorder(BorderFactory.createTitledBorder("Input"));
        JLabel inputLbl = new JLabel("Search Term");
        searchField = new JTextField(20);
        searchFileBtn = new JButton("Search");
        searchClearBtn = new JButton("Clear");
        searchFileBtn.addActionListener(_ -> {
            if(selectedFile != null)
            {
                String search = searchField.getText().toLowerCase();
                filteredFileTa.setText("");
                try
                {
                    Stream<String> lines = Files.lines(selectedFile.toPath());
                    lines.filter(line -> line.toLowerCase().contains(search))
                            .forEach(line -> filteredFileTa.append(line + "\n"));
                } catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please select a file before searching", "No File Selected", JOptionPane.ERROR_MESSAGE);
            }
        });
        searchClearBtn.addActionListener(e -> searchField.setText(""));
        inputPnl.add(inputLbl);
        inputPnl.add(searchField);
        inputPnl.add(searchFileBtn);
        inputPnl.add(searchClearBtn);
        mainPnl.add(inputPnl, BorderLayout.NORTH);
    }
    public void createOutputPanel()
    {
        outputPnl = new JPanel();
        outputPnl.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel buttonPnl = new JPanel();
        buttonPnl.setLayout(new GridLayout(1, 2));

        gbc.weightx = 1.0;
        gbc.weighty = 0.05;
        gbc.gridwidth = 100;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        outputPnl.add(buttonPnl, gbc);

        outputPnl.setBorder(BorderFactory.createTitledBorder("Output"));
        addFileBtn = new JButton("Add File");
        clearBtn = new JButton("Clear");
        buttonPnl.add(addFileBtn, gbc);
        buttonPnl.add(clearBtn, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        gbc.gridwidth = 1;
        originalFileTa = new JTextArea();
        originalFileTa.setEditable(false);
        originalFileTa.setLineWrap(true);
        originalFileTa.setWrapStyleWord(true);
        originalFileTa.setBorder(BorderFactory.createTitledBorder("Original File"));
        JScrollPane originalFileSc = new JScrollPane(originalFileTa);
        outputPnl.add(originalFileSc, gbc);

        gbc.gridx = 5;
        filteredFileTa = new JTextArea();
        filteredFileTa.setEditable(false);
        filteredFileTa.setLineWrap(true);
        filteredFileTa.setWrapStyleWord(true);
        filteredFileTa.setBorder(BorderFactory.createTitledBorder("Filtered File"));
        JScrollPane filteredFileSc = new JScrollPane(filteredFileTa);
        outputPnl.add(filteredFileSc, gbc);
        mainPnl.add(outputPnl);

        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File", "txt");
        fc.setFileFilter(filter);

        addFileBtn.addActionListener(_ -> {
            int result = fc.showOpenDialog(DataStreamsFrame.this);
            if(result == JFileChooser.APPROVE_OPTION)
            {
                selectedFile = fc.getSelectedFile();
                try(Stream<String> lines = Files.lines(selectedFile.toPath()))
                {
                    String input = lines.collect(Collectors.joining("\n"));
                    originalFileTa.setText(input);
                } catch (IOException ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        clearBtn.addActionListener(_ -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear the current selection?", "Clear Current File", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION)
            {
                System.out.println("wow!!!!");
            }
        });
    }
    public void createFooterPanel()
    {
        footerPnl = new JPanel();
        clearBtn = new JButton("Clear");
        quitBtn = new JButton("Quit");
        footerPnl.setLayout(new FlowLayout());
        footerPnl.setBorder(BorderFactory.createTitledBorder("Quit/Clear"));
        quitBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        });
        clearBtn.addActionListener(_ -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to clear the current selection?", "Clear Current File", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION)
            {
                originalFileTa.setText("");
                filteredFileTa.setText("");
                searchField.setText("");
                selectedFile = null;
            }
        });
        footerPnl.add(clearBtn);
        footerPnl.add(quitBtn);
        mainPnl.add(footerPnl, BorderLayout.SOUTH);
    }
}
